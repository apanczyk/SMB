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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        if(GeofencingEvent.fromIntent(intent).geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Toast.makeText(context, "Weszliśmy w obszar.", Toast.LENGTH_SHORT).show()
            Log.i("geofenceApp", "Weszliśmy w obszar.",)
        }else{
            Log.e("geofenceApp", "Wrong transition type.")
        }
    }
}