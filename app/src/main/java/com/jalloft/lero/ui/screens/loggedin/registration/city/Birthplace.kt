package com.jalloft.lero.ui.screens.loggedin.registration.city

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.City
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField


@Composable
fun BirthplaceScreen(
    citySearchViewModel: CityViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var city by remember { mutableStateOf<City?>(null) }
    val isvalidSubmit = city != null
    var isLoading by remember { mutableStateOf(false) }

    var showCitiesContent by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.city_of_birth),
        subtitle = stringResource(R.string.what_city_were_you_born_in),
        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSubmit = {},
        onSkip = onNext,
        isLoading = isLoading,
    ) {
        SelectableTextField(
            label = stringResource(R.string.birthplace_field_title),
            text = if (city != null) "${city?.name}, ${city?.state}, ${city?.country}" else stringResource(
                R.string.what_city_were_you_born_in
            ),
            onOpenOptios = { showCitiesContent = true },
            hasSelected = city != null
        )
    }

    AnimatedComponent(visible = showCitiesContent) {
        ChooseCityScreen(
            city = city,
            citySearchViewModel = citySearchViewModel,
            onBack = { showCitiesContent = false },
            onSelectedCity = {
                city = it
                showCitiesContent = false
            }
        )
    }
}