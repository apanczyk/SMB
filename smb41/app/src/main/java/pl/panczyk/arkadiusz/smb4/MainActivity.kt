package pl.panczyk.arkadiusz.smb4

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import pl.panczyk.arkadiusz.smb4.map.MapActivity
import pl.panczyk.arkadiusz.smb4.option.Options
import pl.panczyk.arkadiusz.smb4.option.OptionsActivity
import pl.panczyk.arkadiusz.smb4.product.ProductListActivity
import pl.panczyk.arkadiusz.smb4.store.StoreListActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSharedPreferences()

        val listView: ListView = findViewById(R.id.listview)
        val list: MutableList<String?> = ArrayList()
        list.add("Options")
        list.add("Products List")
        list.add("Store List")
        list.add("Map")

        val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list)
        listView.adapter = arrayAdapter
        listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this@MainActivity, OptionsActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity, ProductListActivity::class.java))
                2 -> startActivity(Intent(this@MainActivity, StoreListActivity::class.java))
                3 -> startActivity(Intent(this@MainActivity, MapActivity::class.java))
                else -> throw Exception()
            }
        }
    }

    private fun loadSharedPreferences() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sharedPreferences.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sharedPreferences.getFloat(Options.SIZE, Options.BASIC_SIZE)
        Options.color = color
        Options.size = size
    }
}