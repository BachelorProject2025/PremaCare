package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun MessageScreen() {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val messages by firebaseViewModel.messages.observeAsState(emptyList())
    var message by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Unit) {
        firebaseViewModel.fetchMessagesRealtime() // Hent meldinger ved oppstart
        firebaseViewModel.fetchParentName()
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            firebaseViewModel.markMessagesAsRead()

            // Sett eksplisitt false lokalt etter kort delay
            //delay(500) // gir Firestore litt tid
            //firebaseViewModel.setHasUnreadMessages(false)
        }
    }




    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Samtale med sykepleier",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        SpaceEm(10.dp)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { msg ->
                MessageBubble(
                    text = msg.message,
                    senderId = msg.senderid,
                    parentName = firebaseViewModel.parentName.value
                )
                Spacer(modifier = Modifier.height(7.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Skriv en melding...") }
            )
        }

        OutlinedButton(
            onClick = {
                if (message.text.isNotBlank()) {
                    firebaseViewModel.sendMessage(message.text)
                    message = TextFieldValue()
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF4CAF50) // Fargen på tekst og ikon
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4CAF50))
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send ikon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send")
        }
    }
}

@Composable
fun MessageBubble(text: String, senderId: String, parentName: String?) {
    // Set background color based on senderId
    val backgroundColor = if (senderId == "Sykepleier") {
        Color(0xFFEEEEEE) // Light grey for "Sykepleier"
    } else {
        Color(0xFFBBDEFB) // Light blue for andre(Foreldre)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column {

            Text(
                text = if (senderId == "Sykepleier") "Sykepleier" else parentName ?: "Unknown",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main message text
            Text(text, color = Color.Black)
        }
    }
}