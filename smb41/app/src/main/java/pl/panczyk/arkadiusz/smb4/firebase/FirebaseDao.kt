package pl.panczyk.arkadiusz.smb4.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseDao(private val ref: DatabaseReference) {

    fun <T : FirebaseIdentity> dbOperationsAdd(product: T): String =
        ref.push().let {
            product.apply {
                id = it.key ?: throw Exception("No key found")
            }.run(it::setValue)
            it.key ?: throw Exception("No key found")
        }

    fun dbDelete(key: String) {
        ref.child(key).removeValue()
    }

    fun <T : FirebaseIdentity> dbUpdate(product: T) {
        ref.child(product.id).setValue(product)
    }

    fun initFromDb() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    dataSnapshot.children.iterator().next()
                } catch (_: Exception) {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("readDB-error", databaseError.details)
            }
        })
    }
}