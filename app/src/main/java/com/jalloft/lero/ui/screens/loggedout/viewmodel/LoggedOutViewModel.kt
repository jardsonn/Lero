package com.jalloft.lero.ui.screens.loggedout.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.PhoneAuthProvider
import com.jalloft.lero.R
import com.jalloft.lero.exception.FirebaseAuthExceptionHandler
import com.jalloft.lero.repositories.FirebaseAuthRepository
import com.jalloft.lero.util.CommonUtil.findActivity
import com.jalloft.lero.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val TIMER_DURATION_SECONDS = 60
const val MILLIS_PER_SECOND = 1000

@HiltViewModel
class LoggedOutViewModel @Inject constructor(
    private val repo: FirebaseAuthRepository
) : ViewModel() {


    //    var signInWithPhoneResponse by mutableStateOf<Response<Pair<String, PhoneAuthProvider.ForceResendingToken>?>?>(null)
    var signInWithPhoneCredentialResponse by mutableStateOf<ResponseState<Unit>?>(null)

    var signInWithPhoneLoading by mutableStateOf(false)
    var signInWithPhoneFailure by mutableStateOf<String?>(null)
    var signInWithPhoneSuccess by mutableStateOf<Pair<String?, PhoneAuthProvider.ForceResendingToken?>?>(
        Pair(null, null)
    )


    var secondsRemaining by mutableIntStateOf(0)
    var isTimerRunning by mutableStateOf(false)

    val isAuthenticated by lazy { repo.isAuthenticated() }

    fun signInWithGoogle(token: String) {
        viewModelScope.launch {
            repo.signInWithGoogle(token).collect {

            }
        }
    }

    fun signInWithPhone(context: Context, number: String) {
        val exceptionHandler = FirebaseAuthExceptionHandler(context)
        if (!isTimerRunning) {
            secondsRemaining = TIMER_DURATION_SECONDS
            isTimerRunning = true
        }
        val activity = context.findActivity()
        viewModelScope.launch {
            repo.signInWithPhone(activity, number, onStartVerification = {
                launch {
                    if (isTimerRunning) {
                        repeat(TIMER_DURATION_SECONDS) {
                            delay(MILLIS_PER_SECOND.toLong())
                            secondsRemaining = TIMER_DURATION_SECONDS - it - 1
                        }
                        isTimerRunning = false
                    }
                }
            }) { signInWithPhoneResponse ->
                when (signInWithPhoneResponse) {
                    ResponseState.Loading -> {
                        signInWithPhoneLoading = true
                        signInWithPhoneFailure = null
                        Timber.i("SignInWithPhone::Loading")
                    }

                    is ResponseState.Success -> {
                        signInWithPhoneLoading = false
                        signInWithPhoneFailure = null
                        signInWithPhoneSuccess = signInWithPhoneResponse.data
                    }

                    is ResponseState.Failure -> {
                        signInWithPhoneLoading = false
                        val message = with(signInWithPhoneResponse) {
                            if (exception != null && exception is FirebaseAuthException) {
                                exceptionHandler.handleException(exception)
                            } else {
                                context.getString(R.string.error_generic_execption_null)
                            }
                        }
                        signInWithPhoneFailure = message
                        Timber.i("SignInWithPhone::Failure ${signInWithPhoneResponse.exception?.message}")
                    }
                }
            }
        }
    }

    fun signInWithPhoneAuthCredential(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneCredentialResponse = ResponseState.Loading
        viewModelScope.launch {
            repo.signInWithPhoneAuthCredential(credential) { response ->
                signInWithPhoneCredentialResponse = when (response) {
                    is ResponseState.Success -> {
                        ResponseState.Success(Unit)
                    }

                    is ResponseState.Failure -> {
                        ResponseState.Failure(response.exception)
                    }

                    else -> {
                        throw IllegalArgumentException("Unexpected response type: $response")
                    }
                }
            }
        }
    }

    fun clearSignInResponses() {
        signInWithPhoneLoading = false
        signInWithPhoneFailure = null
        signInWithPhoneSuccess = Pair(null, null)
    }

}