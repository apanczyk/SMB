package pl.panczyk.arkadiusz.smb4.store

import pl.panczyk.arkadiusz.smb4.firebase.FirebaseIdentity

data class Store(
    var name: String = "",
    var description: String = "",
    var radius: Long = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    override var id: String = "0"
): FirebaseIdentity
