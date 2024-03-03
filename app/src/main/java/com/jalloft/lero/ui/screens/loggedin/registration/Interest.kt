package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.UserFields
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestScreen(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    leroViewModel: LeroViewModel,
) {
    val context = LocalContext.current
    val user = leroViewModel.currentUser

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val interests = remember { mutableStateListOf<Interests>() }
    var showInterestsOptions by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = user, block = {
        if (interests.isEmpty() && user != null) {
            interests.addAll(user.interests)
        }
    })

    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            leroViewModel.clear()
            onNext()
        }
    })

    RegisterScaffold(
        title = stringResource(R.string.what_are_you_looking_for),
        subtitle = stringResource(R.string.interests_subtitle),
        onBack = onBack,
        errorMessage = leroViewModel.erroUpdateOrEdit,
        onSubmit = {
            if (interests != user?.interests) {
                val updates = mapOf(
                    UserFields.INTERESTS to interests,
                )
                leroViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            } else {
                Timber.i("Dados não alterados e não atualizados")
                leroViewModel.clear()
                onNext()
            }
        },
        onSkip = onNext,
        isLoading = leroViewModel.isLoadingUpdateOrEdit
    ) {

        val text =
            if (interests.isEmpty()) stringResource(R.string.interests_field_subtitle) else interests.joinToString(
                ", "
            ) {
                context.getString(
                    it.nameResId
                )
            }

        SelectableTextField(
            label = stringResource(R.string.interests_field_title),
            text = text,
            maxLines = 2,
            hasSelected = interests.isNotEmpty(),
            onOpenOptios = { showInterestsOptions = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )
    }

    val interestsTemp = remember { mutableStateListOf<Interests>() }

    if (showInterestsOptions) {
        ModalBottomSheet(
            onDismissRequest = {
                showInterestsOptions = false
                interestsTemp.clear()
            },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.height_placeholder),
                onSave = {
                    showInterestsOptions = false
                    interests.clear()
                    interests.addAll(interestsTemp)
                },
                onBack = {
                    interestsTemp.clear()
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showInterestsOptions = false
                        }
                    }
                },
            ) {
                val list = Interests.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = interestsTemp.contains(currentOption)
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        checkBoxShape = RoundedCornerShape(5.dp),
                        name = stringResource(id = currentOption.nameResId),
                        onClick = {
                            if (interestsTemp.contains(currentOption)) {
                                interestsTemp.remove(currentOption)
                            } else {
                                interestsTemp.add(currentOption)
                            }
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = showInterestsOptions, block = {
        if (showInterestsOptions) {
            interestsTemp.clear()
            interestsTemp.addAll(interests)
        } else {
            interestsTemp.clear()
        }
    })
}