package pl.panczyk.arkadiusz.smb3.product.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Product(
    var name: String,
    var price: Double,
    var quantity: Int,
    var bought: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
): Serializable