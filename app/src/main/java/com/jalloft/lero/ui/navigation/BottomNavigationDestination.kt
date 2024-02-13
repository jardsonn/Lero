package com.jalloft.lero.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jalloft.lero.R


sealed class BottomNavigationDestination(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconResourceId: Int,
    @DrawableRes val iconCheckedResourceId: Int,
) {
    data object Home :
        BottomNavigationDestination(
            "home",
            R.string.home,
            R.drawable.ic_home_regular,
            R.drawable.ic_home_solid
        )

    data object Like :
        BottomNavigationDestination(
            "like",
            R.string.likes,
            R.drawable.ic_like_regular,
            R.drawable.ic_like_solid
        )

    data object Match :
        BottomNavigationDestination(
            "match",
            R.string.matches,
            R.drawable.ic_match_regular,
            R.drawable.ic_match_solid
        )

    data object Message :
        BottomNavigationDestination(
            "message",
            R.string.messages,
            R.drawable.ic_chat_regular,
            R.drawable.ic_chat_solid
        )

    data object Profile :
        BottomNavigationDestination(
            "profile",
            R.string.profile,
            R.drawable.ic_person_regular,
            R.drawable.ic_person_solid
        )
}

val screensBottomMenu = listOf(
    BottomNavigationDestination.Home,
    BottomNavigationDestination.Like,
    BottomNavigationDestination.Match,
    BottomNavigationDestination.Message,
    BottomNavigationDestination.Profile,
)
