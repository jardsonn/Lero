package com.jalloft.lero.ui.screens.loggedin.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Education
import com.jalloft.lero.data.domain.Work
import com.jalloft.lero.data.domain.enums.EducationLevel
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.ItemOption
import com.jalloft.lero.ui.components.NormalTextField
import com.jalloft.lero.ui.components.OptionsListScaffold
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.components.SelectableTextField
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.UserFields
import timber.log.Timber


@Composable
fun WorkEducationScreen(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    leroViewModel: LeroViewModel,
) {

    val context = LocalContext.current
    val user = leroViewModel.currentUser

    var profession by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var education by remember { mutableStateOf(Education()) }

    val hasEducation = education.level != null ||
            education.secondaryEducationInstitution != null ||
            education.higherEducationInstitution != null ||
            education.postGraduationInstitution != null

    LaunchedEffect(key1 = user, block = {
        if (user != null) {
            if (profession.trim().isEmpty()) {
                profession = user.work?.profission.orEmpty()
            }
            if (profession.trim().isEmpty()) {
                company = user.work?.company.orEmpty()
            }
            if (!hasEducation && user.education != null) {
                education = user.education
            }
        }
    })

    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            leroViewModel.clear()
            onNext()
        }
    })

    val isvalidSubmit = hasEducation ||
            profession.isNotEmpty() ||
            company.isNotEmpty()

    var showEducationLevelContent by remember { mutableStateOf(false) }

    RegisterScaffold(
        title = stringResource(R.string.work_and_education),
        subtitle = stringResource(R.string.work_and_education_subtitle),
        enabledSubmitButton = isvalidSubmit,
        onBack = onBack,
        onSkip = onNext,
        errorMessage = leroViewModel.erroUpdateOrEdit,
        onSubmit = {
            if (education != user?.education || profession != user.work?.profission || company != user.work.company) {
                val updates = mapOf(
                    UserFields.WORK to Work(profession, company),
                    UserFields.EDUCATION to education,
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