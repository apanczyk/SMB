package pl.panczyk.arkadiusz.smb1.product.db

class Repository(private val productDao: ProductDao) {

    val allProducts = productDao.getProducts()

    suspend fun insert(product: Product) = productDao.insert(product)

    suspend fun update(product: Product) = productDao.update(product)

    suspend fun delete(id: Long) = productDao.delete(id)

    suspend fun deleteAll() = productDao.deleteAll()

}