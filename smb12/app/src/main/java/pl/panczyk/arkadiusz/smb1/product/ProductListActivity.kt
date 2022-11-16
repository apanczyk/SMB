package pl.panczyk.arkadiusz.smb1.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb1.databinding.ActivityProductListBinding
import pl.panczyk.arkadiusz.smb1.option.Options
import pl.panczyk.arkadiusz.smb1.product.db.ProductViewModel


class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val svm = ProductViewModel(application)
        val adapter = ProductAdapter(svm, this)

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
}