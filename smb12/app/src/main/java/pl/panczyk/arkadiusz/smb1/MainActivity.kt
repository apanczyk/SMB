package pl.panczyk.arkadiusz.smb1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import pl.panczyk.arkadiusz.smb1.option.OptionsActivity
import pl.panczyk.arkadiusz.smb1.product.ProductListActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listview)
        val list: MutableList<String?> = ArrayList()
        list.add("Options")
        list.add("Products List")

        val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, list)
        listView.adapter = arrayAdapter
        listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this@MainActivity, OptionsActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity, ProductListActivity::class.java))
                else -> throw Exception()
            }
        }
    }

    // TODO
    data class ViewHolder(
        var thumbnail: ImageView? = null,
        var title: TextView? = null,
        var button: Button? = null,
        var intent: Intent? = null
    )
}