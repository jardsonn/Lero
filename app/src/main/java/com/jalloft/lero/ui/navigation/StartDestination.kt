package com.jalloft.lero.ui.navigation


sealed class StartDestination(val route: String) {
    data object Start : StartDestination(route = "start")
    data object LoginWithPhone : StartDestination(route = "loginWithPhone")
    data object VerifyPhone : StartDestination(route = "verifyPhone")
}