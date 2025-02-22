package no.hiof.bachelor.premacare.ui.screens



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import no.hiof.bachelor.premacare.R


@Composable
fun LoginScreen(takeMeDash: () -> Unit, aboutUs: () -> Unit, auth: FirebaseAuth, newUser: () -> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF79ECE0),  // cyan
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
            Text( text = "LOGIN",
                color = Color(0xFF333333),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                )

            SpaceEm(10.dp)

            // ----------Login------------------------
            OutlinedTextField(
                value = firebaseViewModel.email.value,
                onValueChange = { newValue -> firebaseViewModel.email.value = newValue },
                label = { Text("Email") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x80FFFFFF),
                    unfocusedContainerColor = Color.White
                )
            )

            SpaceEm(10.dp)

            OutlinedTextField(
                value = firebaseViewModel.password.value,
                onValueChange = { newValue -> firebaseViewModel.password.value = newValue },
                label = { Text("Password") },
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
                }
            )

            SpaceEm(10.dp)

            ButtonWithToast(
                firebaseViewModel,
                { takeMeDash() },
                "Login",
                auth = auth,
                "Incorrect email or password. Please try again."
            )

            SpaceEm(5.dp)

            newUserText(newUser, "Not yet a User?")

            aboutUsText(aboutUs,"  V1 ©Designed & Develop By Chanipa Dencharoen," +
                    "" +
                    " Heljar Andreas Nilsen Korbi and Daniel John Russell" )


        }
    }
}


@Composable
fun newUserText(newUser: () -> Unit, text: String) {
    Text(
        text = text,
        color = Color(0xFF1A237E),
        fontSize = 20.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { newUser() }
    )
}

@Composable
fun aboutUsText(aboutUs: () -> Unit, text: String) {
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

    Button(
        onClick = {
            firebaseViewModel.loginUser()
            takeMeDash()

            if (currentUser == null) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4ABAB3),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = buttonText.uppercase(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}



@Composable
fun DrawImg(painter: Painter, contentDescription: String, modifier: Modifier = Modifier) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
    )
}




