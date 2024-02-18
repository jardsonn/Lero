package com.jalloft.lero.repositories

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) :
    FirebaseAuthRepository {
    override fun signOut() {
        auth.signOut()
    }

    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun firebaseUser() = auth.currentUser


    override suspend fun reauthenticate(credential: AuthCredential) = flow {
        try {
            emit(ResponseState.Loading)
            auth.currentUser?.reauthenticate(credential)?.await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("reauthenticate:failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }


    override suspend fun deleteUser() = flow {
        try {
            emit(ResponseState.Loading)
            auth.currentUser?.delete()?.await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("deleteUser:failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun signInWithGoogle(idToken: String) = flow {
        try {
            emit(ResponseState.Loading)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.additionalUserInfo?.apply {
//                if (isNewUser) {
//                    createUserInFirestore(FieldValue.serverTimestamp()).collect {
//                        if (it !is Response.Loading) {
//                            emit(Response.Success(isNewUser))
//                        }
//                    }
//                } else {
//                    emit(Response.Success(isNewUser))
//                }
            }
        } catch (e: Exception) {
            Timber.w("signInWithGoogle:failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }


    override fun signInWithPhone(
        activity: Activity,
        number: String,
        onStartVerification: () -> Unit,
        signInResponse: (ResponseState<Pair<String, PhoneAuthProvider.ForceResendingToken>?>) -> Unit,
    ) {
//        val profileUpdates = UserProfileChangeRequest.Builder()
//            .setDisplayName("Novo Nome de Usuário")
//            .build()
        signInResponse(ResponseState.Loading)
        auth.useAppLanguage()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential) { response ->
                        when (response) {
                            is ResponseState.Success -> {
                                signInResponse(ResponseState.Success(null))
                            }

                            is ResponseState.Failure -> {
                                signInResponse(ResponseState.Failure(response.exception))
                            }

                            else -> {
                                throw IllegalArgumentException("Unexpected response type: $response")
                            }
                        }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    signInResponse(ResponseState.Failure(e))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    signInResponse(ResponseState.Success(Pair(verificationId, token)))
                }
            })
            .build()
        onStartVerification()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        signInResponse: (ResponseState<FirebaseUser?>) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signInResponse(ResponseState.Success(task.result?.user))
                } else {
                    signInResponse(ResponseState.Failure(task.exception))
                }
            }
    }

}