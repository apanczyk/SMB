package pl.panczyk.arkadiusz.smb4.store

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb4.R
import pl.panczyk.arkadiusz.smb4.databinding.StoreListElementBinding
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class StoreAdapter(
    private val context: StoreListActivity,
    val intent: Intent,
    private val firebaseDB: StoreFirebaseDB
) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    class ViewHolder(val binding: StoreListElementBinding) : RecyclerView.ViewHolder(binding.root)
    private lateinit var geoClient: GeofencingClient


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = StoreListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setSize(holder.binding)
        setColor(holder.binding)
        holder.binding.storeName.text = firebaseDB.storeArrayList[position].name
        holder.binding.storeDescription.text = firebaseDB.storeArrayList[position].description
        holder.binding.storeRadius.text = firebaseDB.storeArrayList[position].radius.toString()
        holder.binding.storeGeolocation.text =
            "${firebaseDB.storeArrayList[position].latitude}/${firebaseDB.storeArrayList[position].longitude}"

        holder.binding.deleteButton.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Deleted product with id: ${firebaseDB.storeArrayList[position].id}",
                Toast.LENGTH_LONG
            ).show()
            firebaseDB.dbDeleteStore(firebaseDB.storeArrayList[position].id)
        }
        holder.binding.editButton.setOnClickListener {
            context.findLocation(firebaseDB.storeArrayList[position])
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun showCustomDialog(location: Location, store: Store? = null) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog_store)

        val nameEt: EditText = dialog.findViewById(R.id.name_set)
        val descriptionEt: EditText = dialog.findViewById(R.id.description_et)
        val radiusEt: EditText = dialog.findViewById(R.id.radius_et)
        val submitButton: Button = dialog.findViewById(R.id.submit_button)

        if (store != null) {
            nameEt.text = store.name.toEditable()
            descriptionEt.text = store.description.toEditable()
            radiusEt.text = store.radius.toString().toEditable()
        }

        submitButton.setOnClickListener {
            if (store != null) {
                store.apply {
                    name = nameEt.text.toString()
                    description = descriptionEt.text.toString()
                    radius = radiusEt.text.toString().toLong()
                }
                firebaseDB.dbUpdateStore(store)
            } else {
                Store(
                    nameEt.text.toString(),
                    descriptionEt.text.toString(),
                    radiusEt.text.toString().toLong(),
                    location.latitude,
                    location.longitude
                ).also{ store ->
                    firebaseDB.dbOperationsStore(store).also {
                        addGeos(location, store, it)
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addGeos(location: Location, store: Store, key: String) {
        addGeo(location, store, key, Geofence.GEOFENCE_TRANSITION_ENTER, "W")
        addGeo(location, store, key, Geofence.GEOFENCE_TRANSITION_EXIT, "S")
    }

    @SuppressLint("MissingPermission")
    private fun addGeo(location: Location, store: Store, key: String, type: Int, text: String){
        val geofence = Geofence.Builder()
            .setCircularRegion(location.latitude, location.longitude, store.radius.toFloat())
            .setExpirationDuration(30*60*1000)
            .setRequestId("${store.name} ${store.description} key: $key $text")
            .setTransitionTypes(type)
            .build()

        val geoRequest = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .build()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, GeoReceiver::class.java),
            PendingIntent.FLAG_MUTABLE
        )
        geoClient = LocationServices.getGeofencingClient(context)
        geoClient.addGeofences(geoRequest, pendingIntent)
            .addOnSuccessListener {
                Log.i("geofenceApp", "Geofence: $key  is added!")
            }
            .addOnFailureListener {
                Log.e("geofenceApp", it.message.toString())
            }
    }

    private fun setSize(binding: StoreListElementBinding) {
        binding.storeName.textSize = Options.size
        binding.storeDescription.textSize = Options.size
        binding.storeRadius.textSize = Options.size
        binding.storeGeolocation.textSize = Options.size
    }

    private fun setColor(binding: StoreListElementBinding) {
        binding.editButton.setBackgroundColor(Options.color)
        binding.deleteButton.setBackgroundColor(Options.color)
    }

    override fun getItemCount(): Int = firebaseDB.storeArrayList.size

    init {
        firebaseDB.ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    addFromHashMap(snapshot)
                    withContext(Dispatchers.Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    firebaseDB.storeArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    addFromHashMap(snapshot)
                    withContext(Dispatchers.Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    firebaseDB.storeArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    withContext(Dispatchers.Main) {
                        notifyDataSetChanged()
                    }
                }
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

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}