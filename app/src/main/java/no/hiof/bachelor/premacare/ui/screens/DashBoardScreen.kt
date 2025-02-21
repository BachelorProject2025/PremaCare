package no.hiof.bachelor.premacare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel

@Composable
fun DashBoardScreen(home: ()-> Unit) {
    val firebaseViewModel: FirebaseViewModel = viewModel()

    // igjen en test for å hente barnenavn
    LaunchedEffect(Unit) {
        firebaseViewModel.fetchChildsName()
        firebaseViewModel.fetchMemberSinceDate()
    }

    // bare for og få logget ut slik at vi for testet. denne må legges et annet sted og penere gjordt
    Text("Logout",
        modifier = Modifier.clickable {
            firebaseViewModel.logOut()
            home()
        })
//bare for å teste å hente ut barns navn 
    Text(firebaseViewModel.childsName.value)





}