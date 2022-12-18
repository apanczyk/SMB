package pl.panczyk.arkadiusz.smb4.store

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggering = geoEvent.triggeringGeofences
        when (geoEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                for (geo in triggering)
                    Toast.makeText(context, geo.requestId, Toast.LENGTH_LONG).show()
                Log.i("geofenceApp", "Enterned the area.",)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                for (geo in triggering)
                    Toast.makeText(context, geo.requestId, Toast.LENGTH_LONG).show()
                Log.i("geofenceApp", "Left the area.",)
            }
            else -> {
                Log.e("geofenceApp", "Wrong transition type.")
            }
        }
    }
}