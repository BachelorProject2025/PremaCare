package no.hiof.bachelor.premacare.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class FeedingRecord(
    val amount: Int = 0,
    val time: Timestamp = Timestamp.now(), // Tidspunkt som Timestamp (Firestore-standard)
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()), // Dato som String
    val pee: Boolean = false,
    val poo: Boolean = false,
    val feedingMethod: String = "",
    val comment: String = ""
)