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

fun User.toMap(): Map<String, Any> = mapOf(
    "childsName" to childsName,
    "parentName" to parentName,
    "phoneNumber" to phoneNumber,
    "email" to email,
    "password" to password,
    "chilDateOfBirth" to chilDateOfBirth,
    "memberSince" to memberSince
)
