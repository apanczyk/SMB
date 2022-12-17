package pl.panczyk.arkadiusz.smb4.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.panczyk.arkadiusz.smb4.R
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var firebaseDB: StoreFirebaseDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        firebaseDB = StoreFirebaseDB

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
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