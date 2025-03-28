package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageScreen() {
    var message by remember { mutableStateOf(TextFieldValue()) }
    val messages = remember { mutableStateListOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {


        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            messages.forEach { msg ->
                MessageBubble(text = msg)
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

            Spacer(modifier = Modifier.width(8.dp))


        }

        Button(modifier = Modifier
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            onClick = {
                if (message.text.isNotBlank()) {
                    messages.add(message.text)
                    message = TextFieldValue()
                }
            },
            shape = RoundedCornerShape(50)
        ) {
            Text("Send")
        }
    }
}

@Composable
fun MessageBubble(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBBDEFB), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text, color = Color.Black)
    }
}
