package pl.panczyk.arkadiusz.smb2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyProductService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotification(this, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotification(context: Context, intent: Intent) {
        val text = intent.getStringExtra("productId")
        val addProductIntent = Intent().also {
            it.putExtra("productId", text.toString())
            it.component = ComponentName(
                "pl.panczyk.arkadiusz.smb1",
                "pl.panczyk.arkadiusz.smb1.product.ProductListActivity"
            )
        }
        val pIntent = PendingIntent.getActivity(
            context,
            1,
            addProductIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val channelId = createChannel(context)
        val notification = NotificationCompat.Builder(
            context,
            channelId
        ).setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Dodano produkt:")
            .setContentText(text)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()

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