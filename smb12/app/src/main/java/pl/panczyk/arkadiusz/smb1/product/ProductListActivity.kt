package pl.panczyk.arkadiusz.smb1.product

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb1.R
import pl.panczyk.arkadiusz.smb1.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.et1.setText(intent.getStringExtra("imie"))

        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val svm = ProductViewModel(application)
        val adapter = ProductAdapter(svm)

        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        ) //optional
        binding.rv1.adapter = adapter
        svm.allProducts.observe(this) {
            it.let {
                adapter.setStudents(it)
            }
        }

        binding.bt1.setOnClickListener {
            binding.tv1.text = getString(R.string.productText) + binding.et1.text
            editor.apply()
            adapter.add(
                Product(
                    name = binding.et1.text.toString(),
                    price = 2.0,
                    quantity = 2,
                    bought = true
                )
            )
        }
    }
}