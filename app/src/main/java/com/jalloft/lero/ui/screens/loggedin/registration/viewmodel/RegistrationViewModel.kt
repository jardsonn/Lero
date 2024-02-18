package com.jalloft.lero.ui.screens.loggedin.registration.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.exception.FirebaseAuthExceptionHandler
import com.jalloft.lero.exception.FirebaseFirestoreExceptionHandler
import com.jalloft.lero.repositories.FirebaseAuthRepository
import com.jalloft.lero.repositories.FirebaseFirestoreRepository
import com.jalloft.lero.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firestoreRepo: FirebaseFirestoreRepository,
    private val authRepo: FirebaseAuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

//    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val firebaseUser by lazy { authRepo.firebaseUser() }

    var isLoadingUpdateOrEdit by mutableStateOf(false)
    var erroUpdateOrEdit by mutableStateOf<String?>(null)
    var isSuccessUpdateOrEdit by mutableStateOf(false)

    //    var userUiState by mutableStateOf<ResponseState<User?>>(ResponseState.Loading)
    var userState by mutableStateOf<User?>(null)

    init {
        updateUserState()
    }

    private fun updateUserState() {
        firebaseUser?.uid?.let {
            firestoreRepo.addUserSnapshotListener(it) { state ->
                when (state) {
                    ResponseState.Loading -> {
                        Timber.i("updateUserState::Carregando dados do usuário")
                    }
                    is ResponseState.Success -> {
                        userState = state.data
                        Timber.i("updateUserState::Dados do usuário carreagdo = ${state.data}")
                    }
                    is ResponseState.Failure -> {
                        Timber.i("updateUserState::Ocorreu um erro ao obter o usuário: ${state.exception?.message}")
                    }
                }
            }
        }
    }

    fun clear() {
        isLoadingUpdateOrEdit = false
        erroUpdateOrEdit = null
        isSuccessUpdateOrEdit = false
    }

    fun updateOrEdit(context: Context, updates: Map<String, Any?>) {
        val exceptionHandler = FirebaseFirestoreExceptionHandler(context)
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepo.updateOrEdit(firebaseUser?.uid, updates).collectLatest { response ->
                when (response) {
                    ResponseState.Loading -> {
                        isLoadingUpdateOrEdit = true
                        isSuccessUpdateOrEdit = false
                        erroUpdateOrEdit = null
                    }

                    is ResponseState.Success -> {
                        isLoadingUpdateOrEdit = false
                        isSuccessUpdateOrEdit = true
                    }

                    is ResponseState.Failure -> {
                        isLoadingUpdateOrEdit = false
                        erroUpdateOrEdit =
                            if (response.exception != null && response.exception is FirebaseFirestoreException) {
                                exceptionHandler.handleException(response.exception)
                            } else {
                                context.getString(R.string.error_firestore_generic_execption_null)
                            }

                    }
                }
            }
        }
    }

}