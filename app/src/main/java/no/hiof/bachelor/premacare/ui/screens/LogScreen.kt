package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.model.FeedingRecord
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LoggScreen() {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    var selectedIndex by remember { mutableStateOf(0) }
    val cardColor = if (selectedIndex == 0) Color(0xFFF0F9FF)else Color(0xFFFFF7F0)


    val labels = listOf("Dags logg", " Full Log")
    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    LaunchedEffect(Unit) {
        firebaseViewModel.fetchFeedingRecords()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Segmented buttons
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE0E0E0))
                .height(40.dp)
        ) {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) Color(0xFF1565C0) else Color.Transparent)
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color(0xFF1565C0),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Content
        if (firebaseViewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        } else {
            val records = if (selectedIndex == 0) {
                firebaseViewModel.feedingRecords.filter { it.date == today }
            } else {
                firebaseViewModel.feedingRecords
            }

            if (records.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ingen data funnet.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(records) { record ->
                        LoggCard(record = record, backgroundColor = cardColor)
                    }
                }
            }
        }
    }
}


@Composable
fun LoggCard(record: FeedingRecord, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Topptekst: dato og klokkeslett
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.date,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF607D8B)
                )
                Text(
                    text = record.time.toDate().toLocaleString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF90A4AE)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Overskrift: Feeding method
            Text(
                text = record.feedingMethod,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mengde og vekt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoLabel("Mengde", "${record.amount} ml")
                InfoLabel("Vekt", "${record.weight} g")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tisset / Bæsj
            Row {
                if (record.pee) StatusChip("💧 Tisset", Color(0xFF81D4FA))  // Soft sky blue
                if (record.poo) StatusChip("💩 Bæsjet", Color(0xFFFFCC80))
            }

            // Kommentar
            if (record.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFFCFD8DC), thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = record.comment,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF455A64)
                )
            }
        }
    }
}

@Composable
fun InfoLabel(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp)
    }
}







