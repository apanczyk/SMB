package pl.panczyk.arkadiusz.smb3.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pl.panczyk.arkadiusz.smb3.product.db.Product

class ProductFirebaseDB {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://s18706smb-default-rtdb.europe-west1.firebasedatabase.app")
    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw Exception()

    fun dbOperationsUser(product: Product): Long{
        val ref = database.getReference("users/"+user.uid)

        val productRef = ref.child("product")
        productRef.push().setValue(product)
        return product.id
    }

    fun readFromDb() {
        val ref = database.getReference("objects/product")
        var name: String
        var surname: String
        var age: Long
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for( messageSnapshot in dataSnapshot.children){
                    name = messageSnapshot.child("name").value as String
                    surname = messageSnapshot.child("surname").value as String
                    age = messageSnapshot.child("age").value as Long
                    Log.i("readDB", "$name $surname $age")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("readDB-error", databaseError.details)
            }

        })
    }
}