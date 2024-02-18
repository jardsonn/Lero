package com.jalloft.lero.ui.navigation


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jalloft.lero.ui.screens.loggedin.registration.viewmodel.RegistrationViewModel
import com.jalloft.lero.ui.screens.loggedout.SignInWithPhoneScreen
import com.jalloft.lero.ui.screens.loggedout.StartScreen
import com.jalloft.lero.ui.screens.loggedout.VerifyPhoneScreen
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel
import com.jalloft.lero.util.CURRENTE_REGISTRATION_ROUTE_KEY
import com.jalloft.lero.util.MANDATORY_DATA_SAVED
import com.jalloft.lero.util.getRoute
import com.orhanobut.hawk.Hawk
import timber.log.Timber


@Composable
fun LeroNavigation(navController: NavHostController, loggedOutViewModel: LoggedOutViewModel) {
    val startDestination = getStartDestination(navController, loggedOutViewModel)
    val citySearchViewModel: CityViewModel = hiltViewModel()
//    val registrationViewModel: RegistrationViewModel = hiltViewModel()

    val registerStartDestination by remember {
        mutableStateOf(
            Hawk.get(
                CURRENTE_REGISTRATION_ROUTE_KEY,
                RegisterDataDestination.EssentialInformation.route
            )
        )
    }
    SlideNavHost(
        navController,
        startDestination = startDestination
    ) {
        loggedOutGraph(navController, loggedOutViewModel)
        loggedInGraph(navController)
        registerDataGraph(
            navController,
            citySearchViewModel = citySearchViewModel,
            startDestination = registerStartDestination
//            registrationViewModel = registrationViewModel
        )
    }
}


fun NavGraphBuilder.registerDataGraph(
    navController: NavHostController,
    citySearchViewModel: CityViewModel,
    startDestination: String,
//    registrationViewModel: RegistrationViewModel,
) {
    navigation(
        startDestination = startDestination,
        route = GraphDestination.DataRegistration.route,
    ) {
        composable(RegisterDataDestination.EssentialInformation.route) {
            Hawk.put(
                CURRENTE_REGISTRATION_ROUTE_KEY,
                RegisterDataDestination.EssentialInformation.route
            )
            EssentialInformationScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.SexualIdentification.route) },
                registrationViewModel = hiltViewModel()
            )
        }

        composable(RegisterDataDestination.SexualIdentification.route) {
            Hawk.put(
                CURRENTE_REGISTRATION_ROUTE_KEY,
                RegisterDataDestination.SexualIdentification.route
            )
            SexualIdentification(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.Birthplace.route) },
                registrationViewModel = hiltViewModel()
            )
        }
        composable(RegisterDataDestination.Birthplace.route) {
            Hawk.put(CURRENTE_REGISTRATION_ROUTE_KEY, RegisterDataDestination.Birthplace.route)
            BirthplaceScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.Interest.route) },
                citySearchViewModel = citySearchViewModel,
                registrationViewModel = hiltViewModel()
            )
        }

        composable(RegisterDataDestination.Interest.route) {
            Hawk.put(CURRENTE_REGISTRATION_ROUTE_KEY, RegisterDataDestination.Interest.route)
            InterestScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.WorkAndEducation.route) },
                registrationViewModel = hiltViewModel()
            )
        }

        composable(RegisterDataDestination.WorkAndEducation.route) {
            Hawk.put(
                CURRENTE_REGISTRATION_ROUTE_KEY,
                RegisterDataDestination.WorkAndEducation.route
            )
            WorkEducationScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.Lifestyle.route) },
                registrationViewModel = hiltViewModel()
            )
        }

        composable(RegisterDataDestination.Lifestyle.route) {
            Hawk.put(CURRENTE_REGISTRATION_ROUTE_KEY, RegisterDataDestination.Lifestyle.route)
            LifestyleScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.Hobbies.route) },
                registrationViewModel = hiltViewModel()
            )
        }

        composable(RegisterDataDestination.Hobbies.route) {
            Hawk.put(CURRENTE_REGISTRATION_ROUTE_KEY, RegisterDataDestination.Hobbies.route)
            HobbiesScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onNext = { navController.navigate(RegisterDataDestination.Bio.route) },
                registrationViewModel = hiltViewModel()
            )
        }
        composable(RegisterDataDestination.Bio.route) {
            Hawk.put(CURRENTE_REGISTRATION_ROUTE_KEY, RegisterDataDestination.Bio.route)
            BiographyScreen(
                onBack = if (navController.previousBackStackEntry == null) null else ({ navController.popBackStack() }),
                onDone = {
                    navController.navigate(GraphDestination.LoggedIn.route) {
                        popUpTo(GraphDestination.DataRegistration.route) { inclusive = true }
                    }
                },
                registrationViewModel = hiltViewModel()
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
                    onAuthenticated = { firebaseUser ->
                        val user = firebaseUser ?: viewModel.firebaseUser
                        viewModel.determineNextRoute(user) {
                            navController.navigate(it) {
                                popUpTo(GraphDestination.LoggedOut.route) { inclusive = true }
                            }
                        }
//                        navController.navigate(GraphDestination.DataRegistration.route.plus("?userId=${user?.uid}")) {
//                            popUpTo(GraphDestination.LoggedOut.route) { inclusive = true }
//                        }
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

fun getStartDestination(
    navController: NavHostController,
    loggedOutViewModel: LoggedOutViewModel
): String {
    val isAuthenticated = loggedOutViewModel.isAuthenticated
    return if (isAuthenticated) {
        if (!Hawk.contains(MANDATORY_DATA_SAVED) || !Hawk.get<Boolean?>(MANDATORY_DATA_SAVED)) {
            GraphDestination.DataRegistration.route
        } else
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