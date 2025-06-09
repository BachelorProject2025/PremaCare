package no.hiof.bachelor.premacare.model

import com.google.firebase.firestore.PropertyName // Viktig: Husk å importere denne!

data class Message(
    val senderid: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    @get:PropertyName("isRead") // Denne linjen er nøkkelen!
    var isRead: Boolean = false // Endret fra 'val' til 'var' siden den endres i Firestore
)