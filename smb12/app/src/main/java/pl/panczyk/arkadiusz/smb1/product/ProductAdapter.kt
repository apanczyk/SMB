package pl.panczyk.arkadiusz.smb1.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb1.MainActivity
import pl.panczyk.arkadiusz.smb1.databinding.ProductListElementBinding

class ProductAdapter(private val svm: ProductViewModel) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductListElementBinding) : RecyclerView.ViewHolder(binding.root)

    private var products = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productName.text = products[position].name
        holder.binding.productPrice.text = products[position].price.toString()
        holder.binding.productQuantity.text = products[position].price.toString()
        holder.binding.productBought.isChecked = products[position].bought
        holder.binding.root.setOnClickListener {
            delete(products[position].id)
            Toast.makeText(
                holder.binding.root.context,
                "Usunięto produkt o id: ${products[position].id}",
                Toast.LENGTH_LONG
            ).show()
        }
        holder.binding.root.setOnLongClickListener {
            val intent = Intent(holder.binding.root.context, MainActivity::class.java)
            intent.putExtra("imie", products[position].name)
            startActivity(
                holder.binding.root.context,
                intent,
                Bundle()
            )
        true
        }
    }

    override fun getItemCount(): Int = products.size

    fun add(product: Product){
        CoroutineScope(IO).launch {
            svm.insert(product)
            withContext(Main){
                //zmiana w GUI
            }
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

}
