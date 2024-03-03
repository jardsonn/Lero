package com.jalloft.lero.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jalloft.lero.R
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel
import com.orhanobut.hawk.Hawk


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen(
    navController: NavHostController,
) {
    var currentRoute by remember { mutableStateOf(StartDestination.Start.route) }

    if (screensBottomMenu.map { it.route }.contains(currentRoute)) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.lero_logo),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier.size(78.dp)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)

            }) { contentPadding -> LeroNavigation(contentPadding, navController = navController) }
    } else {
        LeroNavigation(navController = navController)
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route ->
            currentRoute = route
        }
    }
}