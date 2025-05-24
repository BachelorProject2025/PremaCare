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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.R
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntry(ToDash: () -> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp) // mellomrom mellom kort
    ) {
        item {
            Text(
                text = "Fôring & helse",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Sørg for at all informasjon er korrekt før du lagrer.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // === CARD 1: Mengde melk ===
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.baby_blue)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp)) // Skygge og avrundede hjørner
                    .clip(RoundedCornerShape(12.dp)) // Avrundede kanter på kortet
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Mengde melk: ${firebaseViewModel.amount.value} ml", fontWeight = FontWeight.Bold)
                    Slider(
                        value = firebaseViewModel.amount.value.toFloat(),
                        onValueChange = { firebaseViewModel.amount.value = it.toInt() },
                        valueRange = 0f..170f
                    )
                }
            }
        }
        item {
            // === CARD 2: Fôringsmetode ===
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.baby_blue)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp)) // Skygge og avrundede hjørner
                    .clip(RoundedCornerShape(12.dp)) // Avrundede kanter på kortet
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Bryst", "Sonde", "Flaske").forEach { method ->
                        TextButton(
                            onClick = { firebaseViewModel.feedingMethod.value = method },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (firebaseViewModel.feedingMethod.value == method) Color.White else Color(0xFF1565C0),
                                contentColor = if (firebaseViewModel.feedingMethod.value == method) Color(0xFF1565C0) else Color.White
                            )
                        ) {
                            Text(method)
                        }
                    }
                }
            }
        }

        // === CARD 3: Vekt (nytt kort for vekt) ===
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.baby_pink)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp)) // Skygge og avrundede hjørner
                    .clip(RoundedCornerShape(12.dp)) // Avrundede kanter på kortet
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                            firebaseViewModel.weight.value = lastWeight // Sett vekten her
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
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                        placeholder = {
                            Text(
                                text = if (lastWeight > 0) "$lastWeight" else "Skriv inn vekt i gram"
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            // === CARD 3: Urin og Avføring ===
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.baby_green)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp)) // Skygge og avrundede hjørner
                    .clip(RoundedCornerShape(12.dp)) // Avrundede kanter på kortet
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(" \uD83D\uDCA7 Urinasjon ")
                    RadioButton(
                        selected = firebaseViewModel.pee.value,
                        onClick = { firebaseViewModel.pee.value = true }
                    )

                    Text(" \uD83D\uDCA9 Avføring")
                    RadioButton(
                        selected = firebaseViewModel.poo.value,
                        onClick = { firebaseViewModel.poo.value = true }
                    )
                }
            }
        }



        item {
            // === CARD 5: Kommentar og Lagre-knapp ===
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.baby_yellow)),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(12.dp)) // Skygge og avrundede hjørner
                    .clip(RoundedCornerShape(12.dp)) // Avrundede kanter på kortet
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = firebaseViewModel.comment.value,
                        onValueChange = { firebaseViewModel.comment.value = it },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                        label = { Text("Kommentar") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))


            }

        }

            Spacer(modifier = Modifier.height(16.dp))
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


}

