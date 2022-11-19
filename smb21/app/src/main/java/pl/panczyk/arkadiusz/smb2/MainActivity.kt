package pl.panczyk.arkadiusz.smb2

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.panczyk.arkadiusz.smb2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: AddProductReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = AddProductReceiver()

        binding.btn1.setOnClickListener {
            sendBroadcast(Intent().also {
                it.putExtra("product", "text")
                it.action="pl.panczyk.arkadiusz.smb1.action.AddProduct"
            })
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            receiver,
            IntentFilter("pl.panczyk.arkadiusz.smb1.action.AddProduct")
        )
    }
}