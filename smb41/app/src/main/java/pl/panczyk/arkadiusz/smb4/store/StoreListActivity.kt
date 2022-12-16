package pl.panczyk.arkadiusz.smb4.store

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb4.databinding.ActivityStoreListBinding
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class StoreListActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityStoreListBinding
    private lateinit var adapter: StoreAdapter
    private lateinit var firebaseDB: StoreFirebaseDB

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
        adapter.storeArrayList

        binding.bt2.setOnClickListener { adapter.showCustomDialog() }
        binding.bt2.setBackgroundColor(Options.color)
    }

    override fun onResume() {
        super.onResume()
        firebaseDB.initFromDb()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sharedPreferences.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sharedPreferences.getFloat(Options.SIZE, Options.BASIC_SIZE)
        Options.color = color
        Options.size = size
    }
}