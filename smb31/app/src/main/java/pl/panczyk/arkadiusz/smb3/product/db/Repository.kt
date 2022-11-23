package pl.panczyk.arkadiusz.smb3.product.db

import androidx.lifecycle.LiveData

class Repository(private val productDao: ProductDao) {

    val allProducts = productDao.getProducts()

    fun get(productId: Long): LiveData<Product> = productDao.get(productId)

    fun insert(product: Product): Long = productDao.insert(product)

    fun update(product: Product) = productDao.update(product)

    fun delete(id: Long) = productDao.delete(id)

    fun deleteAll() = productDao.deleteAll()

}