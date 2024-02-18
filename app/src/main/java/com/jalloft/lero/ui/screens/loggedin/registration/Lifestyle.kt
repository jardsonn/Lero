package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.enums.Children
import com.jalloft.lero.data.domain.enums.Drinker
import com.jalloft.lero.data.domain.enums.Religion
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.data.domain.enums.Smoker
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.screens.loggedin.registration.viewmodel.RegistrationViewModel
import com.jalloft.lero.util.DataValidator
import com.jalloft.lero.util.UserFields
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifestyleScreen(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    registrationViewModel: RegistrationViewModel,
) {

    val context = LocalContext.current
    val user = registrationViewModel.userState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var smoker by remember { mutableStateOf<Smoker?>(null) }
    var drinker by remember { mutableStateOf<Drinker?>(null) }
    var children by remember { mutableStateOf<Children?>(null) }
    var religion by remember { mutableStateOf<Religion?>(null) }

    LaunchedEffect(key1 = user, block = {
        if (user != null) {
            if (smoker == null) {
                smoker = user.smoker
            }
            if (drinker == null) {
                drinker = user.drinker
            }
            if (children == null) {
                children = user.children
            }
            if (religion == null) {
                religion = user.religion
            }
        }
    })

    LaunchedEffect(key1 = registrationViewModel.isSuccessUpdateOrEdit, block = {
        if (registrationViewModel.isSuccessUpdateOrEdit) {
            onNext()
            registrationViewModel.clear()
        }
    })


    var showChildrenOptions by remember { mutableStateOf(false) }
    var showDrinkerOptions by remember { mutableStateOf(false) }
    var showSmokerOptions by remember { mutableStateOf(false) }
    var showReligionOptions by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.lifestyle),
        subtitle = stringResource(R.string.tell_about_your_lifistyle),
//        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSkip = onNext,
        errorMessage = registrationViewModel.erroUpdateOrEdit,
        onSubmit = {
            if (smoker != user?.smoker || drinker != user?.drinker || children != user?.children || religion != user?.religion) {
                val updates = mapOf(
                    UserFields.SMOKER to smoker,
                    UserFields.DRINKER to drinker,
                    UserFields.CHILDREN to children,
                    UserFields.RELIGION to religion,
                )
                registrationViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            }else{
                Timber.i("Dados não alterados e não atualizados")
                onNext()
            }
        },
        isLoading = registrationViewModel.isLoadingUpdateOrEdit
    ) {

        SelectableTextField(
            label = stringResource(R.string.children),
            text = children?.nameResId?.let { stringResource(it) }
                ?: stringResource(R.string.you_have_children),
            onOpenOptios = { showChildrenOptions = true },
            hasSelected = children != null,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SelectableTextField(
            label = stringResource(R.string.smoker),
            text = smoker?.nameResId?.let { stringResource(it) }
                ?: stringResource(R.string.smoker_placeholder),
            onOpenOptios = { showSmokerOptions = true },
            hasSelected = smoker != null,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        SelectableTextField(
            label = stringResource(R.string.drinker),
            text = drinker?.nameResId?.let { stringResource(it) }
                ?: stringResource(R.string.drinker_placeholder),
            onOpenOptios = { showDrinkerOptions = true },
            hasSelected = drinker != null,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        SelectableTextField(
            label = stringResource(R.string.religion),
            text = religion?.nameResId?.let { stringResource(it) }
                ?: stringResource(R.string.religion_placeholder),
            onOpenOptios = { showReligionOptions = true },
            hasSelected = religion != null,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }

    if (showChildrenOptions) {
        ModalBottomSheet(
            onDismissRequest = { showChildrenOptions = false },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.you_have_children),
                onBack = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showChildrenOptions = false
                        }
                    }
                },
            ) {
                val list = Children.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = currentOption == children
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        name = stringResource(id = currentOption.nameResId),
                        onClick = {
                            children = currentOption
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showChildrenOptions = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showSmokerOptions) {
        ModalBottomSheet(
            onDismissRequest = { showSmokerOptions = false },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.smoker_placeholder),
                onBack = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showSmokerOptions = false
                        }
                    }
                },
            ) {
                val list = Smoker.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = currentOption == smoker
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        name = stringResource(id = currentOption.nameResId),
                        onClick = {
                            smoker = currentOption
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showSmokerOptions = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }


    if (showDrinkerOptions) {
        ModalBottomSheet(
            onDismissRequest = { showDrinkerOptions = false },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.drinker_placeholder),
                onBack = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showDrinkerOptions = false
                        }
                    }
                },
            ) {
                val list = Drinker.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = currentOption == drinker
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        name = stringResource(id = currentOption.nameResId),
                        onClick = {
                            drinker = currentOption
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showDrinkerOptions = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showReligionOptions) {
        ModalBottomSheet(
            onDismissRequest = { showReligionOptions = false },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.religion_placeholder),
                onBack = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showReligionOptions = false
                        }
                    }
                },
            ) {
                val list = Religion.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = currentOption == religion
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        name = stringResource(id = currentOption.nameResId),
                        onClick = {
                            religion = currentOption
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showReligionOptions = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}