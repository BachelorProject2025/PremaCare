package no.hiof.bachelor.premacare.ui.screens



import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import kotlinx.coroutines.delay
import no.hiof.bachelor.premacare.R
import no.hiof.bachelor.premacare.R.drawable.water

@Composable
fun LoginScreen(
    takeMeDash: () -> Unit,
    aboutUs: () -> Unit,
    auth: FirebaseAuth,
    newUser: () -> Unit,
    reset: () -> Unit
) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var showPassword by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val boxHeight = if (screenHeight < 700.dp) {
        screenHeight * 0.80f
    } else {
        screenHeight * 0.75f
    }

    Box(modifier = Modifier.fillMaxSize()) {

      //  Image(
      //      painter = painterResource(water),
      //      contentDescription = "Top background",
      //      contentScale = ContentScale.Crop,
      //      modifier = Modifier
      //          .fillMaxWidth()
      //          .height(280.dp)
      //          .align(Alignment.TopCenter)
      //  )

        ResponsiveTopImage(R.drawable.water)

        // White box starting from bottom and going up
        Box(

            modifier = Modifier
                .fillMaxWidth()
                //.height(700.dp)
                //.heightIn(min = 400.dp, max = screenHeight * 0.75f)
                .height(boxHeight)

                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 52.dp, topEnd = 52.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top //  viktig
            ) {
                val spacerHeight = if (screenHeight < 700.dp) 4.dp else 16.dp

                Spacer(modifier = Modifier.height(spacerHeight)) // Dynamisk

                val logoSize = if (screenHeight < 700.dp) 100 else 150

                DrawImg(
                    painter = painterResource(R.drawable.premacare),
                    contentDescription = "Logo",
                    modifier = Modifier,
                    size = logoSize
                )

                val spacerHeightText = if (screenHeight < 700.dp) 1.dp else 8.dp
                Spacer(modifier = Modifier.height(spacerHeightText))

                Text(
                    text = "Logg inn",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = firebaseViewModel.email.value,
                    onValueChange = { firebaseViewModel.email.value = it },
                    label = { Text("E-post") },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = firebaseViewModel.password.value,
                    onValueChange = { firebaseViewModel.password.value = it },
                    label = { Text("Passord") },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                ButtonWithToast(
                    firebaseViewModel = firebaseViewModel,
                    takeMeDash = takeMeDash,
                    buttonText = "Logg inn",
                    auth = auth,
                    toastMessage = "Obs, noe gikk galt. Vennligst prøv igjen."
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthFooter(newUser = newUser, reset = reset)

                AboutUsText(aboutUs = aboutUs, text = "  V1 Kontakt Oss")
            }
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
        fontSize = 13.sp,
        textDecoration = TextDecoration.Underline,
        modifier =
        Modifier
            .clickable { aboutUs() }
            .padding(30.dp)
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
            containerColor = colorResource(R.color.royal_blue),
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
fun DrawImg(painter: Painter, contentDescription: String, modifier: Modifier = Modifier, size: Int) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size.dp)

    )
}

// Tester ut litt responsvie design
@Composable
fun ResponsiveTopImage(water: Int) {
    BoxWithConstraints {
        val imageHeight = if (maxHeight < 600.dp) {
            maxHeight * 0.15f // Smaller height for small screens
        } else {
            maxHeight * 0.3f // Default height for normal/bigger screens
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(water),
                contentDescription = "Top background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}




