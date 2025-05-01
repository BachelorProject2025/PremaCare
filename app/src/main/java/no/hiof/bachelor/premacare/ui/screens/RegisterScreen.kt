package no.hiof.bachelor.premacare.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel



@Composable
fun RegisterScreen(toLogin: () -> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember {
        PasswordVisualTransformation()
    }

    val isLoading = remember { mutableStateOf(false) }
    var showToastMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
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
        }

        item {
            // Forelder-info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasjon Om Foreldre/Foresatte",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = firebaseViewModel.parentName.value,
                        onValueChange = { firebaseViewModel.parentName.value = it },
                        label = { Text("Forelders Navn") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x80FFFFFF),
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                   // OutlinedTextField(
                   //     value = firebaseViewModel.phoneNumer.value,
                   //     onValueChange = { firebaseViewModel.phoneNumer.value = it },
                   //     label = { Text("Telefon Nummer") },
                   //     shape = RoundedCornerShape(16.dp),
                   //     colors = OutlinedTextFieldDefaults.colors(
                   //         focusedContainerColor = Color(0x80FFFFFF),
                   //         unfocusedContainerColor = Color.White
                   //     ),
                   //     modifier = Modifier.fillMaxWidth()
                   // )

                    //val rawPhoneNumber = firebaseViewModel.phoneNumer.value.replace(" ", "")
                     PhoneNumberField(firebaseViewModel)
                }
            }
        }

        item {
            // Barn-info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasjon Om Barnet",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = firebaseViewModel.childsName.value,
                        onValueChange = { firebaseViewModel.childsName.value = it },
                        label = { Text("Barnets Navn") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x80FFFFFF),
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))  

                   //OutlinedTextField(
                   //    value = firebaseViewModel.chilDateOfBirth.value,
                   //    onValueChange = { newValue ->
                   //        firebaseViewModel.chilDateOfBirth.value = newValue
                   //    },
                   //    label = { Text("Barnet Er Født") },
                   //    shape = RoundedCornerShape(16.dp),
                   //    colors = OutlinedTextFieldDefaults.colors(
                   //        focusedContainerColor = Color(0x80FFFFFF),
                   //        unfocusedContainerColor = Color.White
                   //    ),
                   //    modifier = Modifier.fillMaxWidth()
                    DateOfBirthPicker(firebaseViewModel = firebaseViewModel)

                    //)
                }
            }
        }

        item {
            // Pålogging-info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(5.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Påloggingsinformasjon",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = firebaseViewModel.email.value,
                        onValueChange = { firebaseViewModel.email.value = it },
                        label = { Text("E-post") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x80FFFFFF),
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                   // OutlinedTextField(
                   //     value = firebaseViewModel.password.value,
                   //     onValueChange = { firebaseViewModel.password.value = it },
                   //     label = { Text("Passord") },
                   //     shape = RoundedCornerShape(16.dp),
                   //     colors = OutlinedTextFieldDefaults.colors(
                   //         focusedContainerColor = Color(0x80FFFFFF),
                   //         unfocusedContainerColor = Color.White
                   //     ),
                   //     visualTransformation = if (showPassword) VisualTransformation.None else passwordVisualTransformation,
                   //     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                   //     trailingIcon = {
                   //         Icon(
                   //             if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                   //             contentDescription = "Toggle password visibility",
                   //             modifier = Modifier.clickable { showPassword = !showPassword }
                   //         )
                   //     },
                   //     modifier = Modifier.fillMaxWidth()
                   // )

                    Column {
                        val password = firebaseViewModel.password.value
                        val isValid = firebaseViewModel.isValidPassword(password)
                        OutlinedTextField(
                            value = firebaseViewModel.password.value,
                            onValueChange = { firebaseViewModel.password.value = it },
                            label = { Text("Passord") },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x80FFFFFF),
                                unfocusedContainerColor = Color.White
                            ),
                            visualTransformation = if (showPassword) VisualTransformation.None else passwordVisualTransformation,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                Icon(
                                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    modifier = Modifier.clickable { showPassword = !showPassword }
                                )
                            },
                            isError = firebaseViewModel.password.value.isNotEmpty() && !isValid,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Bruk store og små bokstaver, tall og spesialtegn (!, ?, @, # osv.)",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (password.isNotEmpty() && !isValid) Color.Red else Color.Gray,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (firebaseViewModel.isValidRegistration()) {
                        isLoading.value = true
                        coroutineScope.launch {
                            firebaseViewModel.registerUser { success ->
                                isLoading.value = false
                                showToastMessage = if (success as Boolean) {
                                    toLogin()
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
                    containerColor = Color(0xFF1565C0),
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
@Composable
fun DateOfBirthPicker(firebaseViewModel: FirebaseViewModel) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Vis dialog når trykket
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val dateString = "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
                firebaseViewModel.chilDateOfBirth.value = dateString
            },
            year, month, day
        )
    }

    // Vis valgt dato, eller standardtekst
    val displayText = if (firebaseViewModel.chilDateOfBirth.value.isNotEmpty())
        firebaseViewModel.chilDateOfBirth.value
    else
        "Trykk for å velge fødselsdato"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x80FFFFFF))
            .clickable { datePickerDialog.show() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Velg dato",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PhoneNumberField(firebaseViewModel: FirebaseViewModel) {
    val rawNumber = firebaseViewModel.phoneNumer.value.filter { it.isDigit() }.take(8)

    OutlinedTextField(
        value = rawNumber,
        onValueChange = { input ->
            val digits = input.filter { it.isDigit() }.take(8)
            firebaseViewModel.phoneNumer.value = digits
        },
        label = { Text("Telefon Nummer") },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0x80FFFFFF),
            unfocusedContainerColor = Color.White
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PhoneNumberTransformation()
    )
}

class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(8).filter { it.isDigit() }
        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 2 || i == 4) append(' ')
            }
        }

        // OffsetMapping som fikser skriverposisjonen (caret) når mellomrom legges til
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 5 -> offset + 1
                    offset <= 8 -> offset + 2
                    else -> formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset - 1
                    offset <= 9 -> offset - 2
                    else -> text.text.length
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}












