package com.jalloft.lero.ui.screens.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
class LeroViewModel @Inject constructor(
    private val firestoreRepo: FirebaseFirestoreRepository,
    private val authRepo: FirebaseAuthRepository,
    private val storageRepo: FirebaseCloudStorage,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    var isLoadingUpdateOrEdit by mutableStateOf(false)
    var erroUpdateOrEdit by mutableStateOf<String?>(null)
    var isSuccessUpdateOrEdit by mutableStateOf(false)

    var currentUser by mutableStateOf<User?>(null)
    var isLoadingCurrentUser by mutableStateOf(false)
    var isErrorLoadUser by mutableStateOf(false)

    var isLoadedProfilePhoto by mutableStateOf(false)
    var isLoadedCollectionPhoto by mutableStateOf(false)
    var successCollectionPhoto by mutableStateOf<List<Photo>?>(null)
    var successProfilePhoto by mutableStateOf<Photo?>(null)

    private val authStateListener: FirebaseAuth.AuthStateListener =
        FirebaseAuth.AuthStateListener { auth ->
            updateUserState(auth.currentUser?.uid)
        }

    val distances by lazy { firestoreRepo.getDistances() }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }


    fun saveProfilePhoto(context: Context, photo: Photo?) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepo.saveProfilePhoto(photo, firebaseAuth.currentUser?.uid)
                .collectLatest { state ->
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
        Timber.i("Fotos para salvar $photos")
        viewModelScope.launch(Dispatchers.IO) {
            storageRepo.savePhotos(photos, firebaseAuth.currentUser?.uid).collectLatest { state ->
                when (state) {
                    ResponseState.Loading -> {
                        isLoadedCollectionPhoto = false
                        isLoadingUpdateOrEdit = true
                    }

                    is ResponseState.Success -> {
                        isLoadedCollectionPhoto = true
                        isLoadingUpdateOrEdit = false
                        if (state.data.isNotEmpty()) {
                            successCollectionPhoto = state.data
                        }
                    }

                    is ResponseState.Failure -> {
                        isLoadingUpdateOrEdit = false
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

    private fun updateUserState(userUid: String?) {
        if (userUid != null) {
            firestoreRepo.addUserSnapshotListener(userUid) { state ->
                when (state) {
                    ResponseState.Loading -> {
                        isLoadingCurrentUser = true
                        isErrorLoadUser = false
                        Timber.i("updateUserState::Carregando dados do usuário")
                    }

                    is ResponseState.Success -> {
                        isLoadingCurrentUser = false
                        isErrorLoadUser = false
                        currentUser = state.data
                        Timber.i("updateUserState::Dados do usuário carreagdo = ${state.data}")
                    }

                    is ResponseState.Failure -> {
                        isLoadingCurrentUser = false
                        isErrorLoadUser = true
                        Timber.i("updateUserState::Ocorreu um erro ao obter o usuário: ${state.exception?.message}")
                    }
                }
            }
        } else {
            isLoadingCurrentUser = false
            isErrorLoadUser = true
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
            firestoreRepo.updateOrEdit(firebaseAuth?.uid, updates).collectLatest { response ->
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

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

}