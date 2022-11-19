package pl.panczyk.arkadiusz.smb2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AddProductReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action=="pl.panczyk.arkadiusz.smb1.action.AddProduct") {
            //Tworzymy kanał notyfikacji
            val channelid = createChannel(context)
            //Tworzymy przepustkę
            val addStudentIntent = Intent().also {
                it.component = ComponentName(context, MainActivity::class.java)
            }
            val pendintIntent = PendingIntent.getActivity(
                context,
                1,
                addStudentIntent,
                PendingIntent.FLAG_MUTABLE
            )

            //Tworzymy notyfikację
            val notification = NotificationCompat.Builder(
                context,
                channelid
            ).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Dodano studenta:")
                .setContentText(intent.getStringExtra("product"))
                .setContentIntent(pendintIntent)
                .setAutoCancel(true)
                .build()

            //Pokazujemy notyfikację
            NotificationManagerCompat.from(context).notify(0, notification)
        }else{
            Toast.makeText(context, "Unknown action.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createChannel(context: Context): String{
        val id = "ProductAddChannel"
        val channel = NotificationChannel(
            id,
            "Product Add Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
        return id
    }
}