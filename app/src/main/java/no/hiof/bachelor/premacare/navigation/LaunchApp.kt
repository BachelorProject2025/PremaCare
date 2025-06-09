package no.hiof.bachelor.premacare.navigation


import CoParentScreen
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import no.hiof.bachelor.premacare.R
import no.hiof.bachelor.premacare.ui.screens.AboutUs
import no.hiof.bachelor.premacare.ui.screens.DashBoardScreen
import no.hiof.bachelor.premacare.ui.screens.EmergencyCheckScreen
import no.hiof.bachelor.premacare.ui.screens.LoggScreen
import no.hiof.bachelor.premacare.ui.screens.LoginScreen
import no.hiof.bachelor.premacare.ui.screens.MessageScreen
import no.hiof.bachelor.premacare.ui.screens.NewEntry
import no.hiof.bachelor.premacare.ui.screens.PasswordResetScreen
import no.hiof.bachelor.premacare.ui.screens.RegisterScreen
import no.hiof.bachelor.premacare.ui.screens.SettingsScreen
import no.hiof.bachelor.premacare.viewModels.FirebaseViewModel
import androidx.compose.runtime.livedata.observeAsState


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
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val unreadState = firebaseViewModel.hasUnreadMessages.value

    // <--- LEGG TIL DENNE LINJEN ETTER `unreadState` DEFINERES
    Log.d("PremaCareApp", "LaunchApp recomposed. unreadState: $unreadState")

    LaunchedEffect(Unit) {
        firebaseViewModel.fetchMessagesRealtime() //
    }




    // Check authentication status
    val currentUser = auth.currentUser
    val startDestination = determineStartDestination(auth.currentUser)

    Scaffold(topBar = {
        if (isTopAppVisible.value) {

            Column(
                //.background(Color(0xFF50D4F2))
                modifier = Modifier
                    .background(colorResource(R.color.top_and_navbar_blue))
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black
                    ),
                    title = {
                        when (currentRoute) {
                            AppScreens.Login.name, AppScreens.Register.name -> {

                            }

                            else -> {

                                Image(
                                    painter = painterResource(id = R.drawable.premacare),
                                    contentDescription = "App Logo",
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(200.dp),
                                    //colorFilter = ColorFilter.tint(Color(0xFF0077B6))

                                )
                            }
                        }
                    }
                ,
                    navigationIcon = {

                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint =  Color(0xFF0077B6)
                            )
                        }
                    },
                    actions = {
                        if (currentUser != null){
                            IconButton(onClick = {
                                navController.navigate(AppScreens.Settings.name)

                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "User Settings",
                                    tint =  Color(0xFF0077B6)
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
            BottomAppBar(containerColor = colorResource(R.color.top_and_navbar_blue)) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 15.dp, 0.dp)
                        .height(38.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (item in items) {
                        val isSelected = item.screen == currentSelectedItem.value
                        val isMessageItem = item.screen == AppScreens.Message

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                navController.navigate(item.screen.name)
                                currentSelectedItem.value = item.screen
                            }
                        ) {
                            if (isMessageItem && unreadState) {
                                BadgedBox(badge = {
                                    Badge(containerColor = Color.Red, content = {
                                        
                                    })
                                }) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        modifier = Modifier.size(21.dp),
                                        tint = if (isSelected) Color(0xFF5A6C7B) else Color(
                                            0xFF0077B6
                                        )
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(21.dp),
                                    tint = if (isSelected) Color(0xFF5A6C7B) else Color(0xFF0077B6)
                                )
                            }

                            Text(
                                text = item.label,
                                color = if (isSelected) Color(0xFF5A6C7B) else Color(0xFF0077B6)
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
                LoggScreen()
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
                    , auth = auth,
                    reset = {navController.navigate(AppScreens.Reset.name)})
            }

            composable(AppScreens.Reset.name) {
                isBottomBarVisible.value = false
                isTopAppVisible.value = false
                isFloatingActionButtonVisible.value = false
               PasswordResetScreen(navController = navController)
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
                DashBoardScreen(takeMeToEc = {navController.navigate(AppScreens.EmergencyCheck.name)})
            }

            composable(AppScreens.NewEntry.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = false
                NewEntry(ToDash = {navController.navigate(AppScreens.DashBoard.name)})
            }

            composable(AppScreens.Message.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = false // <-- Fjerner FAB(floating action btn) i denne screenen for bedre skjerm plass
                MessageScreen()
            }

            composable(AppScreens.Settings.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = false
                SettingsScreen({navController.navigate(AppScreens.Reset.name)},
                    {
                        navController.navigate(AppScreens.CoParent.name)
                    },
                    home = {navController.navigate(AppScreens.Login.name)})
            }

            composable(AppScreens.CoParent.name) {
                isBottomBarVisible.value = true
                isTopAppVisible.value = true
                isFloatingActionButtonVisible.value = true
                CoParentScreen(auth.currentUser?.uid.toString(),
                    {navController.navigate(AppScreens.DashBoard.name)})
            }

            composable(AppScreens.EmergencyCheck.name) {
                isBottomBarVisible.value = false
                isTopAppVisible.value = false
                isFloatingActionButtonVisible.value = false
                EmergencyCheckScreen()
            }

        }

    }

}
//List of Icons and text for navbar(bottom bar)
val items = listOf(

    BottomNavItems(AppScreens.DashBoard, Icons.Outlined.Dashboard, "Dashboard"),
    //BottomNavItems(AppScreens.DashBoard, Icons.Outlined.Inventory, "Bestilling"),
    BottomNavItems(AppScreens.Log, Icons.Outlined.List, "Logg"),
    BottomNavItems(AppScreens.Message, Icons.Outlined.Message, "Meldninger")

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
        //Color.DarkGray,
        containerColor = Color.DarkGray,
        contentColor = Color.White

    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription =" Add Action" )
    }
}

