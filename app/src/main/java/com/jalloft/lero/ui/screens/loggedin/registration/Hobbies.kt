package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalloft.lero.R
import com.jalloft.lero.ui.components.NormalTextField
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.screens.loggedin.registration.viewmodel.RegistrationViewModel
import com.jalloft.lero.util.DataValidator
import com.jalloft.lero.util.UserFields
import timber.log.Timber


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HobbiesScreen(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    registrationViewModel: RegistrationViewModel,
) {
    val context = LocalContext.current
    val user = registrationViewModel.userState

    var value by remember { mutableStateOf("") }
    val hobbies = remember { mutableStateListOf<String>() }

    LaunchedEffect(key1 = user, block = {
        if (user != null && hobbies.isEmpty()) {
            hobbies.addAll(user.hobbies)
        }
    })


    LaunchedEffect(key1 = registrationViewModel.isSuccessUpdateOrEdit, block = {
        if (registrationViewModel.isSuccessUpdateOrEdit) {
            onNext()
            registrationViewModel.clear()
        }
    })


    RegisterScaffold(
        title = stringResource(R.string.hobbies_and_interests),
        subtitle = stringResource(R.string.hobbies_and_interests_subtitle),
        onBack = onBack,
        errorMessage = registrationViewModel.erroUpdateOrEdit,
        enabledSubmitButton = hobbies.isNotEmpty(),
        onSubmit = {
            if (hobbies != user?.hobbies) {
                val updates = mapOf(
                    UserFields.HOBBIES to hobbies,
                )
                registrationViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            } else {
                Timber.i("Dados não alterados e não atualizados")
                onNext()
            }
        },
        onSkip = onNext,
        isLoading = registrationViewModel.isLoadingUpdateOrEdit
    ) {
        NormalTextField(
            label = stringResource(R.string.your_hobbies_label),
            placeholder = stringResource(R.string.your_hobbies_palceholder),
            value = value,
            onValueChange = { if (it.trim().length <= 20) value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (value.trim().length > 1) {
                        hobbies.add(value)
                        value = ""
                    }
                }
            )
        )

        AnimatedVisibility(visible = hobbies.isNotEmpty()) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (hobbie in hobbies) {
                    InputChip(
                        selected = false,
                        onClick = { },
                        shape = CircleShape,
                        border = InputChipDefaults.inputChipBorder(
                            enabled = true,
                            selected = false,
                            borderWidth = 2.dp,
                            borderColor = MaterialTheme.colorScheme.onBackground.copy(.1f)
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(id = R.string.remove_o_hobbie),
                                modifier = Modifier.clickable { hobbies.remove(hobbie) }
                            )
                        },
                        label = {
                            Text(
                                text = hobbie,
                                fontSize = 14.sp
                            )
                        })
                }
            }
        }

    }
}

