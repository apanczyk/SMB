package pl.panczyk.arkadiusz.smb1.product

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


    suspend fun insert(product: Product) = repository.insert(product)

    suspend fun update(product: Product) = repository.update(product)

    suspend fun delete(id: Long) = repository.delete(id)

    suspend fun deleteAll() = repository.deleteAll()

}