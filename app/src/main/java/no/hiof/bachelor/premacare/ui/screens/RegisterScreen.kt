package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        Text( text = "REGISTER",
            color = Color(0xFF333333),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        SpaceEm(10.dp)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)), // Slightly rounded shadow
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD) // Light blue
            ),
            shape = RoundedCornerShape(5.dp), // Rounded corners
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp) // Add padding inside the Card
            ) {
                Text(
                    text = "Information About Parent/Legal Representative",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.parentName.value,
                    onValueChange = { newValue -> firebaseViewModel.parentName.value = newValue },
                    label = { Text("Name of Parent") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firebaseViewModel.phoneNumer.value,
                    onValueChange = { newValue -> firebaseViewModel.phoneNumer.value = newValue },
                    label = { Text("Phone Number") },
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
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)), // Slightly rounded shadow
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC) // Light pink
            ),
            shape = RoundedCornerShape(5.dp), // Rounded corners
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp) // Add padding inside the Card
            ) {
                Text(
                    text = "Information About Child",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.childsName.value,
                    onValueChange = { newValue -> firebaseViewModel.childsName.value = newValue },
                    label = { Text("Name of Child") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x80FFFFFF),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firebaseViewModel.chilDateOfBirth.value,
                    onValueChange = { newValue -> firebaseViewModel.chilDateOfBirth.value = newValue },
                    label = { Text("Child`s Date Of Birth") },
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
                .shadow(8.dp, shape = RoundedCornerShape(5.dp)), // Slightly rounded shadow
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9) // Light green
            ),
            shape = RoundedCornerShape(5.dp), // Rounded corners
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp) // Add padding inside the Card
            ) {
                Text(
                    text = "Credentials",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = firebaseViewModel.email.value,
                    onValueChange = { newValue -> firebaseViewModel.email.value = newValue },
                    label = { Text("Email") },
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
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        SpaceEm(40.dp)

        Button(onClick = { toLogin();
            firebaseViewModel.registerUser()},
            colors = ButtonDefaults.buttonColors(
                //0xFF4ABAB3 <-- denne vi har brukt men kke bra nokk for fargebilde
                containerColor = Color(0xFF1565C0), //<-- Royal Blue vi tester ut denne!
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

                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                text = "Sign up")
        }
    }
}

