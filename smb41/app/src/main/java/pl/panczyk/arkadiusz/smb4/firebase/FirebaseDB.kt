package pl.panczyk.arkadiusz.smb4.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import pl.panczyk.arkadiusz.smb4.product.Product
import pl.panczyk.arkadiusz.smb4.store.Store

open class FirebaseDB {
    var database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://s18706smb-default-rtdb.europe-west1.firebasedatabase.app")
    val user: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw Exception()
}

object ProductFirebaseDB: FirebaseDB() {
    var productArrayList = ArrayList<Product>()

    val ref = database.getReference("users/${user.uid}/product")
    private val firebaseDao = FirebaseDao(ref)

    fun dbOperationsProduct(product: Product): String = firebaseDao.dbOperationsAdd(product)

    fun dbDeleteProduct(key: String) = firebaseDao.dbDelete(key)

    fun dbUpdateProduct(product: Product) = firebaseDao.dbUpdate(product)

    fun initFromDb() {
        productArrayList.clear()
        firebaseDao.initFromDb()
    }
}

object StoreFirebaseDB: FirebaseDB() {
    var storeArrayList = ArrayList<Store>()

    val ref = database.getReference("users/${user.uid}/store")
    private val firebaseDao = FirebaseDao(ref)

    fun dbOperationsStore(store: Store): String = firebaseDao.dbOperationsAdd(store)

    fun dbDeleteStore(key: String) = firebaseDao.dbDelete(key)

    fun dbUpdateStore(store: Store) = firebaseDao.dbUpdate(store)

    fun initFromDb() {
        storeArrayList.clear()
        firebaseDao.initFromDb()
    }
}