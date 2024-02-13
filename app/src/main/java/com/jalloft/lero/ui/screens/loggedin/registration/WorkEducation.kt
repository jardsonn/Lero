package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Education
import com.jalloft.lero.data.domain.enums.EducationLevel
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.NormalTextField
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField


@Composable
fun WorkEducationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var profession by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var education by remember { mutableStateOf(Education()) }

    val isvalidSubmit =
        education.level != null ||
                education.secondaryEducationInstitution != null ||
                education.higherEducationInstitution != null ||
                education.postGraduationInstitution != null ||
                profession.isNotEmpty() ||
                company.isNotEmpty()

    var isLoading by remember { mutableStateOf(false) }
    var showEducationLevelContent by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.work_and_education),
        subtitle = stringResource(R.string.work_and_education_subtitle),
        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSkip = onNext,
        onSubmit = {},
        isLoading = isLoading
    ) {
        NormalTextField(
            label = stringResource(R.string.profission),
            placeholder = stringResource(R.string.profission_placeholder),
            value = profession,
            onValueChange = { profession = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
        )
        NormalTextField(
            label = stringResource(R.string.company),
            placeholder = stringResource(R.string.company_placeholder),
            value = company,
            onValueChange = { company = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
        )

        NormalTextField(
            label = stringResource(R.string.secondary_education_institution),
            placeholder = stringResource(R.string.secondary_education_institution_placeholder),
            value = education.secondaryEducationInstitution.orEmpty(),
            onValueChange = { education = education.copy(secondaryEducationInstitution = it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
        )

        NormalTextField(
            label = stringResource(R.string.higher_education_institution),
            placeholder = stringResource(R.string.higher_education_institution_placeholder),
            value = education.higherEducationInstitution.orEmpty(),
            onValueChange = { education = education.copy(higherEducationInstitution = it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
        )

        NormalTextField(
            label = stringResource(R.string.post_graduation_institution),
            placeholder = stringResource(R.string.post_graduation_institution_placeholder),
            value = education.postGraduationInstitution.orEmpty(),
            onValueChange = { education = education.copy(postGraduationInstitution = it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
        )

        SelectableTextField(
            label = stringResource(R.string.education_level),
            text = education.level?.nameResId?.let { stringResource(id = it) }
                ?: stringResource(R.string.education_level_placeholder),
            onOpenOptios = { showEducationLevelContent = true },
            hasSelected = education.level != null
        )
    }

    AnimatedComponent(visible = showEducationLevelContent) {
        OptionsListScaffold(
            modifier = Modifier.fillMaxSize(),
            title = stringResource(R.string.education_level),
            subtitle = stringResource(R.string.select_education_level),
            onBack = { showEducationLevelContent = false },
        ) {
            val list = EducationLevel.entries
            items(list.size) { index ->
                val currentEducationLevel = list[index]
                val isChecked = currentEducationLevel == education.level
                ItemOption(
                    modifier = Modifier.fillMaxSize(),
                    isChecked = isChecked,
                    name = stringResource(id = currentEducationLevel.nameResId),
                    onClick = {
                        education = education.copy(level = currentEducationLevel)
                        showEducationLevelContent = false
                    }
                )
            }
        }
    }
}