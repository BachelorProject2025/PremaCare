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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun NewEntry(ToDash: () -> Unit) {
    // Heljar sin ....:P
    // I appens floating action button, er det lagt til denne screenen da det er hoved funksjonen til applikasjoenen.
    // for å følge gidelines til matirial design skal dette være slik.
    val firebaseViewModel: FirebaseViewModel = viewModel()


    Column(
        modifier = Modifier.run {
            padding(16.dp)
                .fillMaxSize()
        }
    ) {

        Text(
            text = "Legg til ny fôring",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 75.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fyll ut detaljene for fôringen din. Sørg for at all informasjon er korrekt før du lagrer.",
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
            text = "Feeding Amount: ${firebaseViewModel.amount.value} ml")

        Slider(
            value = firebaseViewModel.amount.value.toFloat(),
            onValueChange = { firebaseViewModel.amount.value = it.toInt() },
            valueRange = 0f..170f
        )

        Spacer(modifier = Modifier.height(26.dp))


        // RadioButtons for Pee and Poo
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
            Text(" Ikke urinasjon")
            RadioButton(
                selected = !firebaseViewModel.pee.value,
                onClick = { firebaseViewModel.pee.value = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Avføring")
            RadioButton(
                selected = firebaseViewModel.poo.value,
                onClick = { firebaseViewModel.poo.value = true }
            )
            Text("Ikke avføring")
            RadioButton(
                selected = !firebaseViewModel.poo.value,
                onClick = { firebaseViewModel.poo.value = false }
            )
        }


        Spacer(modifier = Modifier.height(26.dp))

        // Buttons  Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bryst Button
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

            // Sonde Button
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

            // Flaske Button
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

        Spacer(modifier = Modifier.height(45.dp))

        // TextField for Comment
        TextField(
            value = firebaseViewModel.comment.value,
            onValueChange = { firebaseViewModel.comment.value = it },
            label = { Text("Kommentar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Save Button
        Button(
            onClick = {
                firebaseViewModel.saveFeedingRecord(
                    amount = firebaseViewModel.amount.value,
                    pee = firebaseViewModel.pee.value,
                    poo = firebaseViewModel.poo.value,
                    feedingMethod = firebaseViewModel.feedingMethod.value,
                    comment = firebaseViewModel.comment.value
                )
                ToDash()

            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0),//<- Royal Blue
                contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

        ) {
            Text("Lagre")
        }
    }
}