package no.hiof.bachelor.premacare.model

data class User(
    val childsName: String,
    val parentName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val chilDateOfBirth: String,
    val memberSince: String
)
