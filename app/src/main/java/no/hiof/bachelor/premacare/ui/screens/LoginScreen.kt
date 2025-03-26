package no.hiof.bachelor.premacare.ui.screens



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import no.hiof.bachelor.premacare.R


@Composable
fun LoginScreen(takeMeDash: () -> Unit, aboutUs: () -> Unit, auth: FirebaseAuth, newUser: () -> Unit, reset: () -> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    val gradient = Brush.linearGradient(
        colors = listOf(
            //0xFFA040C0  livlig lilla
            //0xFF50D4F2  cyan/blå
            // 0xFFE8F5E9 lys grønnaktig farge


            Color( 0xFF50D4F2),  // cyan
            Color.White
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {

            DrawImg(
                painter = painterResource(R.drawable.premacare),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )

            SpaceEm(10.dp)
            // Not sure about this one ....
            Text(
                text = "Logg inn",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            SpaceEm(10.dp)

            // ----------Login------------------------
            OutlinedTextField(
                value = firebaseViewModel.email.value,
                onValueChange = { newValue -> firebaseViewModel.email.value = newValue },
                label = { Text("E-Post") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x80FFFFFF),
                    unfocusedContainerColor = Color.White,

                ),
                modifier = Modifier.fillMaxWidth()

            )

            SpaceEm(10.dp)

            var showPassword by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = firebaseViewModel.password.value,
                onValueChange = { newValue -> firebaseViewModel.password.value = newValue },
                label = { Text("Passord") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x80FFFFFF),
                    unfocusedContainerColor = Color.White
                ),
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),  // This is fine now
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )


            SpaceEm(15.dp)

            ButtonWithToast(
                firebaseViewModel,
                { takeMeDash() },
                "Logg inn",
                auth = auth,
                "Obs, Noe gikk galt. Vennligst prøv igjen."
            )

            SpaceEm(10.dp)


            AuthFooter(
               newUser, reset
            )

            AboutUsText(aboutUs,"  V1 Om Oss" )


        }
    }
}

@Composable
fun AuthFooter(newUser: () -> Unit, reset: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        newUserText(newUser, "Ny bruker?")
        forgetPasswordText(reset, "Glemt Passord?")
    }
}



@Composable
fun newUserText(newUser: () -> Unit, text: String) {
    Text(
        text = text,
        color = Color(0xFF1A237E),
        fontSize = 17.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { newUser() }
    )
}

@Composable
fun forgetPasswordText(reset: () -> Unit, text: String) {
    Text(
        text = text,
        color = Color(0xFF1A237E),
        fontSize = 17.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { reset() }
    )
}

@Composable
fun AboutUsText(aboutUs: () -> Unit, text: String) {
    Text(
        text = text,
        color = Color.Gray,
        fontSize = 12.sp,
        textDecoration = TextDecoration.Underline,
        modifier =
        Modifier
            .clickable { aboutUs() }
            .padding(50.dp)
    )
}


@Composable
fun SpaceEm(high: Dp) {
    Spacer(modifier = Modifier
        .height(height = high))
}

@Composable
fun ButtonWithToast(
    firebaseViewModel: FirebaseViewModel,
    takeMeDash: () -> Unit,
    buttonText: String,
    auth: FirebaseAuth,
    toastMessage: String
) {
    val currentUser = auth.currentUser
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) } // State for loading

    Button(
        onClick = {
            // Set loading to true when login is initiated
            loading = true

            // Call the login function
            firebaseViewModel.loginUser { success ->
                loading = false  // Stop loading once login is finished

                if (success) {
                    // If login is successful, navigate to the dashboard
                    takeMeDash()
                } else {
                    // If login failed, show the error message
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                }
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
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (loading) {
            // Show circular progress bar when loading
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = buttonText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}




@Composable
fun DrawImg(painter: Painter, contentDescription: String, modifier: Modifier = Modifier) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(380.dp)
    )
}




