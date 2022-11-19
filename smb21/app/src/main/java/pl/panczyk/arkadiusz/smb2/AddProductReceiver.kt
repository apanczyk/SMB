package pl.panczyk.arkadiusz.smb2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class AddProductReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action=="pl.panczyk.arkadiusz.smb1.action.AddProduct") {
            context.startService(Intent(context, MyProductService::class.java))
        }else{
            Toast.makeText(context, "Unknown action.", Toast.LENGTH_SHORT).show()
        }
    }
}