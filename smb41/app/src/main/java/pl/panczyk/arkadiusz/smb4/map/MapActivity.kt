package pl.panczyk.arkadiusz.smb4.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb4.R
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.store.Store

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var firebaseDB: StoreFirebaseDB = StoreFirebaseDB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        firebaseDB.ref.push().also {
            firebaseDB.storeArrayList.forEach {
                googleMap.apply {
                    val newLoc = LatLng(it.latitude, it.longitude)
                    addMarker(
                        MarkerOptions()
                            .position(newLoc)
                            .title("${it.name}: ${it.description}")
                    )
                }
            }
        }
    }

    init {
        firebaseDB.ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    addFromHashMap(snapshot)
                    withContext(Dispatchers.Main) {
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun addFromHashMap(snapshot: DataSnapshot) {
        (snapshot.value as HashMap<*, *>).let {
            Store(
                it["name"] as String,
                it["description"] as String,
                it["radius"] as Long,
                it["latitude"] as Double,
                it["longitude"] as Double,
                snapshot.key!!
            )
        }.run(firebaseDB.storeArrayList::add)
    }
}