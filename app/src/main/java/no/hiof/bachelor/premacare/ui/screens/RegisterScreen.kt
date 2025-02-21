package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

        OutlinedTextField(
            value = firebaseViewModel.childsName.value,
            onValueChange = { newValue -> firebaseViewModel.childsName.value = newValue },
            label = { Text("Name of Child") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0x80FFFFFF),
                unfocusedContainerColor = Color.White
            )
        )

        // ----------Register------------------------
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

        SpaceEm(40.dp)

        Button(onClick = { toLogin();
            firebaseViewModel.registerUser()},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4ABAB3),
                contentColor = Color.White
            )
        ) {
            Text(text = "Sign up")
        }
    }
}

