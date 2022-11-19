package pl.panczyk.arkadiusz.smb2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class MyProductService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotification(this, intent)
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotification(context: Context, intent: Intent) {
        val channelid = createChannel(context)
        val text = intent.getStringExtra("product")

        val addStudentIntent = Intent().also {
            it.putExtra("product", text.toString())
            it.component = ComponentName(
                "pl.panczyk.arkadiusz.smb1",
                "pl.panczyk.arkadiusz.smb1.product.ProductListActivity"
            )
        }
        val pIntent = PendingIntent.getActivity(
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
            .setContentTitle("Dodano produkt:")
            .setContentText(intent.getStringExtra("product"))
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()

        //Pokazujemy notyfikację
        NotificationManagerCompat.from(context).notify(0, notification)
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