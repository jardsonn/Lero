package com.jalloft.lero.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.jalloft.lero.data.domain.City
import com.jalloft.lero.data.domain.Education
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.data.domain.Work
import com.jalloft.lero.util.ResponseState
import com.jalloft.lero.util.USERS
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named


class FirebaseFirestoreRepositoryImpl @Inject constructor(
    @Named(USERS) private val usersRef: CollectionReference,
) : FirebaseFirestoreRepository {

    override suspend fun saveNewUser(userId: String, user: User) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).set(user).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("saveUserData::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataNameAndDateOfBirth(
        userId: String,
        name: String,
        dateOfBirth: Date
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).update("name", name, "dateOfBirth", Timestamp(dateOfBirth))
                .await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataNameAndDateOfBirth::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataGenderAndOrientation(
        userId: String,
        gender: SexualGender,
        orientation: SexualOrientation
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).update("gender", gender, "orientation", orientation).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataGenderAndOrientation::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataCity(
        userId: String,
        city: City
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).update("city", city).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataCity::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataInterests(
        userId: String,
        interests: List<Interests>
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).update("interests", interests).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataInterests::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataWorkAndEducation(
        userId: String,
        work: Work,
        education: Education
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId).update("work", work, "education", education).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataWorkAndEducation::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataLifeDetails(
        userId: String,
        isDrinker: Boolean,
        isSmoker: Boolean,
        hasChildren: Boolean,
        religion: String
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId)
                .update(
                    "isDrinker",
                    isDrinker,
                    "isSmoker",
                    isSmoker,
                    "hasChildren",
                    hasChildren,
                    "religion",
                    religion
                )
                .await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataLifeDetails::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataHobbies(
        userId: String,
        hobbies: List<String>
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId)
                .update("hobbies", hobbies)
                .await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataHobbies::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataBio(
        userId: String,
        bio: String
    ) = flow {
        try {
            emit(ResponseState.Loading)
            usersRef.document(userId)
                .update("bio", bio)
                .await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataBio::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }
}
