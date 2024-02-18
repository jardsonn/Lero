package com.jalloft.lero.ui.screens.loggedout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.jalloft.lero.R
import com.jalloft.lero.exception.FirebaseAuthExceptionHandler
import com.jalloft.lero.ui.components.LeroButton
import com.jalloft.lero.ui.components.OtpTextField
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel
import com.jalloft.lero.ui.theme.LeroTheme
import com.jalloft.lero.util.ResponseState
import timber.log.Timber


@Composable
fun VerifyPhoneScreen(
    viewModel: LoggedOutViewModel,
    verificationId: String,
    phoneNumber: String,
    onAuthenticated: (FirebaseUser?) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val exceptionHandler = remember { FirebaseAuthExceptionHandler(context) }
    var code by remember { mutableStateOf("") }
    var signInLoading by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf<String?>(null) }
    var signInFailure by remember { mutableStateOf(false) }
    var newVerificationId by remember { mutableStateOf(verificationId) }

    LaunchedEffect(key1 = viewModel.signInWithPhoneSuccess, block = {
        if (viewModel.signInWithPhoneSuccess == null) {
            Timber.i("SignInWithPhone::Success autenticado")
            onAuthenticated(null)
        } else {
            Timber.i("SignInWithPhone::Success confirmar codigo - ${viewModel.signInWithPhoneSuccess?.first}")
            viewModel.signInWithPhoneSuccess?.first?.let { newVerificationId = it }
        }
    })

    LaunchedEffect(key1 = viewModel.signInWithPhoneCredentialResponse, block = {
        when (val response = viewModel.signInWithPhoneCredentialResponse) {
            ResponseState.Loading -> {
                signInFailure = false
                signInLoading = true
                Timber.i("SignInWithPhone::Loading")
            }

            is ResponseState.Success -> {
                signInLoading = false
                onAuthenticated(response.data)
                Timber.i("SignInWithPhone::Success autenticado")
            }

            is ResponseState.Failure -> {
                signInLoading = false
                signInFailure = true
                messageError =
                    if (response.exception != null && response.exception is FirebaseAuthException) {
                        exceptionHandler.handleException(response.exception)
                    } else {
                        context.getString(R.string.error_generic_execption_null)
                    }
                Timber.i("SignInWithPhone::Failure ${response.exception?.message}")
            }

            else -> {}
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }

        Text(
            text = "Confirme o código",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Digite o código de 6 dígitos que enviamos para você.",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OtpTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            otpText = code,
            isError = signInFailure,
            onOtpTextChange = { value, _ ->
                code = value
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                messageError = null
                signInFailure = false
                viewModel.signInWithPhone(context, phoneNumber)
            }, enabled = !viewModel.isTimerRunning) {
                Text(text = if (viewModel.isTimerRunning) "Reenviar código em ${viewModel.secondsRemaining} segundos" else "Reenviar código")
            }
        }

        AnimatedVisibility(visible = !messageError.isNullOrEmpty()) {
            Text(
                text = messageError.orEmpty(),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        LeroButton(
            modifier = Modifier.fillMaxWidth(),
            content = {
                if (signInLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 16.dp, top = 8.dp),
                        strokeCap = StrokeCap.Round,
                        color = MaterialTheme.colorScheme.primary.copy(.2f)
                    )
                }
                Text(text = "Confirmar")
            },
            enabled = !signInLoading && code.isNotEmpty(),
            onClick = {
                signInFailure = false
                messageError = null
                viewModel.signInWithPhoneAuthCredential(newVerificationId, code)
            },
        )
    }
    DisposableEffect(key1 = Unit, effect = {
        onDispose { viewModel.clearSignInResponses() }
    })
}


@Preview
@Composable
fun PreviewVerifyCode() {
    LeroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var code by remember { mutableStateOf("") }
            OtpTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                otpText = code,
                onOtpTextChange = { value, _ ->
                    code = value
                }
            )
        }
    }
}
