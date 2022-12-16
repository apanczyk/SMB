package pl.panczyk.arkadiusz.smb4.store

import android.annotation.SuppressLint
import android.app.Dialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.panczyk.arkadiusz.smb4.R
import pl.panczyk.arkadiusz.smb4.databinding.StoreListElementBinding
import pl.panczyk.arkadiusz.smb4.firebase.StoreFirebaseDB
import pl.panczyk.arkadiusz.smb4.option.Options

class StoreAdapter(private val context: Context, val intent: Intent, private val firebaseDB: StoreFirebaseDB) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    class ViewHolder(val binding: StoreListElementBinding) : RecyclerView.ViewHolder(binding.root)

    var storeArrayList = ArrayList<Store>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = StoreListElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setSize(holder.binding)
        setColor(holder.binding)
        holder.binding.storeName.text = storeArrayList[position].name
        holder.binding.storeDescription.text = storeArrayList[position].description
        holder.binding.storeRadius.text = storeArrayList[position].radius.toString()
        holder.binding.storeGeolocation.text = "${storeArrayList[position].latitude}/${storeArrayList[position].longitude}"

        holder.binding.deleteButton.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Deleted product with id: ${storeArrayList[position].id}",
                Toast.LENGTH_LONG
            ).show()
            firebaseDB.dbDeleteStore(storeArrayList[position].id)
        }
        holder.binding.editButton.setOnClickListener {
            showCustomDialog(storeArrayList[position])
        }
        if(intent.hasExtra("storeId") && intent.getStringExtra("storeId") == storeArrayList[position].id) {
            showCustomDialog(storeArrayList[position])
            intent.removeExtra("storeId")
        }
    }

    fun showCustomDialog(store: Store? = null) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog_store)

        val nameEt: EditText = dialog.findViewById(R.id.name_set)
        val descriptionEt: EditText = dialog.findViewById(R.id.description_et)
        val radiusEt: EditText = dialog.findViewById(R.id.radius_et)
        val submitButton: Button = dialog.findViewById(R.id.submit_button)

        if(store != null) {
            nameEt.text = store.name.toEditable()
            descriptionEt.text = store.description.toEditable()
            radiusEt.text = store.radius.toString().toEditable()
        }

        submitButton.setOnClickListener {
            if(store != null) {
                store.apply {
                    name = nameEt.text.toString()
                    description = descriptionEt.text.toString()
                    radius = radiusEt.text.toString().toLong()
                }
                firebaseDB.dbUpdateStore(store)
            } else {
                val name = nameEt.text.toString()
                val description = descriptionEt.text.toString()
                val radius = radiusEt.text.toString().toLong()
                val latitude = 0.2
                val longitude = 0.2
                firebaseDB.dbOperationsStore(Store(name,description,radius, latitude, longitude))
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setSize(binding: StoreListElementBinding) {
        binding.storeName.textSize = Options.size
        binding.storeDescription.textSize = Options.size
        binding.storeRadius.textSize = Options.size
        binding.storeGeolocation.textSize = Options.size
    }

    private fun setColor(binding: StoreListElementBinding) {
        binding.editButton.setBackgroundColor(Options.color)
        binding.deleteButton.setBackgroundColor(Options.color)
    }

    override fun getItemCount(): Int = storeArrayList.size

    init {
        firebaseDB.ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    addFromHashMap(snapshot)
                    withContext(Dispatchers.Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(Dispatchers.IO).launch {
                    storeArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    addFromHashMap(snapshot)
                    withContext(Dispatchers.Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    storeArrayList.removeIf {
                        it.id == (snapshot.value as HashMap<*, *>)["id"]
                    }
                    withContext(Dispatchers.Main) {
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
            Store(
                it["name"] as String,
                it["description"] as String,
                it["radius"] as Long,
                it["latitude"] as Double,
                it["longitude"] as Double,
                snapshot.key!!
            )
        }.run(storeArrayList::add)
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}