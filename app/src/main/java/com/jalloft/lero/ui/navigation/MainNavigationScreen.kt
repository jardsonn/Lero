package com.jalloft.lero.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigationScreen(navController: NavHostController, loggedOutViewModel: LoggedOutViewModel = hiltViewModel()) {
    var currentRoute by remember { mutableStateOf(StartDestination.Start.route) }

    if (screensBottomMenu.map { it.route }.contains(currentRoute)) {
        Scaffold(
            bottomBar = {
//            BottomAppBar { BottomNavigationBar(navController = navController) }
                BottomNavigationBar(navController = navController)

        }) { LeroNavigation(navController = navController, loggedOutViewModel) }
    } else {
        LeroNavigation(navController = navController, loggedOutViewModel)
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { currentRoute = it }
    }
}