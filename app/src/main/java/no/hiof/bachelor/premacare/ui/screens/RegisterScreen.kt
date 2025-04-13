package no.hiof.bachelor.premacare.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel


@Composable
fun RegisterScreen(toLogin: ()-> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember {
        PasswordVisualTransformation()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Registrere",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Oppgi nødvendige opplysninger om barnet og foresatt for registrering.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        SpaceEm(10.dp)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
            colors = CardDefaults.cardColors(
                //0xFFFFF3E0 Light orange
                //0xFFE3F2FD Light blue
                containerColor = Color(0xFFE3F2FD) // Light blue
            ),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Informasjon Om Foreldre/Foresatte",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.parentName.value,
                    onValueChange = { newValue -> firebaseViewModel.parentName.value = newValue },
                    label = { Text("Forelders Navn") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firebaseViewModel.phoneNumer.value,
                    onValueChange = { newValue -> firebaseViewModel.phoneNumer.value = newValue },
                    label = { Text("Telefon Nummer") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        //------Child-------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
            colors = CardDefaults.cardColors(
                //0xFFEDE7F6 Light purple
                //0xFFFCE4EC Light pink
                containerColor = Color(0xFFFCE4EC) // Light pink
            ),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Informasjon Om Barnet",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.childsName.value,
                    onValueChange = { newValue -> firebaseViewModel.childsName.value = newValue },
                    label = { Text("Barnets Navn") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firebaseViewModel.chilDateOfBirth.value,
                    onValueChange = { newValue ->
                        firebaseViewModel.chilDateOfBirth.value = newValue
                    },
                    label = { Text("Barnet Er Født") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        // ----------Register------------------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
            colors = CardDefaults.cardColors(
                //0xFFFFF9C4 Light yellow
                //0xFFE8F5E9 Light green
                containerColor = Color(0xFFE8F5E9) // Light green
            ),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Påloggingsinformasjon",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.email.value,
                    onValueChange = { newValue -> firebaseViewModel.email.value = newValue },
                    label = { Text("E-post") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = firebaseViewModel.password.value,
                    onValueChange = { newValue -> firebaseViewModel.password.value = newValue },
                    label = { Text("Passord") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        passwordVisualTransformation
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        Icon(
                            if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier.clickable { showPassword = !showPassword }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        SpaceEm(30.dp)

        val isLoading = remember { mutableStateOf(false) }
        var showToastMessage by remember { mutableStateOf<String?>(null) }
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                if (firebaseViewModel.isValidRegistration()) { // Legg til en valideringsmetode i ViewModel
                    isLoading.value = true
                    coroutineScope.launch {
                        firebaseViewModel.registerUser { success ->
                            isLoading.value = false
                            showToastMessage = if (success as Boolean) {
                                toLogin() // Naviger til login-skjermen
                                "Registrering fullført!"
                            } else {
                                "Noe gikk galt. Prøv igjen."
                            }
                        }
                    }
                } else {
                    showToastMessage = "Vennligst fyll inn alle felt riktig."
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0), //<-- Royal Blue
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            ),
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading.value
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    text = "Registrer"
                )
            }
        }

// Vis en toast hvis det er en melding
        showToastMessage?.let { message ->
            Toast.makeText(
                LocalContext.current, message, Toast.LENGTH_LONG
            ).show()
            showToastMessage = null
        }

    }

}

