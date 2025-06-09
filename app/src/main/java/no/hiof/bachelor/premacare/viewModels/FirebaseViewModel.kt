package no.hiof.bachelor.premacare.viewModels

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.withContext
import no.hiof.bachelor.premacare.model.FeedingRecord
import no.hiof.bachelor.premacare.model.Message
import no.hiof.bachelor.premacare.model.User
import no.hiof.bachelor.premacare.model.toMap
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State


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

    // ---------for feedingRecords----------------

    private var _amount = mutableStateOf(0)
    val amount = _amount

    private var _time = mutableStateOf("")
    val time = _time

    private var _pee = mutableStateOf(false)
    val pee = _pee

    private var _poo = mutableStateOf(false)
    val poo = _poo

    private var _feedingMethod = mutableStateOf("")
    val feedingMethod = _feedingMethod

    private var _comment = mutableStateOf("")
    val comment = _comment

    private var _weight = mutableStateOf(0)
    val weight = _weight

// for logg delen
    var feedingRecords by mutableStateOf<List<FeedingRecord>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set



    // -------- Message --------
    // private val _messages = MutableLiveData<List<Message>>()
    // val messages: LiveData<List<Message>> = _messages


    // Uten Password validation check
  // fun registerUser(onResult: (Boolean) -> Unit) {
  //     val emailValue = email.value
  //     val passwordValue = password.value
  //     val parentNameValue = parentName.value
  //     val phoneNumerValue = phoneNumer.value
  //     val childsNameValue = childsName.value
  //     val chilDateOfBirthValue = chilDateOfBirth.value

  //     if (emailValue.isNotBlank() && passwordValue.isNotBlank() && childsNameValue.isNotBlank()) {
  //         CoroutineScope(Dispatchers.IO).launch {
  //             try {
  //                 // Opprett bruker i Firebase Authentication
  //                 val authResult =
  //                     auth.createUserWithEmailAndPassword(emailValue, passwordValue).await()
  //                 val currentUser = authResult.user

  //                 if (currentUser != null) {
  //                     val userId = currentUser.uid
  //                     val userMap = hashMapOf(
  //                         "chilDateOfBirth" to chilDateOfBirthValue,
  //                         "childsName" to childsNameValue,
  //                         "parentName" to parentNameValue,
  //                         "phoneNumer" to phoneNumerValue,
  //                         "email" to emailValue
  //                     )

  //                     // Lagre brukerinfo i Firestore
  //                     firestore.collection("users").document(userId).set(userMap).await()

  //                     // Oppdater UI på hovedtråden
  //                     withContext(Dispatchers.Main) {
  //                         fetchChildsName() // Henter barnets navn etter registrering
  //                         onResult(true) // Registrering vellykket
  //                     }
  //                 } else {
  //                     withContext(Dispatchers.Main) { onResult(false) }
  //                 }
  //             } catch (e: Exception) {
  //                 withContext(Dispatchers.Main) { onResult(false) }
  //             }
  //         }
  //     } else {
  //         onResult(false) // Feil hvis noen felt er tomme
  //     }
  // }

    // Med Password validation check
    fun registerUser(onResult: (Boolean) -> Unit) {
        val emailValue = email.value
        val passwordValue = password.value
        val parentNameValue = parentName.value
        val phoneNumerValue = phoneNumer.value
        val childsNameValue = childsName.value
        val chilDateOfBirthValue = chilDateOfBirth.value

        //  Password validation check
        if (!isValidPassword(passwordValue)) {
            onResult(false)
            return
        }

        if (emailValue.isNotBlank() && passwordValue.isNotBlank() && childsNameValue.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Opprett bruker i Firebase Authentication
                    val authResult =
                        auth.createUserWithEmailAndPassword(emailValue, passwordValue).await()
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



    //Fetch the username from Firestore
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

    fun fetchParentName() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userSnapshot = firestore.collection("users").document(userId).get().await()
                    _parentName.value =
                        userSnapshot.getString("parentName") ?: "" // Fetch parent name
                } catch (e: Exception) {
                    println(e.message)
                }
            }
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

    // En ekstra passord sjekk siden firebase kun har "må være 6 characters", vi ønsker noe litt mer
    //ced å bruk store og små bokstaver, tall og spesialtegn (!, ?, @, # osv.)
    fun isValidPassword(password: String): Boolean {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { "!@#\$%^&*()_+[]{}|;:',.<>?/`~".contains(it) }
        return password.length >= 6 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar
    }


    //------------------ Feeding records ----------------------

    fun saveFeedingRecord(
        amount: Int,
        weight: Int,
        pee: Boolean,
        poo: Boolean,
        feedingMethod: String,
        comment: String
    ) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val feedingRecord = FeedingRecord(
                amount = amount,
                weight = weight,
                time = Timestamp.now(), // Bruker Firestore sin Timestamp
                date = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date()), // Dagens dato som String
                pee = pee,
                poo = poo,
                feedingMethod = feedingMethod,
                comment = comment
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firestore.collection("users")
                        .document(userId)
                        .collection("feedingRecords")
                        .add(feedingRecord)
                        .await()
                } catch (e: Exception) {
                    println("Error saving feeding record: ${e.message}")
                }
            }
        }
    }

    // Slette en feeding
    fun deleteFeedingRecord(record: FeedingRecord) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            firestore.collection("users")
                .document(userId)
                .collection("feedingRecords")
                .whereEqualTo("amount", record.amount)
                .whereEqualTo("weight", record.weight)
                .whereEqualTo("date", record.date)
                .whereEqualTo("pee", record.pee)
                .whereEqualTo("poo", record.poo)
                .whereEqualTo("comment", record.comment)
                .limit(1) // Veldig viktig – bare slett én!
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        querySnapshot.documents[0].reference.delete()
                    }
                }
                .addOnFailureListener {
                    println("Feil ved sletting: ${it.message}")
                }
        }
    }


    // Henter feedings
    fun fetchFeedingRecords() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            isLoading = true
            val userId = user.uid
            firestore.collection("users")
                .document(userId)
                .collection("feedingRecords")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        println("Error fetching records: ${exception.message}")
                        isLoading = false
                        return@addSnapshotListener
                    }

                    snapshot?.let {
                        feedingRecords = it.documents.mapNotNull { doc ->
                            doc.toObject(FeedingRecord::class.java)
                        }
                        isLoading = false

                    }
                }
        }
    }

    fun fetchFeedingRecordWeights() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            isLoading = true
            val userId = user.uid
            firestore.collection("users")
                .document(userId)
                .collection("feedingRecords")
                .orderBy("time", Query.Direction.ASCENDING)  // Viktig for vekthistorikk
                .get()
                .addOnSuccessListener { snapshot ->
                    val weights = snapshot.documents.mapNotNull { doc ->
                        val weight = doc.getDouble("weight") // eller getLong hvis lagret som Int
                        weight?.toFloat()
                    }
                    _weightHistory.value = weights
                    isLoading = false
                    Log.d("DEBUG", "Weights hentet: $weights")
                }
                .addOnFailureListener { e ->
                    println("Error fetching feeding record weights: ${e.message}")
                    isLoading = false
                }
        }
    }



    private val _currentIntake = MutableLiveData<Float>()
    val currentIntake: LiveData<Float> = _currentIntake

    fun getTotalAmountForToday() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val todayDate = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date()) // Henter dagens dato

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val snapshot = firestore.collection("users")
                        .document(userId)
                        .collection("feedingRecords")
                        .whereEqualTo("date", todayDate) // Filtrerer etter dagens dato
                        .get()
                        .await()

                    val totalAmount =
                        snapshot.documents.sumOf { it.getLong("amount")?.toInt() ?: 0 }
                    _currentIntake.postValue(totalAmount.toFloat())

                } catch (e: Exception) {
                    println("Error fetching total amount: ${e.message}")
                }
            }
        }
    }

    private val _lastWeight = MutableLiveData<Int>()
    val lastWeight: LiveData<Int> = _lastWeight

    fun getLastWeight() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val snapshot = firestore.collection("users")
                        .document(userId)
                        .collection("feedingRecords")
                        .orderBy(
                            "time",
                            Query.Direction.DESCENDING
                        ) // Match field name in Firestore
                        .limit(1)
                        .get()
                        .await()

                    val lastWeight = snapshot.documents.firstOrNull()?.getLong("weight")?.toInt()
                    _lastWeight.postValue(lastWeight ?: 0)

                } catch (e: Exception) {
                    println("Error fetching last weight: ${e.message}")
                }
            }
        }
    }



    private val _weightHistory = MutableLiveData<List<Float>>(emptyList())
    val weightHistory: LiveData<List<Float>> = _weightHistory

    //vekt historikk
    fun fetchWeightHistory() {
        val newData = feedingRecords
            .filter { it.weight > 0 }
            .sortedBy { it.time.toDate() }
            .map { it.weight.toFloat() }

        _weightHistory.value = newData
    }




    //------------------ Message ----------------------



    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _hasUnreadMessages = mutableStateOf(false)
    val hasUnreadMessages: State<Boolean> = _hasUnreadMessages



    //fun fetchMessagesRealtime() {
    //    val userId = auth.currentUser?.uid ?: return
    //    val messagesRef = firestore.collection("users")
    //        .document(userId)
    //        .collection("messages")
    //        .orderBy("timestamp")
