package pl.panczyk.arkadiusz.smb3.product.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name: String,
    var price: Double,
    var quantity: Int,
    var bought: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)