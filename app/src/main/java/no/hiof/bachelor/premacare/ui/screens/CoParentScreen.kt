import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun CoParentScreen(
    forelder1Id: String,
    toDash: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val parent1Password = remember { mutableStateOf("") }

    val context = LocalContext.current
    val loading = remember { mutableStateOf(false) }
    val viewModel: FirebaseViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Legg til medforelder", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("E-post til medforelder") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Passord til medforelder") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = parent1Password.value,
            onValueChange = { parent1Password.value = it },
            label = { Text("Ditt passord (Forelder 1)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                loading.value = true
                viewModel.registerCoParentWithRelogin(
                    coEmail = email.value,
                    coPassword = password.value,
                    parent1Password = parent1Password.value,
                    onSuccess = {
                        loading.value = false
                        Toast.makeText(context, "Medforelder er registrert!", Toast.LENGTH_LONG).show()
                        toDash()
                    },
                    onFailure = { exception ->
                        loading.value = false
                        Toast.makeText(context, "Feil: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading.value
        ) {
            Text(if (loading.value) "Legger til..." else "Legg til medforelder")
        }
    }
}



