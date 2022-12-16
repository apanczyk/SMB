package pl.panczyk.arkadiusz.smb4.product

import pl.panczyk.arkadiusz.smb4.firebase.FirebaseIdentity

data class Product(
    var name: String,
    var price: Double,
    var quantity: Int,
    var bought: Boolean,
    override var id: String = "0"
): FirebaseIdentity