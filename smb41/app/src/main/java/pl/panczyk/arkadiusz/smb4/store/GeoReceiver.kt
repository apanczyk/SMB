package pl.panczyk.arkadiusz.smb4.store

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import pl.panczyk.arkadiusz.smb4.R

class GeoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggering = geoEvent.triggeringGeofences
        when (geoEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                for (geo in triggering) {
                    Toast.makeText(context, geo.requestId, Toast.LENGTH_LONG).show()
                    createNotification(context, intent, geo.requestId, "Welcome in")
                }
                Log.i("geofenceApp", "Enterned the area.",)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                for (geo in triggering) {
                    Toast.makeText(context, geo.requestId, Toast.LENGTH_LONG).show()
                    createNotification(context, intent, geo.requestId, "See you again")
                }
                Log.i("geofenceApp", "Left the area.",)
            }
            else -> {
                Log.e("geofenceApp", "Wrong transition type.")
            }
        }
    }

    private fun createNotification(context: Context, intent: Intent, text: String, welcome: String) {
        val createNotificationIntent = Intent().also {
            it.component = ComponentName(
                "pl.panczyk.arkadiusz.smb4",
                "pl.panczyk.arkadiusz.smb4.store.GeoReceiver"
            )
        }
        val pIntent = PendingIntent.getActivity(
            context,
            1,
            createNotificationIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val channelId = createChannel(context)
        val notification = NotificationCompat.Builder(
            context,
            channelId
        ).setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(welcome)
            .setContentText(text)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(0, notification)
    }

    private fun createChannel(context: Context): String{
        val id = "StoreWelcomeChannel"
        val channel = NotificationChannel(
            id,
            "Store Welcome Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
        return id
    }
}