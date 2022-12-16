package pl.panczyk.arkadiusz.smb4.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import pl.panczyk.arkadiusz.smb4.product.Product

class ProductFirebaseDB {

    private var database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://s18706smb-default-rtdb.europe-west1.firebasedatabase.app")
    private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw Exception()
    val ref = database.getReference("users/${user.uid}/product")
    private val productDao = ProductDao(ref)

    fun dbOperationsProduct(product: Product): String = productDao.dbOperationsProduct(product)

    fun dbDeleteProduct(key: String) = productDao.dbDeleteProduct(key)

    fun dbUpdateProduct(product: Product) = productDao.dbUpdateProduct(product)

    fun initFromDb() = productDao.initFromDb()
}