package com.jalloft.lero.ui.navigation


sealed class AlternativeDestination(val route: String){
    data object Preferences: AlternativeDestination("preferences")
}