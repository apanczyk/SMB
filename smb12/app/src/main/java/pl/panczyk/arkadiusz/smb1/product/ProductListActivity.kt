package pl.panczyk.arkadiusz.smb1.product

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.panczyk.arkadiusz.smb1.R
import pl.panczyk.arkadiusz.smb1.databinding.ActivityProductListBinding
import pl.panczyk.arkadiusz.smb1.product.db.Product
import pl.panczyk.arkadiusz.smb1.product.db.ProductViewModel


class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.bt2.setOnClickListener { adapter.showCustomDialog() }
    }

    private fun showCustomDialog(adapter: ProductAdapter) {
        val dialog = Dialog(this@ProductListActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)

        val nameEt: EditText = dialog.findViewById(R.id.name_et)
        val priceEt: EditText = dialog.findViewById(R.id.price_et)
        val quantityEt: EditText = dialog.findViewById(R.id.quantity_et)
        val boughtCb: CheckBox = dialog.findViewById(R.id.bought_cb)
        val submitButton: Button = dialog.findViewById(R.id.submit_button)
        submitButton.setOnClickListener {
            val name = nameEt.text.toString()
            val price = priceEt.text.toString().toDouble()
            val quantity = quantityEt.text.toString().toInt()
            val bought = boughtCb.isChecked

            adapter.add(
                Product(
                    name, price, quantity, bought
                )
            )
            dialog.dismiss()
        }
        dialog.show()
    }
}