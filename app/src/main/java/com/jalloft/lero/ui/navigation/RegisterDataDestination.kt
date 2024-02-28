package com.jalloft.lero.ui.navigation


sealed class RegisterDataDestination(val route: String) {
    data object EssentialInformation : RegisterDataDestination(route = "essentialInformation")
    data object SexualIdentification : RegisterDataDestination(route = "sexualIdentification")
    data object Birthplace : RegisterDataDestination(route = "birthplace")
    data object Interest : RegisterDataDestination(route = "interests")
    data object WorkAndEducation : RegisterDataDestination(route = "workAndEducation")
    data object Lifestyle : RegisterDataDestination(route = "lifestyle")
    data object Hobbies : RegisterDataDestination(route = "hobbies")
    data object Bio : RegisterDataDestination(route = "bio")
    data object Photo : RegisterDataDestination(route = "photo")
    data object Localization : RegisterDataDestination(route = "localization")
}