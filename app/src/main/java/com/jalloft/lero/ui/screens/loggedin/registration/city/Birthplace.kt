package com.jalloft.lero.ui.screens.loggedin.registration.city

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.City
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.UserFields
import timber.log.Timber


@Composable
fun BirthplaceScreen(
    citySearchViewModel: CityViewModel,
    leroViewModel: LeroViewModel,
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
) {

    val context = LocalContext.current
    val user = leroViewModel.currentUser

    var city by remember { mutableStateOf<City?>(null) }

    LaunchedEffect(key1 = user, block = {
        if (user != null && city == null){
            city = user.city
        }
    })

    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            leroViewModel.clear()
            onNext()
        }
    })

    var showCitiesContent by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.city_of_birth),
        subtitle = stringResource(R.string.what_city_were_you_born_in),
        enabledSubmitButton = city != null,
        onBack = onBack,
        errorMessage = leroViewModel.erroUpdateOrEdit,
        onSubmit = {
            if (city != user?.city){
                val updates = mapOf(
                    UserFields.CITY to city,
                )
                leroViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            }else{
                Timber.i("Dados não alterados e não atualizados")
                leroViewModel.clear()
                onNext()
            }
        },
        isLoading = leroViewModel.isLoadingUpdateOrEdit
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