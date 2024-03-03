package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.DialogInformation
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.UserFields
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexualIdentification(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    leroViewModel: LeroViewModel,
) {
    val context = LocalContext.current

    val user = leroViewModel.currentUser

    var gender by remember { mutableStateOf(user?.gender) }
    var orientation by remember { mutableStateOf(user?.orientation) }

    LaunchedEffect(key1 = user, block = {
        if (user != null) {
            if (gender == null) {
                gender = user.gender
            }
            if (orientation == null) {
                orientation = user.orientation
            }
        }
    })

    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            leroViewModel.clear()
            onNext()
        }
    })

    val isvalidSubmit = gender != null && orientation != null

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
            if (gender != user?.gender || orientation != user?.orientation) {
                val updates = mapOf(
                    UserFields.GENDER to gender,
                    UserFields.ORIENTATION to orientation,
                )
                leroViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            } else {
                Timber.i("Dados não alterados e não atualizados")
                leroViewModel.clear()
                onNext()
            }
        },
        errorMessage = leroViewModel.erroUpdateOrEdit,
        isLoading = leroViewModel.isLoadingUpdateOrEdit
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


//@Preview
//@Composable
//fun PreviewSexualIdentificationPage() {
//    LeroTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            SexualIdentification(
//                onBack = {},
//                onNext = {}
//            )
//        }
//    }
//}