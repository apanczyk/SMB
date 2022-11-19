package pl.panczyk.arkadiusz.smb1.product

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb1.databinding.ActivityProductListBinding
import pl.panczyk.arkadiusz.smb1.option.Options
import pl.panczyk.arkadiusz.smb1.product.db.ProductViewModel


class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var svm: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        svm = ProductViewModel(application)
        adapter = ProductAdapter(svm, this)

        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rv1.adapter = adapter
        svm.allProducts.observe(this) {
            it.let {
                adapter.setProducts(it)
            }
        }

        binding.bt2.setOnClickListener { adapter.showCustomDialog() }
        binding.bt2.setBackgroundColor(Options.color)
    }

    override fun onResume() {
        loadSharedPreferences()
        if(intent.hasExtra("product")) {
            val productId = intent.getStringExtra("product")
            adapter.loadProduct(productId!!.toLong())
        }
        super.onResume()
    }

    private fun loadSharedPreferences() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sharedPreferences.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sharedPreferences.getFloat(Options.SIZE, Options.BASIC_SIZE)
        Options.color = color
        Options.size = size
    }
}