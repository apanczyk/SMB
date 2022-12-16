package pl.panczyk.arkadiusz.smb4.product

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb4.databinding.ActivityProductListBinding
import pl.panczyk.arkadiusz.smb4.firebase.ProductFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options


class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var firebaseDB: ProductFirebaseDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDB = ProductFirebaseDB()
        adapter = ProductAdapter(this, intent, firebaseDB)

        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rv1.adapter = adapter
        adapter.productArrayList

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