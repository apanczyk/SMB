package pl.panczyk.arkadiusz.smb4.store

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import pl.panczyk.arkadiusz.smb4.databinding.ActivityStoreListBinding
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class StoreListActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityStoreListBinding
    private lateinit var adapter: StoreAdapter
    private lateinit var firebaseDB: StoreFirebaseDB

    @RequiresApi(Build.VERSION_CODES.S)
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
        firebaseDB.storeArrayList

        binding.bt2.setOnClickListener { findLocation() }
        binding.bt2.setBackgroundColor(Options.color)
    }

    override fun onResume() {
        super.onResume()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sharedPreferences.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sharedPreferences.getFloat(Options.SIZE, Options.BASIC_SIZE)
        Options.color = color
        Options.size = size
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
                    adapter.showCustomDialog(it, store)
                }
            }
            .addOnFailureListener {
                Log.e("geofenceApp", "Location error: ${it.message.toString()}")
            }
    }
}