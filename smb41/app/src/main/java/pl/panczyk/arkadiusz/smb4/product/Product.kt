package pl.panczyk.arkadiusz.smb4.product

data class Product(
    var name: String,
    var price: Double,
    var quantity: Int,
    var bought: Boolean,
    var id: String = "0"
)