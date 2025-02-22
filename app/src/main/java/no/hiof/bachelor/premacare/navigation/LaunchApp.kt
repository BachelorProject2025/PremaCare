package no.hiof.bachelor.premacare.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import no.hiof.bachelor.premacare.R
import no.hiof.bachelor.premacare.ui.screens.AboutUs
import no.hiof.bachelor.premacare.ui.screens.DashBoardScreen
import no.hiof.bachelor.premacare.ui.screens.LogScreen
import no.hiof.bachelor.premacare.ui.screens.LoginScreen
import no.hiof.bachelor.premacare.ui.screens.NewEntry
import no.hiof.bachelor.premacare.ui.screens.RegisterScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchApp(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val isBottomBarVisible = remember { mutableStateOf(true) }
    val isTopAppVisible = remember { mutableStateOf(true) }
    val isFloatingActionButtonVisible = remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentSelectedItem = remember { mutableStateOf(AppScreens.DashBoard) }

    // Check authentication status
    val currentUser = auth.currentUser
    val startDestination = determineStartDestination(auth.currentUser)

    Scaffold(topBar = {
        if (isTopAppVisible.value) {
            // Define the gradient
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF79ECE0),// cyan
                    Color.White
                )
            )


            // TopAppBar title based on currentRoute
            val title = when (currentRoute) {
                AppScreens.Login.name -> "Login"
                AppScreens.Register.name -> "Register"
                AppScreens.Log.name -> "Log"
                AppScreens.DashBoard.name -> "Dashboard"
                AppScreens.NewEntry.name -> "New Entry"

                // TODO:
                //  add the search title or a searchbar
                // Add other screens
                else -> "Prema Care"
            }

            // Apply the gradient as a background to a Box, Column, or any suitable composable
            // that fills the top bar area.
            Column(modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent, // Make the AppBar container transparent
                        titleContentColor = Color.Black
                    ),
                    title = {
                        Text(
                            title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    },
                    navigationIcon = {

                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint =  Color.Black
                            )
                        }
                    },
                    actions = {
                        if (currentUser != null){
                            IconButton(onClick = {


                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "User Settings",
                                    tint =  Color.Black
                                )


                            }
                        }
                        else {
                            IconButton(onClick = {
                                navController.navigate(AppScreens.Login.name)
                            }) {
                                Icon(imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = "Login",
                                    tint =  Color.Black)
                            }
                        }

                    },
                    windowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Top)
                )
            }
        }
    }, bottomBar = {
        if (isBottomBarVisible.value) {

            BottomAppBar(containerColor = Color(0xFF79ECE0)) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 20.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (item in items) {
                        val isSelected = item.screen == currentSelectedItem.value
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(item.screen.name)
                                    currentSelectedItem.value = item.screen
                                }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(30.dp),
                                tint = if (isSelected) Color.Black else Color.LightGray

                            )
                            Text(
                                text = item.label,
                                color = if (isSelected) Color.Black else Color.LightGray
                                )

                        }
                    }
                }
            }
        }
    }, floatingActionButton = {
        if (isFloatingActionButtonVisible.value){
            SearchFloatingAction(toEntry = {
                navController.navigate(AppScreens.NewEntry.name)
            })
        }
    }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            Modifier.padding(innerPadding)
        ) {



            composable(AppScreens.Log.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = true
                LogScreen()
            }


            composable(AppScreens.Login.name) {
                isBottomBarVisible.value = false
                isTopAppVisible.value = false
                isFloatingActionButtonVisible.value = false
                LoginScreen(
                    takeMeDash = {
                        // checking if user credentials is a match
                        if (currentUser != null) {
                            navController.navigate(AppScreens.DashBoard.name)
                        } else {
                            navController.navigate(AppScreens.Login.name)
                        } },
                    newUser = {navController.navigate(AppScreens.Register.name)},
                    aboutUs = {navController.navigate(AppScreens.About.name)}
                    , auth = auth )
            }

            composable(AppScreens.Register.name) {
                isBottomBarVisible.value = false
                isTopAppVisible.value = false
                isFloatingActionButtonVisible.value = false
                RegisterScreen(toLogin = {navController.navigate(AppScreens.Login.name)})
            }

            composable(AppScreens.About.name) {
                isBottomBarVisible.value = false
                isTopAppVisible.value = false
                isFloatingActionButtonVisible.value = false
                AboutUs()
            }

            composable(AppScreens.DashBoard.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = true
                DashBoardScreen(home = {navController.navigate(AppScreens.Login.name)})
            }

            composable(AppScreens.NewEntry.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = true
                NewEntry()
            }

        }

    }

}
//List of Icons and text for navbar(bottom bar)
val items = listOf(

    BottomNavItems(AppScreens.DashBoard, Icons.Outlined.Dashboard, "Dashboard"),
    BottomNavItems(AppScreens.Log, Icons.Outlined.List, "Log"),

)


private fun determineStartDestination(currentUser: FirebaseUser?): String {
    return if (currentUser != null) {
        AppScreens.DashBoard.name
    } else {
        AppScreens.Login.name
    }

}




@Composable
fun SearchFloatingAction( toEntry: ()-> Unit) {
    FloatingActionButton(onClick = { toEntry()},
        containerColor = Color.LightGray,
        contentColor = Color.White

    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription =" Add Action" )
    }
}