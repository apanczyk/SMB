package pl.panczyk.arkadiusz.smb1.product

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb1.R
import pl.panczyk.arkadiusz.smb1.databinding.ProductListElementBinding
import pl.panczyk.arkadiusz.smb1.product.db.Product
import pl.panczyk.arkadiusz.smb1.product.db.ProductViewModel

class ProductAdapter(private val svm: ProductViewModel, private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductListElementBinding) : RecyclerView.ViewHolder(binding.root)

    private var products = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProductListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productName.text = products[position].name
        holder.binding.productPrice.text = products[position].price.toString()
        holder.binding.productQuantity.text = products[position].price.toString()
        holder.binding.productBought.isChecked = products[position].bought
        holder.binding.deleteButton.setOnClickListener {
            delete(products[position].id)
            Toast.makeText(
                holder.binding.root.context,
                "Deleted product with id: ${products[position].id}",
                Toast.LENGTH_LONG
            ).show()
        }
        holder.binding.editButton.setOnClickListener {
            showCustomDialog(products[position])
        }
    }

    fun showCustomDialog(product: Product? = null) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)

        val nameEt: EditText = dialog.findViewById(R.id.name_et)
        val priceEt: EditText = dialog.findViewById(R.id.price_et)
        val quantityEt: EditText = dialog.findViewById(R.id.quantity_et)
        val boughtCb: CheckBox = dialog.findViewById(R.id.bought_cb)
        val submitButton: Button = dialog.findViewById(R.id.submit_button)

        if(product != null) {
            nameEt.text = product.name.toEditable()
            priceEt.text = product.price.toString().toEditable()
            quantityEt.text = product.quantity.toString().toEditable()
            boughtCb.isChecked = product.bought
        }

        submitButton.setOnClickListener {
            if(product != null) {
                product.apply {
                    name = nameEt.text.toString()
                    price = priceEt.text.toString().toDouble()
                    quantity = quantityEt.text.toString().toInt()
                    bought = boughtCb.isChecked
                }
                this.update(product)
            } else {
                val name = nameEt.text.toString()
                val price = priceEt.text.toString().toDouble()
                val quantity = quantityEt.text.toString().toInt()
                val bought = boughtCb.isChecked
                this.add(
                    Product(
                        name, price, quantity, bought
                    )
                )
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun getItemCount(): Int = products.size

    fun add(product: Product){
        CoroutineScope(IO).launch {
            svm.insert(product)
            withContext(Main){
            }
        }
        notifyDataSetChanged()
    }

    fun update(product: Product){
        CoroutineScope(IO).launch {
            svm.update(product)
        }
        notifyDataSetChanged()
    }

    fun delete(id: Long){
        CoroutineScope(IO).launch {
            svm.delete(id)
        }
        notifyDataSetChanged()
    }

    fun setStudents(allProducts: List<Product>){
        products = allProducts
        notifyDataSetChanged()
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
