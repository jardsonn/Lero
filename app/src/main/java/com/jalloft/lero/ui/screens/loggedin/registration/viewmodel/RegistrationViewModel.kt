package com.jalloft.lero.ui.screens.loggedin.registration.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageException
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Photo
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.exception.FirebaseFirestoreExceptionHandler
import com.jalloft.lero.exception.FirebaseStorageExceptionHandler
import com.jalloft.lero.repositories.FirebaseAuthRepository
import com.jalloft.lero.repositories.FirebaseCloudStorage
import com.jalloft.lero.repositories.FirebaseFirestoreRepository
import com.jalloft.lero.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firestoreRepo: FirebaseFirestoreRepository,
    private val authRepo: FirebaseAuthRepository,
    private val storageRepo: FirebaseCloudStorage,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val firebaseUser by lazy { authRepo.firebaseUser() }

    var isLoadingUpdateOrEdit by mutableStateOf(false)
    var erroUpdateOrEdit by mutableStateOf<String?>(null)
    var isSuccessUpdateOrEdit by mutableStateOf(false)
    var userState by mutableStateOf<User?>(null)

    var isLoadedProfilePhoto by mutableStateOf(false)
    var isLoadedCollectionPhoto by mutableStateOf(false)
    var successCollectionPhoto by mutableStateOf<List<Photo>?>(null)
    var successProfilePhoto by mutableStateOf<Photo?>(null)

    init {
        updateUserState()
    }

    fun saveProfilePhoto(context: Context, photo: Photo?) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepo.saveProfilePhoto(photo, firebaseUser?.uid).collectLatest { state ->
                when (state) {
                    ResponseState.Loading -> {
                        isLoadedProfilePhoto = false
                        isLoadingUpdateOrEdit = true
                    }

                    is ResponseState.Success -> {
                        isLoadedProfilePhoto = true
                        successProfilePhoto = state.data
                        Timber.i("Foto do perfil ${state.data}")
                    }

                    is ResponseState.Failure -> {
//                        isLoadingProfilePhoto = false
                        val exceptionHandler = FirebaseStorageExceptionHandler(context)
                        if (state.exception != null && state.exception is StorageException) {
                            erroUpdateOrEdit = exceptionHandler.handleException(state.exception)
                        } else {
                            context.getString(R.string.error_firebase_generic_execption_null)
                        }
                    }
                }
            }

        }
    }

    fun savePhotos(context: Context, photos: List<Photo>) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepo.savePhotos(photos, firebaseUser?.uid).collectLatest { state ->
                when (state) {
                    ResponseState.Loading -> {
                        isLoadedCollectionPhoto = false
                        isLoadingUpdateOrEdit = true
                    }

                    is ResponseState.Success -> {
                        isLoadedCollectionPhoto = true
                        successCollectionPhoto = state.data
                    }

                    is ResponseState.Failure -> {
//                        isLoadingCollectionPhoto = false
                        val exceptionHandler = FirebaseStorageExceptionHandler(context)
                        if (state.exception != null && state.exception is StorageException) {
                            erroUpdateOrEdit = exceptionHandler.handleException(state.exception)
                        } else {
                            context.getString(R.string.error_firebase_generic_execption_null)
                        }
                    }
                }
            }
        }
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
                                context.getString(R.string.error_firebase_generic_execption_null)
                            }

                    }
                }
            }
        }
    }

}