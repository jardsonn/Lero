package com.jalloft.lero.repositories

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.Flow


interface FirebaseAuthRepository {

    fun signOut()

    fun isAuthenticated(): Boolean

    suspend fun reauthenticate(credential: AuthCredential): Flow<ResponseState<Unit>>

    suspend fun deleteUser(): Flow<ResponseState<Unit>>

    suspend fun signInWithGoogle(idToken: String): Flow<ResponseState<Boolean>>

    fun signInWithPhone(
        activity: Activity,
        number: String,
        onStartVerification: () -> Unit,
        signInResponse: (ResponseState<Pair<String, PhoneAuthProvider.ForceResendingToken>?>) -> Unit,

        )

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        signInResponse: (ResponseState<FirebaseUser?>) -> Unit,
    )

}