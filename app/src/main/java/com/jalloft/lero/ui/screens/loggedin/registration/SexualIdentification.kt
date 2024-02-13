package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.DialogInformation
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.theme.LeroTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexualIdentification(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var gender by remember { mutableStateOf<SexualGender?>(null) }
    var orientation by remember { mutableStateOf<SexualOrientation?>(null) }
    val isvalidSubmit = gender != null && orientation != null
    var isLoading by remember { mutableStateOf(false) }

    var showSexualGenderOptions by remember { mutableStateOf(false) }
    var showSexualOrientationOptions by remember { mutableStateOf(false) }
    var showGenderInformation by remember { mutableStateOf(false) }
    var showlOrientationInformation by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.identification),
        subtitle = stringResource(R.string.identification_inform),
        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSubmit = {
        },
        isLoading = isLoading
    ) {

        SelectableTextField(
            label = stringResource(R.string.gender),
            text = gender?.nameResId?.let { stringResource(id = it) }
                ?: stringResource(R.string.gender_placeholder),
            hasSelected = gender != null,
            onInformation = { showGenderInformation = true },
            onOpenOptios = { showSexualGenderOptions = true },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SelectableTextField(
            label = stringResource(R.string.orientation),
            text = orientation?.nameResId?.let { stringResource(id = it) }
                ?: stringResource(R.string.orientation_placeholder),
            hasSelected = orientation != null,
            onInformation = { showlOrientationInformation = true },
            onOpenOptios = { showSexualOrientationOptions = true }
        )
    }


    if (showGenderInformation) {
        DialogInformation(
            onDismissRequest = { showGenderInformation = false },
            confirmButton = { showGenderInformation = false },
            title = stringResource(R.string.information_about_gender),
            text = stringResource(R.string.aviso_privacidade_genero),
        )
    }

     if (showlOrientationInformation) {
        DialogInformation(
            onDismissRequest = { showlOrientationInformation = false },
            confirmButton = { showlOrientationInformation = false },
            title = stringResource(R.string.information_about_orientation),
            text = stringResource(R.string.aviso_privacidade_orientacao),
        )
    }



    AnimatedComponent(visible = showSexualOrientationOptions) {
        OptionsListScaffold(
            modifier = Modifier
                .fillMaxSize(),
            title = stringResource(R.string.select_sexual_orientation),
            subtitle = stringResource(R.string.select_orientation_subtitle),
            onBack = { showSexualOrientationOptions = false },
        ) {
            val list = SexualOrientation.entries
            items(list.size) { index ->
                val currentOrientation = list[index]
                val isChecked = currentOrientation == orientation
                ItemOption(
                    modifier = Modifier.fillMaxSize(),
                    isChecked = isChecked,
                    name = stringResource(id = currentOrientation.nameResId),
                    onClick = {
                        orientation = currentOrientation
                        showSexualOrientationOptions = false
                    }
                )
            }
        }
    }

    AnimatedComponent(visible = showSexualGenderOptions) {
        OptionsListScaffold(
            modifier = Modifier
                .fillMaxSize(),
            title = stringResource(R.string.selected_gender),
            subtitle = stringResource(R.string.selected_gender_subtitle),
            onBack = { showSexualGenderOptions = false },
        ) {
            val list = SexualGender.entries
            items(list.size) { index ->
                val currentGender = list[index]
                val isChecked = currentGender == gender
                ItemOption(
                    modifier = Modifier.fillMaxSize(),
                    isChecked = isChecked,
                    name = stringResource(id = currentGender.nameResId),
                    onClick = {
                        gender = currentGender
                        showSexualGenderOptions = false
                    }
                )
            }
        }
    }
}



@Preview
@Composable
fun PreviewSexualIdentificationPage() {
    LeroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SexualIdentification(
                onBack = {},
                onNext = {}
            )
        }
    }
}