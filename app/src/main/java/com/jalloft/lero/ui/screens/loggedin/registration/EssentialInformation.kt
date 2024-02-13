package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Height
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.NormalTextField
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.theme.LeroTheme
import com.jalloft.lero.util.DataValidator.INVALID_DATE_FORMT
import com.jalloft.lero.util.DataValidator.INVALID_DATE_MINOR
import com.jalloft.lero.util.DataValidator.VALID_DATE
import com.jalloft.lero.util.DataValidator.isBirthDateValid
import com.jalloft.lero.util.TextFieldFilter.date
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EssentialInformationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var height by remember { mutableStateOf<Height?>(null) }
    var showHeightOptions by remember { mutableStateOf(false) }

    val isvalidSubmit =
        (name.isNotEmpty() && name.length > 3 && name.length < 30) && isBirthDateValid(dateOfBirth) == VALID_DATE && height != null

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isLoading, block = {
        if (isLoading) {
            delay(3000)
            isLoading = false
            onNext()
        }
    })

    RegisterScaffold(
        title = stringResource(R.string.seu_nome_e_sua_idade),
        subtitle = stringResource(R.string.informe_seu_nome_e_sua_data_de_nascimento),
        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSubmit = { isLoading = true },
        isLoading = isLoading
    ) {

        NormalTextField(
            value = name,
            label = stringResource(R.string.name_label),
            placeholder = stringResource(R.string.name_placeholder),
            onValueChange = { name = it },
            errorMessage = if (name.isEmpty())
                stringResource(R.string.please_type_your_name) else if (name.length < 3)
                stringResource(R.string.minimun_character_name) else if (name.length > 30)
                stringResource(R.string.maximun_character_name)
            else null,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        NormalTextField(
            value = dateOfBirth,
            label = stringResource(R.string.date_of_birth_label),
            placeholder = stringResource(R.string.date_of_birth_placeholder),
            closeKeyboad = dateOfBirth.length == 8,
            onValueChange = { if (it.isDigitsOnly() && it.length <= 8) dateOfBirth = it },
            errorMessage = if (isBirthDateValid(dateOfBirth) == INVALID_DATE_FORMT)
                stringResource(R.string.date_format_invalid)
            else if (isBirthDateValid(dateOfBirth) == INVALID_DATE_MINOR)
                stringResource(
                    R.string.date_invalid_minor
                ) else null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            visualTransformation = VisualTransformation {
                return@VisualTransformation date(it)
            }
        )

        SelectableTextField(
            label = stringResource(R.string.height),
            text = height?.value ?: stringResource(R.string.height_placeholder),
            hasSelected = height != null,
            onOpenOptios = { showHeightOptions = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )

    }

    if (showHeightOptions) {
        ModalBottomSheet(
            onDismissRequest = { showHeightOptions = false },
            shape = BottomSheetDefaults.HiddenShape,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            OptionsListScaffold(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.height_placeholder),
                onBack = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showHeightOptions = false
                        }
                    }
                },
            ) {
                val list = Height.entries
                items(list.size) { index ->
                    val currentOption = list[index]
                    val isChecked = currentOption == height
                    ItemOption(
                        modifier = Modifier.fillMaxSize(),
                        isChecked = isChecked,
                        name = when (currentOption) {
                            Height.LESS_THAN_91 -> stringResource(R.string.menor_que_91_cm)
                            Height.BIGGER_THEN -> stringResource(R.string.maior_que_214_cm)
                            else -> currentOption.value
                        },
                        onClick = {
                            height = currentOption
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showHeightOptions = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewPage() {
    LeroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EssentialInformationScreen(
                onBack = {},
                onNext = {}
            )
        }
    }
}


