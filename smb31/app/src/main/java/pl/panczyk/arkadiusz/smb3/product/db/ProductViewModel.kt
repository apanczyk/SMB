package pl.panczyk.arkadiusz.smb3.product.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allProducts: LiveData<List<Product>>

    init {
        repository = Repository(ProductDB.getDatabase(application)!!.productDao())
        allProducts = repository.allProducts
    }

    fun get(productId: Long): Product = repository.get(productId).value!!

    fun insert(product: Product): Long = repository.insert(product)

    fun update(product: Product) = repository.update(product)

    fun delete(id: Long) = repository.delete(id)

    fun deleteAll() = repository.deleteAll()

}