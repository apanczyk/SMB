package pl.panczyk.arkadiusz.smb2

import android.app.Activity
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var receiver: AddProductReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "CREATED", Toast.LENGTH_LONG).show()
        receiver = AddProductReceiver()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            receiver,
            IntentFilter("pl.panczyk.arkadiusz.smb1.action.AddProduct")
        )
    }
}