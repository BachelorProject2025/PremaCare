package no.hiof.bachelor.premacare.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class FirebaseViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var _email = mutableStateOf("")
    val email = _email

    private var _password = mutableStateOf("")
    val password = _password

    private var _username = mutableStateOf("")
    val username = _username

    private var _childsName = mutableStateOf("")
    val childsName = _childsName

    private var _chilDateOfBirth = mutableStateOf("")
    val chilDateOfBirth = _chilDateOfBirth

    private var _parentName = mutableStateOf("")
    val parentName = _parentName

    private var _phoneNumber = mutableStateOf("")
    val phoneNumer = _phoneNumber

    private var _memberSince = mutableStateOf("")
    val memberSince = _memberSince



    fun registerUser(onResult: (Boolean) -> Unit) {
        val emailValue = email.value
        val passwordValue = password.value
        val parentNameValue = parentName.value
        val phoneNumerValue = phoneNumer.value
        val childsNameValue = childsName.value
        val chilDateOfBirthValue = chilDateOfBirth.value

        if (emailValue.isNotBlank() && passwordValue.isNotBlank() && childsNameValue.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Opprett bruker i Firebase Authentication
                    val authResult = auth.createUserWithEmailAndPassword(emailValue, passwordValue).await()
                    val currentUser = authResult.user

                    if (currentUser != null) {
                        val userId = currentUser.uid
                        val userMap = hashMapOf(
                            "chilDateOfBirth" to chilDateOfBirthValue,
                            "childsName" to childsNameValue,
                            "parentName" to parentNameValue,
                            "phoneNumer" to phoneNumerValue,
                            "email" to emailValue
                        )

                        // Lagre brukerinfo i Firestore
                        firestore.collection("users").document(userId).set(userMap).await()

                        // Oppdater UI på hovedtråden
                        withContext(Dispatchers.Main) {
                            fetchChildsName() // Henter barnets navn etter registrering
                            onResult(true) // Registrering vellykket
                        }
                    } else {
                        withContext(Dispatchers.Main) { onResult(false) }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onResult(false) }
                }
            }
        } else {
            onResult(false) // Feil hvis noen felt er tomme
        }
    }


    // Fetch the username from Firestore
    fun fetchChildsName() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userSnapshot = firestore.collection("users").document(userId).get().await()
                    _childsName.value = userSnapshot.getString("childsName") ?: ""
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }



    // Fetch the user's member since date
    fun fetchMemberSinceDate() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val creationTimestamp = user.metadata?.creationTimestamp ?: 0
            // Format the date
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            _memberSince.value = dateFormat.format(creationTimestamp)
        }
    }

    fun loginUser(onResult: (Boolean) -> Unit) {
        val emailValue = email.value
        val passwordValue = password.value
        if (emailValue.isNotBlank() && passwordValue.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(emailValue, passwordValue).await()
                    fetchChildsName()

                    // Notify success
                    withContext(Dispatchers.Main) {
                        onResult(true)
                    }
                } catch (e: Exception) {
                    // Handle login failure
                    withContext(Dispatchers.Main) {
                        onResult(false)
                    }
                    println(e.message)
                }
            }
        } else {
            onResult(false)
        }
    }

    fun logOut() {
        auth.signOut()
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun isValidRegistration(): Boolean {
        return email.value.isNotBlank() && password.value.isNotBlank()
    }

}




