package com.jalloft.lero.ui.navigation


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.jalloft.lero.ui.screens.loggedin.registration.BiographyScreen
import com.jalloft.lero.ui.screens.loggedin.registration.LifestyleScreen
import com.jalloft.lero.ui.screens.loggedin.registration.EssentialInformationScreen
import com.jalloft.lero.ui.screens.loggedin.registration.HobbiesScreen
import com.jalloft.lero.ui.screens.loggedin.registration.InterestScreen
import com.jalloft.lero.ui.screens.loggedin.registration.SexualIdentification
import com.jalloft.lero.ui.screens.loggedin.registration.WorkEducationScreen
import com.jalloft.lero.ui.screens.loggedin.registration.city.BirthplaceScreen
import com.jalloft.lero.ui.screens.loggedin.registration.city.CityViewModel
import com.jalloft.lero.ui.screens.loggedout.SignInWithPhoneScreen
import com.jalloft.lero.ui.screens.loggedout.StartScreen
import com.jalloft.lero.ui.screens.loggedout.VerifyPhoneScreen
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel


@Composable
fun LeroNavigation(navController: NavHostController, loggedOutViewModel: LoggedOutViewModel) {
//    val startDestination = getStartDestination(loggedOutViewModel)
    val startDestination = GraphDestination.DataRegistration.route
    val citySearchViewModel: CityViewModel = hiltViewModel()
    SlideNavHost(
        navController,
        startDestination = startDestination
    ) {
        loggedOutGraph(navController, loggedOutViewModel)
        loggedInGraph(navController)
        registerDataGraph(navController, citySearchViewModel = citySearchViewModel)
    }
}

fun NavGraphBuilder.registerDataGraph(
    navController: NavHostController,
    citySearchViewModel: CityViewModel
) {
    navigation(
        startDestination = RegisterDataDestination.Bio.route,
        route = GraphDestination.DataRegistration.route,
    ) {

        composable(RegisterDataDestination.EssentialInformation.route) {
            EssentialInformationScreen(
                onBack = {},
                onNext = {},
            )
        }

        composable(RegisterDataDestination.SexualIdentification.route) {
            SexualIdentification(
                onBack = {},
                onNext = {},
            )
        }

        composable(RegisterDataDestination.Birthplace.route) {
            BirthplaceScreen(
                onBack = {},
                onNext = {},
                citySearchViewModel = citySearchViewModel,
            )
        }

        composable(RegisterDataDestination.Interest.route) {
            InterestScreen(
                onBack = {},
                onNext = {},
            )
        }

        composable(RegisterDataDestination.WorkAndEducation.route) {
            WorkEducationScreen(
                onBack = {},
                onNext = {},
            )
        }

        composable(RegisterDataDestination.Lifestyle.route) {
            LifestyleScreen(
                onBack = {},
                onNext = {},
            )
        }

        composable(RegisterDataDestination.Hobbies.route) {
            HobbiesScreen(
                onBack = {},
                onNext = {},
            )
        }
        composable(RegisterDataDestination.Bio.route) {
            BiographyScreen(
                onBack = {},
                onDone = {},
            )
        }

    }
}


fun NavGraphBuilder.loggedOutGraph(navController: NavController, viewModel: LoggedOutViewModel) {
    navigation(
        startDestination = StartDestination.Start.route,
        route = GraphDestination.LoggedOut.route,
    ) {
        composable(StartDestination.Start.route) {
            StartScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSignInWithNumber = {
                    navController.navigate(StartDestination.LoginWithPhone.route)
                }
            )
        }
        composable(StartDestination.LoginWithPhone.route) {
            SignInWithPhoneScreen(
                viewModel = viewModel,
                onVerifyNumber = { verificationId, phoneNumber ->
                    navController.navigate(StartDestination.VerifyPhone.route.plus("/?verificationId=${verificationId}&phoneNumber=$phoneNumber"))
                },
                onAuthenticated = {
                    navController.navigate(GraphDestination.LoggedIn.route) {
                        popUpTo(GraphDestination.LoggedOut.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            StartDestination.VerifyPhone.route.plus("/?verificationId={verificationId}&phoneNumber={phoneNumber}"),
            arguments = listOf(
                navArgument("verificationId") { this.nullable = true },
                navArgument("phoneNumber") { this.nullable = true },
            )
        ) { navBackStack ->
            navBackStack.arguments?.let { bundle ->
                val verificationId = bundle.getString("verificationId").orEmpty()
                val phoneNumber = bundle.getString("phoneNumber").orEmpty()
                VerifyPhoneScreen(
                    viewModel = viewModel,
                    verificationId = verificationId,
                    phoneNumber = phoneNumber,
                    onAuthenticated = {
                        navController.navigate(GraphDestination.LoggedIn.route) {
                            popUpTo(GraphDestination.LoggedOut.route) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}


fun NavGraphBuilder.loggedInGraph(navController: NavController) {
    navigation(
        startDestination = BottomNavigationDestination.Home.route,
        route = GraphDestination.LoggedIn.route,
    ) {
        composable(BottomNavigationDestination.Home.route) {}
        composable(BottomNavigationDestination.Like.route) {}
        composable(BottomNavigationDestination.Match.route) {}
        composable(BottomNavigationDestination.Message.route) {}
        composable(BottomNavigationDestination.Profile.route) {}
    }
}


sealed class GraphDestination(val route: String) {
    data object LoggedIn : GraphDestination("loggedIn")
    data object LoggedOut : GraphDestination("loggedOut")
    data object DataRegistration : GraphDestination("dataRegistration")
}

fun getStartDestination(loggedOutViewModel: LoggedOutViewModel): String {
    val isAuthenticated = loggedOutViewModel.isAuthenticated
    return if (isAuthenticated) {
        GraphDestination.LoggedIn.route
    } else {
        GraphDestination.LoggedOut.route
    }
}

@Composable
fun SlideNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(350)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
        builder = builder
    )
}