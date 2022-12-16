package pl.panczyk.arkadiusz.smb4.product

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb4.R
import pl.panczyk.arkadiusz.smb4.databinding.ProductListElementBinding
import pl.panczyk.arkadiusz.smb4.firebase.ProductFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class ProductAdapter(private val context: Context, val intent: Intent, private val firebaseDB: ProductFirebaseDB) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductListElementBinding) : RecyclerView.ViewHolder(binding.root)

    var productArrayList = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProductListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setSize(holder.binding)
        setColor(holder.binding)
        holder.binding.productName.text = productArrayList[position].name
        holder.binding.productPrice.text = productArrayList[position].price.toString()
        holder.binding.productQuantity.text = productArrayList[position].quantity.toString()
        holder.binding.productBought.isChecked = productArrayList[position].bought
        holder.binding.deleteButton.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Deleted product with id: ${productArrayList[position].id}",
                Toast.LENGTH_LONG
            ).show()
            firebaseDB.dbDeleteProduct(productArrayList[position].id)
        }
        holder.binding.editButton.setOnClickListener {
            showCustomDialog(productArrayList[position])
        }
        if(intent.hasExtra("productId") && intent.getStringExtra("productId") == productArrayList[position].id) {
            showCustomDialog(productArrayList[position])
            intent.removeExtra("productId")
        }
    }

    fun showCustomDialog(product: Product? = null) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)

        val nameEt: EditText = dialog.findViewById(R.id.name_et)
        val priceEt: EditText = dialog.findViewById(R.id.description_et)
        val quantityEt: EditText = dialog.findViewById(R.id.radius_et)
        val boughtCb: CheckBox = dialog.findViewById(R.id.bought_cb)
        val submitButton: Button = dialog.findViewById(R.id.submit_button)

        if(product != null) {
            nameEt.text = product.name.toEditable()
            priceEt.text = product.price.toString().toEditable()
            quantityEt.text = product.quantity.toString().toEditable()
            boughtCb.isChecked = product.bought
        }

        submitButton.setOnClickListener {
            lateinit var productToSave: Product
            if(product != null) {
                product.apply {
                    name = nameEt.text.toString()
                    price = priceEt.text.toString().toDouble()
                    quantity = quantityEt.text.toString().toInt()
                    bought = boughtCb.isChecked
                }
                firebaseDB.dbUpdateProduct(product)
                productToSave = product
            } else {
                val name = nameEt.text.toString()
                val price = priceEt.text.toString().toDouble()
                val quantity = quantityEt.text.toString().toInt()
                val bought = boughtCb.isChecked
                productToSave = Product(name, price, quantity, bought)
                firebaseDB.dbOperationsProduct(productToSave)
            }
            sendBroadcast(productToSave.id)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendBroadcast(productId: String) {
        context.sendBroadcast(Intent().also {
            it.putExtra("productId", productId)
            it.action = "pl.panczyk.arkadiusz.smb1.action.AddProduct"
            it.component = ComponentName(
                "pl.panczyk.arkadiusz.smb2",
                "pl.panczyk.arkadiusz.smb2.AddProductReceiver"
            )
        })
    }

    private fun setSize(binding: ProductListElementBinding) {
        binding.productName.textSize = Options.size
        binding.productPrice.textSize = Options.size
        binding.productQuantity.textSize = Options.size
        binding.productBought.textSize = Options.size
    }

    private fun setColor(binding: ProductListElementBinding) {
        binding.editButton.setBackgroundColor(Options.color)
        binding.deleteButton.setBackgroundColor(Options.color)
    }

    override fun getItemCount(): Int = productArrayList.size

    init {
        firebaseDB.ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(IO).launch {
                    addFromHashMap(snapshot)
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(IO).launch {
                    productArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    addFromHashMap(snapshot)
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch {
                    productArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addFromHashMap(snapshot: DataSnapshot) {
        (snapshot.value as HashMap<*, *>).let {
            Product(
                it["name"] as String,
                (it["price"] as Long).toDouble(),
                (it["quantity"] as Long).toInt(),
                it["bought"] as Boolean,
                snapshot.key!!
            )
        }.run(productArrayList::add)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
