package no.hiof.bachelor.premacare.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import no.hiof.bachelor.premacare.model.FeedingRecord
import no.hiof.bachelor.premacare.model.Message
import no.hiof.bachelor.premacare.viewModels.getEffectiveUserIdSuspend
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DashBoardScreen() {
    val firebaseViewModel: FirebaseViewModel = viewModel()



    val currentIntake by firebaseViewModel.currentIntake.observeAsState(0.0f) // Default value
    val currentWeight by firebaseViewModel.lastWeight.observeAsState(0.0f)



    // Kall funksjonen for å hente totalt inntak for i dag når composable-en først blir lastet

    //LaunchedEffect(Unit) {
    //    firebaseViewModel.getTotalAmountForToday()
    //    firebaseViewModel.getLastWeight()
    //    firebaseViewModel.fetchChildsName()
    //    firebaseViewModel.fetchMemberSinceDate()
    //}
    LaunchedEffect(Unit) {
        val effectiveUserId = getEffectiveUserIdSuspend()
        Log.d("DEBUG", "Effektiv bruker-ID er: $effectiveUserId")

        // Sjekk om effectiveUserId er korrekt før videre kall
        if (effectiveUserId != null) {
                // Hent dataene for Forelder 1 (eller medforelder hvis det er aktuelt)
                firebaseViewModel.getTotalAmountForToday()
                firebaseViewModel.getLastWeight()
                firebaseViewModel.fetchChildsName()
                firebaseViewModel.fetchMemberSinceDate()
            Log.d("DEBUG", "Data hentet for bruker-ID: $effectiveUserId")
        } else {
            Log.e("ERROR", "Ingen gyldig effektiv bruker-ID funnet!")
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {


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
                weight = currentWeight.toFloat()

            )

            WeightCard(
                currentIntake = currentIntake,
                weight = currentWeight.toFloat()
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

//@Composable
//fun WebsiteList(websites: List<WebsiteItem>) {
//    LazyRow(modifier = Modifier.fillMaxWidth()) {
//        items(websites) { website ->
//            val image = painterResource(website.imageResId)
//            WebsiteCard(website, image)
//        }
//    }
//}

//WebsireList med ikoner : venstre og høre scoll mot hørte aktiverer venstre ikon
@Composable
fun WebsiteList(websites: List<WebsiteItem>) {
    val listState = rememberLazyListState()

    // Derived states for showing scroll indicators
    val showLeftArrow by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val showRightArrow by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisible != null && lastVisible.index < websites.lastIndex
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState
        ) {
            items(websites) { website ->
                val image = painterResource(website.imageResId)
                WebsiteCard(website, image)
            }
        }

        // Left scroll indicator
        if (showLeftArrow) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Scroll left",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .padding(4.dp),
                tint = Color.Gray
            )
        }

        // Right scroll indicator
        if (showRightArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Scroll right",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .padding(4.dp),
                tint = Color.Gray
            )
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

// Milk total basert på barnets vekt, 15% av vekt er bra ->  grønn, 12% er ok -> gul under 12% er -> rød
//dashboard card uten konfetti
//@Composable
//fun DashboardCard(
//    currentIntake: Float,
//    weight : Float,
//    modifier: Modifier = Modifier,
//
//) {
//    val context = LocalContext.current
//
//
//    val min = if (weight > 0) 0.12f * weight else 0f
//    val max = if (weight > 0) 0.15f * weight else 1f
//
//    // Kalkulasjon progress basert på currentIntake og max value
//    val progress = (currentIntake / max).coerceIn(0f, 1f)
//    val progressColor = if (currentIntake >= min) Color.Green else Color.Red
//    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }
//
//    // Oppdatert animasjons progress når currentIntake forandres
//    LaunchedEffect(currentIntake) {
//        animatedProgress.animateTo(progress, animationSpec = tween(durationMillis = 1000))
//    }
//
//    // Sjekk dato og tilbakestill currentIntake ved midnatt
//    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
//    var lastResetDate by remember { mutableStateOf(currentDate) }
//
//    // Hvis datoen har endret seg, tilbakestill currentIntake til 0
//    if (currentDate != lastResetDate) {
//        // Dette betyr at det er en ny dag
//        LaunchedEffect(currentDate) {
//            // Tilbakestill når ny dag begynner
//
//            // For nå bare tilbakestiller vi currentIntake
//
//            // currentIntake = 0
//            lastResetDate = currentDate // Oppdater datoen
//        }
//    }
//
//    Card(
//        modifier = Modifier
//            .width(200.dp)
//            .height(200.dp)
//            .padding(8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Canvas(
//                modifier = Modifier.size(140.dp)
//            ) {
//                val strokeWidth = 16f
//
//                drawArc(
//                    color = Color.LightGray,
//                    startAngle = -90f,
//                    sweepAngle = 360f,
//                    useCenter = false,
//                    style = Stroke(width = strokeWidth)
//                )
//
//                drawArc(
//                    color = progressColor,
//                    startAngle = -90f,
//                    sweepAngle = 360f * animatedProgress.value,
//                    useCenter = false,
//                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                )
//            }
//
//            Text(
//                text = "${currentIntake} ml",
//                style = MaterialTheme.typography.bodyLarge.copy(
//                    fontWeight = FontWeight.Bold
//                ),
//                color = Color.Black,
//                fontSize = 27.sp
//            )
//        }
//    }
//}

//dashboard card med konfetti
// Når barnet hyar spist 15 % av sin kropsvekt innen 24 timer altså full sirkel vil det komme en konfetti animasjon over progress bar
@Composable
fun DashboardCard(
    currentIntake: Float,
    weight: Float,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val min = if (weight > 0) 0.12f * weight else 0f
    val max = if (weight > 0) 0.15f * weight else 1f

    // Kalkulasjon progress basert på currentIntake og max value
    val progress = (currentIntake / max).coerceIn(0f, 1f)
    val progressColor = if (currentIntake >= min) Color.Green else Color.Red
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    // Confetti-animasjon
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    var lastIntake by remember { mutableStateOf(currentIntake) }
    var confettiPlayedDate by rememberSaveable { mutableStateOf("") }

    // Oppdatert animasjons progress når currentIntake forandres
    LaunchedEffect(currentIntake) {
        animatedProgress.animateTo(progress, animationSpec = tween(durationMillis = 1000))
    }

    // Sjekk dato og tilbakestill currentIntake ved midnatt
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var lastResetDate by remember { mutableStateOf(currentDate) }

    // Hvis datoen har endret seg, tilbakestill currentIntake til 0
    if (currentDate != lastResetDate) {
        // Dette betyr at det er en ny dag
        LaunchedEffect(currentDate) {
            // Tilbakestill når ny dag begynner
            // Denne koden kan tilbakestille Firebase
            // For nå bare tilbakestiller vi currentIntake
            // currentIntake = 0
            lastResetDate = currentDate // Oppdater datoen
            confettiPlayedDate = ""     // Reset confetti for ny dag
        }
    }

    // Vis confetti én gang når maks nås
    LaunchedEffect(currentIntake) {
        if (
            lastIntake < max && currentIntake >= max &&
            confettiPlayedDate != currentDate
        ) {
            confettiPlayedDate = currentDate
            delay(3000)
        }
        lastIntake = currentIntake
    }

    Card(
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
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
                modifier = Modifier.size(140.dp)
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

            // Confetti lagt som øverste lag
            if (currentIntake >= max) {
                LottieAnimation(
                    composition = composition,
                    progress = { animationProgress },
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                )
            }
        }
    }
}



//Vekt card uten animasjon
//@Composable
//fun WeightCard(
//    currentIntake: Float,
//    weight: Float,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = Modifier
//            .width(200.dp)
//            .height(200.dp)
//            .padding(8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//
//            Column {
//                Text(
//                    text = "Barnets Vekt",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontWeight = FontWeight.Bold
//                    ),
//                    color = Color.Black,
//                    fontSize = 19.sp
//                )
//                SpaceEm(15.dp)
//
//                Text(
//                    text = "$weight G",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontWeight = FontWeight.Bold
//                    ),
//                    color = Color.Black,
//                    fontSize = 27.sp
//                )
//            }
//
//
//        }
//    }
//}

//Vekt card med animasjon
// når barnet er +1000g av siste vakt altså lagt på sef 1 kg vil det komme en pil opp animasjon
@Composable
fun WeightCard(
    currentIntake: Float,
    weight: Float,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("arrowUp.json"))
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    var showArrow by rememberSaveable { mutableStateOf(false) }
    var lastShownWeight by rememberSaveable { mutableStateOf(weight) }
    var hasInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(weight) {
        // Første gang ignorer vi
        if (!hasInitialized) {
            hasInitialized = true
            lastShownWeight = weight
            return@LaunchedEffect
        }

        if ((weight - lastShownWeight) >= 1000f) {
            showArrow = true
            lastShownWeight = weight
            delay(3000)
            showArrow = false
        } else if (weight != lastShownWeight) {
            lastShownWeight = weight
        }
    }

    Card(
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Barnets Vekt",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "$weight G",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    fontSize = 27.sp
                )
            }

            if (showArrow) {
                LottieAnimation(
                    composition = composition,
                    progress = { animationProgress },
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                )
            }
        }
    }
}










