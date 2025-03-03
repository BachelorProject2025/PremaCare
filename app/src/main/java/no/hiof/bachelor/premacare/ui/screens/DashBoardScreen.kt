package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun DashBoardScreen(home: ()-> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFCC80),  // Light orange
            Color(0xFFFFDAB9)   // Peach
        )
    )
    // igjen en test for å hente barnenavn
    LaunchedEffect(Unit) {
        firebaseViewModel.fetchChildsName()
        firebaseViewModel.fetchMemberSinceDate()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // bare for og få logget ut slik at vi for testet. denne må legges et annet sted og penere gjordt
        Text("Logout",
            modifier = Modifier.clickable {
                firebaseViewModel.logOut()
                home()
            })


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .shadow(8.dp, shape = RoundedCornerShape(5.dp))
                .background(gradient, shape = RoundedCornerShape(5.dp)),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradient)
            ){

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                Text(
                    fontSize = 18.sp, text = "Parent or Legal Representative of:")
                //bare for å teste å hente ut barns navn
                    SpaceEm(10.dp)
                Text(color = Color.Black,
                    text = firebaseViewModel.childsName.value,
                    fontSize = 22.sp)
                    }

            }

        }
    }

}