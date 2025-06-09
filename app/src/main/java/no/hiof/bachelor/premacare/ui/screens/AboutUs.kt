package no.hiof.bachelor.premacare.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.text.Layout
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext



@Composable
fun AboutUs() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F3F6))
            .padding(16.dp)) {
        Text(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            text = "Alt om oss"
        )
        HorizontalLine()

        SpaceEm(15.dp)

        Text("PremaCare skal forenkle registreringen av slik\n" +
                "informasjon og bidra til en mer strukturert oversikt over barnets utvikling. Dette vil lette\n" +
                "hverdagen for foreldrene og gi helsepersonell\n" +
                "enklere tilgang til presise og oppdaterte data")


        SpaceEm(30.dp)

        ContactForm()


    }

}



@Composable
private fun ContactForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Kontakt oss", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Navn") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )



        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-post") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )


        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Melding") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("premacareapp@gmail.com")) // du kan også bruke `email` hvis du vil sende til brukerens adresse
                    putExtra(Intent.EXTRA_SUBJECT, "Ny melding fra appen")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Navn: $name\nE-post: $email\nMelding: $message"
                    )
                }
                try {
                    context.startActivity(Intent.createChooser(intent, "Send e-post"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Ingen e-postklient funnet.", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send")
        }
    }
}