//
    //    messagesRef.addSnapshotListener { snapshot, error ->
    //        if (error != null) {
    //            println("Error fetching messages: ${error.message}")
    //            return@addSnapshotListener
    //        }
//
    //        val messageList = snapshot?.toObjects(Message::class.java) ?: emptyList()
    //        _messages.value = messageList
//
    //        // Sjekk etter uleste meldinger
    //        val hasUnread = messageList.any { !it.isRead && it.senderid == "Sykepleier" }
    //        _hasUnreadMessages.value = hasUnread
    //    }
    //}

    // I FirebaseViewModel.kt

   //fun fetchMessagesRealtime() {
   //    val userId = auth.currentUser?.uid ?: return
   //    val messagesRef = firestore.collection("users")
   //        .document(userId)
   //        .collection("messages")
   //        .orderBy("timestamp")

   //    messagesRef.addSnapshotListener { snapshot, error ->
   //        if (error != null) {
   //            println("Error fetching messages: ${error.message}")
   //            return@addSnapshotListener
   //        }

   //        val messageList = snapshot?.toObjects(Message::class.java) ?: emptyList()
   //        _messages.value = messageList

   //        // Sjekk etter uleste meldinger
   //        val hasUnread = messageList.any { !it.isRead && it.senderid == "Sykepleier" }
   //        _hasUnreadMessages.value = hasUnread // <-- Den skal være her!
   //        Log.d("PremaCareApp", "Calculated hasUnreadMessages: $hasUnread. Current _hasUnreadMessages.value: ${_hasUnreadMessages.value}")

   //    }
   //}

    fun fetchMessagesRealtime() {
        val userId = auth.currentUser?.uid ?: return
        val messagesRef = firestore.collection("users")
            .document(userId)
            .collection("messages")
            .orderBy("timestamp")

        Log.d("PremaCareApp", "fetchMessagesRealtime called for userId: $userId") // Ny logg for å bekrefte kall

        messagesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Bruk Log.e for feil, ikke println
                Log.e("PremaCareApp", "Error fetching messages: ${error.message}")
                return@addSnapshotListener
            }

            // ***** VIKTIG: Sjekk snapshot før toObjects() *****
            if (snapshot == null) {
                Log.d("PremaCareApp", "Snapshot is null. No messages.")
                _messages.value = emptyList()
                _hasUnreadMessages.value = false
                return@addSnapshotListener
            }
            if (snapshot.isEmpty) {
                Log.d("PremaCareApp", "Snapshot is empty. No messages found in collection.")
                _messages.value = emptyList()
                _hasUnreadMessages.value = false
                return@addSnapshotListener
            }

            // ***** NY VIKTIG LOGG: Rådata fra Firestore *****
            Log.d("PremaCareApp", "Snapshot contains ${snapshot.documents.size} documents.")
            snapshot.documents.forEachIndexed { index, doc ->
                Log.d("PremaCareApp_DOC", "Doc $index ID: ${doc.id}, Data: ${doc.data}")
            }
            // *************************************************

            val messageList = snapshot.toObjects(Message::class.java) // Fjernet ?: emptyList() for å se om toObjects kaster feil, men Firebase SDK returnerer vanligvis en tom liste.
            _messages.value = messageList

            Log.d("PremaCareApp", "Deserialized ${messageList.size} messages from snapshot.") // Ny logg for å bekrefte deserialisering

            // Sjekk etter uleste meldinger
            val hasUnread = messageList.any { msg ->
                // ***** NY VIKTIG LOGG: Deserialisert Message-objekt *****

                Log.d("PremaCareApp_DEBUG", "Deserialized Message: senderid=${msg.senderid}, isRead=${msg.isRead}, message='${msg.message}'")

                val isUnreadFromNurse = !msg.isRead && msg.senderid == "Sykepleier"
                Log.d("PremaCareApp", "  Message: '${msg.message}', isRead: ${msg.isRead}, senderid: '${msg.senderid}'. Is unread from nurse: $isUnreadFromNurse")
                isUnreadFromNurse
            }
            _hasUnreadMessages.value = hasUnread
            Log.d("PremaCareApp", "Calculated hasUnreadMessages: $hasUnread. Current _hasUnreadMessages.value: ${_hasUnreadMessages.value}")
        }
    }

            fun setHasUnreadMessages(value: Boolean) {
            _hasUnreadMessages.value = value
        }

    fun sendMessage(messageText: String) {
        val userId = auth.currentUser?.uid ?: return
        val message = Message(senderid = userId, message = messageText)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestore.collection("users")
                    .document(userId)
                    .collection("messages")
                    .document(UUID.randomUUID().toString())
                    .set(message)
                    .await()

                fetchMessagesRealtime() // Oppdater listen etter sending
            } catch (e: Exception) {
                println("Error sending message: ${e.message}")
            }
        }
    }




    fun markMessagesAsRead() {
        val userId = auth.currentUser?.uid ?: return
        // Filtrer kun sykepleier-meldinger som er ulest.
        val unreadNurseMessages = _messages.value?.filter { !it.isRead && it.senderid == "Sykepleier" } ?: emptyList()

        Log.d("PremaCareApp", "markMessagesAsRead() called. Found ${unreadNurseMessages.size} unread nurse messages to mark.")

        if (unreadNurseMessages.isEmpty()) {
            Log.d("PremaCareApp", "No unread nurse messages found to mark as read.")
            // Hvis det ikke er noen uleste, sørg for at badgen er av.
            // Dette kan trigge lytteren og sette _hasUnreadMessages til false.
            _hasUnreadMessages.value = false // Sett til false med en gang hvis ingen å markere
            return
        }

        // Bruk CoroutineScope for å håndtere asynkrone oppdateringer sekvensielt
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val batch = firestore.batch() // Bruk batch for å oppdatere flere dokumenter mer effektivt

                for (msg in unreadNurseMessages) {
                    // VIKTIG: Hent dokument-ID fra meldingen, ikke søk på timestamp hvis mulig.
                    // Hvis du har en ID i Message-objektet ditt, bruk den!
                    // For nå antar jeg du har en id i Message-objektet ditt, la oss sjekke:
                    // Data class Message: val senderid: String = "", val message: String = "", val timestamp: Long = System.currentTimeMillis(), val isRead: Boolean = false
                    // Din Message-data class mangler en ID. Dette er et problem for pålitelig oppdatering.
                    // Det er mye bedre å oppdatere via document ID.
                    // Hvis du får IDen fra Firestore når du deserialiserer til Message, må du lagre den i Message-objektet.

                    // For nå, la oss fortsette med din 'whereEqualTo' tilnærming, men husk det er en svakhet.
                    // Den tryggeste måten er å hente dokument-IDen når du fetcher meldinger,
                    // og deretter bruke den til å oppdatere.
                    // Example: firestore.collection(...).document(msg.id).update(...)

                    // Siden du ikke har id i din Message data class:
                    val querySnapshot = firestore.collection("users")
                        .document(userId)
                        .collection("messages")
                        .whereEqualTo("timestamp", msg.timestamp)
                        .whereEqualTo("senderid", msg.senderid)
                        .limit(1) // Viktig for å unngå å markere flere om timestamp ikke er unik nok.
                        .get()
                        .await() // Vent på at spørringen fullføres

                    for (doc in querySnapshot.documents) {
                        batch.update(doc.reference, "isRead", true)
                        Log.d("PremaCareApp", "  Added update to batch for message: ${doc.id}")
                    }
                }
                batch.commit().await() // Utfør alle oppdateringene i en batch og vent på at de fullføres

                Log.d("PremaCareApp", "All mark-as-read updates completed in Firestore via batch.")

                // Etter at alle oppdateringer er committed og awaitet,
                // må vi gi Firestore litt tid til å synkronisere *før* lytteren trigges med de nye dataene.
                // Selv om await() er kraftig, er det fortsatt asynkrone nettverksoperasjoner.
                // En liten delay kan hjelpe her.
                // ENDA BEDRE: Firestore lytteren (addSnapshotListener) vil automatisk trigges
                // når endringene er reflektert i databasen. Du trenger ikke å tvinge en sjekk her.
                // Bare la den automatisk håndtere det.
                // Nøkkelen er at oppdateringene MÅ være fullført FØR lytteren får den nye tilstanden.

                // Den _hasUnreadMessages blir oppdatert av fetchMessagesRealtime() som har addSnapshotListener.
                // Når batch.commit().await() er ferdig, vil Firestore trigge lytteren med de nye dataene,
                // og da skal _hasUnreadMessages bli FALSE.
                // Hvis det fremdeles er et problem, kan vi vurdere en liten forsinkelse her, men det er mindre ideelt.

            } catch (e: Exception) {
                Log.e("PremaCareApp", "Error marking messages as read: ${e.message}")
            }
        }
    }
    private fun checkForUnreadMessages(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("messages")
            .whereEqualTo("isRead", false)
            .whereEqualTo("senderid", "Sykepleier") // Kun sykepleier-meldinger
            .get()
            .addOnSuccessListener { docs ->
                // Denne _hasUnreadMessages.value = !docs.isEmpty
                // håndteres allerede av fetchMessagesRealtime() sin addSnapshotListener.
                // Så du trenger ikke å oppdatere _hasUnreadMessages direkte her heller.
                // Lytteren vil fange opp endringen når meldingene er lest.
                // Men funksjonen kan beholdes om du vil trigge en spesifikk sjekk,
                // men da må den oppdatere _hasUnreadMessages.value.
                _hasUnreadMessages.value = !docs.isEmpty
            }
            .addOnFailureListener {
                println("Failed to check for unread messages: ${it.message}")
            }
    }





    // legge til en medforelder
    fun registerCoParentWithRelogin(
        coEmail: String,
        coPassword: String,
        parent1Password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val parent1Uid = auth.currentUser?.uid
        val parent1Email = auth.currentUser?.email

        if (parent1Uid == null || parent1Email == null) {
            onFailure(Exception("Forelder 1 er ikke logget inn"))
            return
        }

        // Steg 1: Opprett medforelder
        auth.createUserWithEmailAndPassword(coEmail, coPassword)
            .addOnSuccessListener { result ->
                val coParentUid = result.user?.uid
                if (coParentUid == null) {
                    onFailure(Exception("Kunne ikke hente medforelder UID"))
                    return@addOnSuccessListener
                }

                // Steg 2: Opprett dokument for medforelder med coParentOf
                val coParentData = mapOf("coParentOf" to parent1Uid)
                db.collection("users").document(coParentUid).set(coParentData)
                    .addOnSuccessListener {
                        // Steg 3: Logg inn igjen som forelder 1
                        auth.signInWithEmailAndPassword(parent1Email, parent1Password)
                            .addOnSuccessListener {
                                // Steg 4: Oppdater forelder 1 sitt dokument
                                db.collection("users").document(parent1Uid)
                                    .update("coParents", FieldValue.arrayUnion(coParentUid))
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { onFailure(it) }
                            }
                            .addOnFailureListener { onFailure(it) }
                    }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }
}


suspend fun getEffectiveUserIdSuspend(): String? {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    return if (currentUser != null) {
        try {
            // Hent medforelderens dokument
            val document = db.collection("users").document(currentUser.uid).get().await()

            if (document.exists()) {
                // Sjekk om medforelderen har en coParentOf-verdi
                val coParentOf = document.getString("coParentOf")
                if (coParentOf != null) {
                    // Hent Forelder 1 sin UID fra coParentOf og returner den
                    return coParentOf
                } else {
                    // Hvis coParentOf ikke er satt, returner medforelderens egen UID
                    return currentUser.uid
                }
            } else {
                Log.e("ERROR", "Brukerdokumentet finnes ikke.")
                return null
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Feil ved henting av effektiv bruker-ID", e)
            return null
        }
    } else {
        Log.e("ERROR", "Ingen bruker er logget inn.")
        return null
    }
}















