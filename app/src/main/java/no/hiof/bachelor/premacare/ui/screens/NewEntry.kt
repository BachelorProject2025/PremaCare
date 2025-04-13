package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun NewEntry(ToDash: () -> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Fôring & helse",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 95.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sørg for at all informasjon er korrekt før du lagrer.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(46.dp))

        // Slider for Amount
        Text(
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            text = "Mengde melk: ${firebaseViewModel.amount.value} ml"
        )

        Slider(
            value = firebaseViewModel.amount.value.toFloat(),
            onValueChange = { firebaseViewModel.amount.value = it.toInt() },
            valueRange = 0f..170f
        )

        // Hent siste vekt fra Firebase ViewModel
        LaunchedEffect(Unit) {
            firebaseViewModel.getLastWeight()
        }

        val lastWeight by firebaseViewModel.lastWeight.observeAsState(0) // Standardverdi 0
        var weightInput by remember { mutableStateOf("") }

// Når lastWeight endres, oppdater både input-feltet OG ViewModel-verdien
        LaunchedEffect(lastWeight) {
            if (lastWeight > 0 && weightInput.isEmpty()) {
                weightInput = lastWeight.toString()
                firebaseViewModel.weight.value = lastWeight // <- VIKTIG! Sett vekten her
            }
        }

        Text(
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            text = "Vekt av barn: $lastWeight gram"
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = weightInput,
            onValueChange = { input ->
                weightInput = input

                input.toIntOrNull()?.let {
                    if (it in 550..5000) {
                        firebaseViewModel.weight.value = it
                    }
                }
            },
            placeholder = {
                Text(
                    text = if (lastWeight > 0) "$lastWeight" else "Skriv inn vekt i gram"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Urinasjon")
            RadioButton(
                selected = firebaseViewModel.pee.value,
                onClick = { firebaseViewModel.pee.value = true }
            )

            Text("Avføring")
            RadioButton(
                selected = firebaseViewModel.poo.value,
                onClick = { firebaseViewModel.poo.value = true }
            )
        }

        HorizontalLine()
        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = { firebaseViewModel.feedingMethod.value = "Bryst" },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (firebaseViewModel.feedingMethod.value == "Bryst") Color.White else Color(0xFF1565C0),
                    contentColor = if (firebaseViewModel.feedingMethod.value == "Bryst") Color(0xFF1565C0) else Color.White
                )
            ) {
                Text("Bryst")
            }

            TextButton(
                onClick = { firebaseViewModel.feedingMethod.value = "Sonde" },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (firebaseViewModel.feedingMethod.value == "Sonde") Color.White else Color(0xFF1565C0),
                    contentColor = if (firebaseViewModel.feedingMethod.value == "Sonde") Color(0xFF1565C0) else Color.White
                )
            ) {
                Text("Sonde")
            }

            TextButton(
                onClick = { firebaseViewModel.feedingMethod.value = "Flaske" },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (firebaseViewModel.feedingMethod.value == "Flaske") Color.White else Color(0xFF1565C0),
                    contentColor = if (firebaseViewModel.feedingMethod.value == "Flaske") Color(0xFF1565C0) else Color.White
                )
            ) {
                Text("Flaske")
            }
        }

        HorizontalLine()
        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = firebaseViewModel.comment.value,
            onValueChange = { firebaseViewModel.comment.value = it },
            label = { Text("Kommentar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Knappen din er BEVART nøyaktig
        Button(
            onClick = {
                firebaseViewModel.saveFeedingRecord(
                    amount = firebaseViewModel.amount.value,
                    weight = firebaseViewModel.weight.value,
                    pee = firebaseViewModel.pee.value,
                    poo = firebaseViewModel.poo.value,
                    feedingMethod = firebaseViewModel.feedingMethod.value,
                    comment = firebaseViewModel.comment.value
                )
                ToDash()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text("Lagre")
        }
    }
}

