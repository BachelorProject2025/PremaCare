package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun MessageScreen() {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val messages by firebaseViewModel.messages.observeAsState(emptyList())
    var message by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(Unit) {
        firebaseViewModel.fetchMessages() // Hent meldinger ved oppstart
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { msg ->
                MessageBubble(text = msg.message)
                Spacer(modifier = Modifier.height(4.dp))
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

            //Spacer(modifier = Modifier.width(8.dp))


        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                if (message.text.isNotBlank()) {
                    firebaseViewModel.sendMessage(message.text)
                    message = TextFieldValue()
                }
            }
        ) {
            Text("Send")
        }

    }
}

@Composable
fun MessageBubble(text: String) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            //pale purple 0xFFE1BEE7
            //light coral 0xFFFFD4D1
            // light blue 0xFFBBDEFB
            .background(Color(0xFFBBDEFB), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {

            Text(text, color = Color.Black)
    }
}
