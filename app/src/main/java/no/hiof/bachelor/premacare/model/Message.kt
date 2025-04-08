package no.hiof.bachelor.premacare.model

data class Message(
    val senderid: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
