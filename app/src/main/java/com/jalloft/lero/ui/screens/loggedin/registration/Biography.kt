package com.jalloft.lero.ui.screens.loggedin.registration

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.screens.loggedin.registration.viewmodel.RegistrationViewModel
import com.jalloft.lero.util.DataValidator
import com.jalloft.lero.util.MANDATORY_DATA_SAVED
import com.jalloft.lero.util.UserFields
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun BiographyScreen(
    onBack: (() -> Unit)?,
    onDone: () -> Unit,
    registrationViewModel: RegistrationViewModel,
) {

    val context = LocalContext.current
    val user = registrationViewModel.userState
    var bio by remember { mutableStateOf("") }

    LaunchedEffect(key1 = user, block = {
        if (user != null && bio.trim().isEmpty()) {
            bio = user.bio.orEmpty()
        }
    })

    LaunchedEffect(key1 = registrationViewModel.isSuccessUpdateOrEdit, block = {
        if (registrationViewModel.isSuccessUpdateOrEdit) {
            onDone()
            Hawk.put(MANDATORY_DATA_SAVED, true)
            registrationViewModel.clear()
        }
    })


    val maxBioLength = 500

    RegisterScaffold(
        title = stringResource(R.string.biography_title),
        subtitle = stringResource(R.string.biography_subtitle),
        onBack = onBack,
        textButton = stringResource(R.string.done),
        onSubmit = {
            if (bio != user?.bio) {
                val updates = mapOf(
                    UserFields.BIO to bio,
                )
                registrationViewModel.updateOrEdit(context, updates)
                Timber.i("Dados atualizados")
            } else {
                Timber.i("Dados não alterados e não atualizados")
                onDone()
                Hawk.put(MANDATORY_DATA_SAVED, true)
            }
        },
        onSkip = {
            Hawk.put(MANDATORY_DATA_SAVED, true)
            onDone()
        },
        errorMessage = registrationViewModel.erroUpdateOrEdit,
        isLoading = registrationViewModel.isLoadingUpdateOrEdit
    ) {
        Text(
            text = stringResource(R.string.bio),
            style = MaterialTheme.typography.titleMedium,
        )

        val scrollState = rememberScrollState()
        BasicTextField(
            value = bio,
            onValueChange = { if (bio.length < maxBioLength) bio = it },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 200.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground.copy(.1f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(12.dp)
                ) {
                    if (bio.isEmpty()) {
                        Text(
                            text = stringResource(R.string.bio_placeholder),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                        )
                    }
                    it()
                }
            }
        }
        Text(
            text = "${bio.length}/$maxBioLength",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.End)
        )
    }
}
