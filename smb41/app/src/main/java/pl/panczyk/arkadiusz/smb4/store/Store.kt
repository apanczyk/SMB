package pl.panczyk.arkadiusz.smb4.store

import pl.panczyk.arkadiusz.smb4.firebase.FirebaseIdentity

data class Store(
    var name: String,
    var description: String,
    var radius: Long,
    var latitude: Double,
    var longitude: Double,
    override var id: String = "0"
): FirebaseIdentity
