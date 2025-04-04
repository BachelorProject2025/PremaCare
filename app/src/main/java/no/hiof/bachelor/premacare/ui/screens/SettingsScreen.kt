package no.hiof.bachelor.premacare.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun SettingsScreen(
    navigateToChangePassword: () -> Unit,
    home: () -> Unit

) {
    var darkModeEnabled by remember { mutableStateOf(false) }
    val firebaseViewModel: FirebaseViewModel = viewModel()

    Text(
        text = "Innstillinger",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 130.dp)
    )




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SpaceEm(65.dp)

        // Logge ut
        SettingItem(
            title = "Logg ut ",
            icon = Icons.Default.Logout,
            action = {

                IconButton(onClick = {
                    firebaseViewModel.logOut()
                    home()
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go")
                }
            }
        )

        
        // Dark Mode Toggle
        SettingItem(
            title = "Dark Mode",
            icon = Icons.Default.DarkMode,
            action = {
                Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it })
            }
        )

        // Change Password
        SettingItem(
            title = "Bytt Passord",
            icon = Icons.Default.Lock,
            action = {
                IconButton(onClick = { navigateToChangePassword() }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go")
                }
            }
        )

        // Add Co-Parent
        SettingItem(
            title = "Legg til med forelder",
            icon = Icons.Default.Group,
            action = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go")
                }
            }
        )
    }
}


@Composable
fun SettingItem(title: String, icon: ImageVector, action: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 18.sp, modifier = Modifier.weight(1f))
        action()
    }
}
