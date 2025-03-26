package no.hiof.bachelor.premacare.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.R
import no.hiof.bachelor.premacare.model.WebsiteItem
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DashBoardScreen(home: ()-> Unit) {
    val weight = 5.5f // Eks vekt i KG (later from Firebase)
    val currentIntake = 7.75f // Eks melkeintak (later from Firebase)

    val firebaseViewModel: FirebaseViewModel = viewModel()


    // igjen en test for å hente barnenavn
    LaunchedEffect(Unit) {
        firebaseViewModel.fetchChildsName()
        firebaseViewModel.fetchMemberSinceDate()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // bare for og få logget ut slik at vi for testet. denne må legges et annet sted og penere gjordt
        Text("Logg ut",
            modifier = Modifier.clickable {
                firebaseViewModel.logOut()
                home()
            })

        SpaceEm(10.dp)

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text(
                fontSize = 18.sp, text = "Forelder/Foresatt av:"
            )
            //bare for å teste å hente ut barns navn
            SpaceEm(10.dp)

            Text(
                color = Color.Black,
                text = firebaseViewModel.childsName.value,
                fontSize = 27.sp
            )

            SpaceEm(20.dp)

        }
        Row {
            DashboardCard(
                currentIntake = currentIntake,
                weight = weight
            )

            WeightCard(
                currentIntake = currentIntake,
                weight = weight
            )

        }
        SpaceEm(20.dp)
        HorizontalLine()
        SpaceEm(10.dp)

        Text(color = Color.Black,
            text = "Nyttige Lenker",
            fontSize = 22.sp)

        SpaceEm(10.dp)

        WebsiteList(websites)

        HorizontalLine()

        CallButton()
    }

}

@Composable
fun CallButton() {
    val context = LocalContext.current
    val phoneNumber = "tel:+4741282999" // Erstatt med telefonnummeret til sykehuset
    val coroutineScope = rememberCoroutineScope()  // Husk å bruke CoroutineScope

    Button(
        onClick = {
            coroutineScope.launch {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
                context.startActivity(intent)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Ring Sykehuset", color = Color.White)
    }
}

@Composable
fun HorizontalLine() {
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun WebsiteCard(item: WebsiteItem, image: Painter) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )



            Image(
                painter = image,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            // Source text evt endre til noe som er standar
            Text(
                text = "Source: ${item.source}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WebsiteList(websites: List<WebsiteItem>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(websites) { website ->
            val image = painterResource(website.imageResId)
            WebsiteCard(website, image)
        }
    }
}

//Nyttige lenger :
// Oppretter website klasser.
val websites = listOf(
    WebsiteItem(
        title = "Ammestillinger",
        imageResId = R.drawable.ammestillinger,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/ammestillinger/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Ammeteknikk",
        imageResId = R.drawable.ammeteknikk,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/ammeteknikk/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Amming: Nyfødt",
        imageResId = R.drawable.amming_etter_fodsel,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/amming-etter-fodsel/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Utfordringer",
        imageResId = R.drawable.utfordringer,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/ammeutfordringer/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Delamming",
        imageResId = R.drawable.delamming,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/delamming/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Pumping",
        imageResId = R.drawable.pumping,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/handmelking-og-pumping/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Hvor lenge?",
        imageResId = R.drawable.hvorfor,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/hvorfor-amme-og-hvor-lenge/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Øke produksjon",
        imageResId = R.drawable.okt_prod,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/melkeproduksjon/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Oppbevaring",
        imageResId = R.drawable.oppbevering,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/oppbevare-morsmelk/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Melkemengde",
        imageResId = R.drawable.mengde,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/amming/nok-morsmelk/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Mate riktig",
        imageResId = R.drawable.mate_riktig,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/mate-barnet/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Babyens mage",
        imageResId = R.drawable.fordoyelse,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/spedbarnsbaesj/",
        source = "Helsenorge"
    ),
    WebsiteItem(
        title = "Rengjøring",
        imageResId = R.drawable.rengjoring,
        url = "https://www.helsenorge.no/spedbarn/spedbarnsmat-og-amming/alt-om-spedbarnsmat/rengjore-utstyr/",
        source = "Helsenorge"
    ),

    )

// Milk calcutation based on weight
@Composable
fun DashboardCard(
    currentIntake: Float,
    weight: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val min = 0.12f * (weight * 10)
    val max = 0.15f * (weight * 10)

    val progress = (currentIntake / max).coerceIn(0f, 1f)
    val progressColor = if (currentIntake >= min) Color.Green else Color.Red
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(progress, animationSpec = tween(durationMillis = 1000))
    }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp) // Ensuring it doesn't stretch
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(140.dp) // Restricting size of the progress bar
            ) {
                val strokeWidth = 16f

                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )

                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }


            Text(
                text = "${currentIntake} ml",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 27.sp
            )
        }
    }
}

@Composable
fun WeightCard(
    currentIntake: Float,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column {
                Text(
                    text = "Barnets Vekt", // Displays the weight variable correctly
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    fontSize = 19.sp
                )
                SpaceEm(15.dp)

                Text(
                    text = "$weight Kg",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    fontSize = 27.sp
                )
            }


        }
    }
}








