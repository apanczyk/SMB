package pl.panczyk.arkadiusz.smb4.store

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import pl.panczyk.arkadiusz.smb4.databinding.ActivityStoreListBinding
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class StoreListActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityStoreListBinding
    private lateinit var adapter: StoreAdapter
    private lateinit var firebaseDB: StoreFirebaseDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDB = StoreFirebaseDB()
        adapter = StoreAdapter(this, intent, firebaseDB)

        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rv1.adapter = adapter
        adapter.storeArrayList

        binding.bt2.setOnClickListener { findLocation() }
        binding.bt2.setBackgroundColor(Options.color)
    }

    override fun onResume() {
        super.onResume()
        firebaseDB.initFromDb()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sharedPreferences.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sharedPreferences.getFloat(Options.SIZE, Options.BASIC_SIZE)
        Options.color = color
        Options.size = size
    }

    fun findLocation(store: Store? = null) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), 1
            )
        }
        LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnSuccessListener {
                if (it == null) {
                    Log.e("geofenceApp", "Location is null.")
                } else {
                    Log.i("geofenceApp", "Location: ${it.latitude}, ${it.longitude}")
                    adapter.showCustomDialog(Pair(it.latitude, it.longitude), store)
                }
            }
            .addOnFailureListener {
                Log.e("geofenceApp", "Location error: ${it.message.toString()}")
            }
    }
}