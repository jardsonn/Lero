package com.jalloft.lero.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.core.UserData
import com.google.firebase.firestore.toObject
import com.jalloft.lero.data.domain.Choice
import com.jalloft.lero.data.domain.City
import com.jalloft.lero.data.domain.DatingPreferences
import com.jalloft.lero.data.domain.Education
import com.jalloft.lero.data.domain.Height
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.data.domain.Work
import com.jalloft.lero.util.PREFERENCES_DISTANCE
import com.jalloft.lero.util.PREFERENCES_PATH
import com.jalloft.lero.util.ResponseState
import com.jalloft.lero.util.USERS
import com.jalloft.lero.util.UserFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named


class FirebaseFirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(USERS) private val usersRef: CollectionReference,
) : FirebaseFirestoreRepository {

    override suspend fun updateOrEdit(userId: String?, updates: Map<String, Any?>) = flow {
        emit(ResponseState.Loading)
        val userRef = userId?.let { usersRef.document(it) }
        try {

            if (userRef == null) {
                emit(ResponseState.Failure())
                return@flow
            }
            userRef.set(updates, SetOptions.merge()).await()
            Timber.i("User data updated successfully")
            emit(ResponseState.Success(Unit))
        } catch (e: FirebaseFirestoreException) {
            Timber.w("Error updating user data: ${e.message}")
            emit(ResponseState.Failure(e, e.message))
        }
    }

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

    override suspend fun updateUserEssentialInformation(
        userId: String,
        name: String?,
        dateOfBirth: Date?,
        height: Height?,
    ) = flow {
        try {
            emit(ResponseState.Loading)
            val userRef = usersRef.document(userId)
            val snapshot = userRef.get().await()
            if (snapshot.exists()) {
                userRef.update("name", name, "dateOfBirth", dateOfBirth?.let { Timestamp(it) })
                    .await()
            } else {
                userRef.set(
                    User(
                        name = name,
                        dateOfBirth = dateOfBirth?.let { Timestamp(it) },
                        height = height
                    )
                ).await()
            }
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataNameAndDateOfBirth::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataGenderAndOrientation(
        userId: String,
        gender: SexualGender?,
        orientation: SexualOrientation?
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
        city: City?
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
        interests: List<Interests>?
    ) = flow {
        try {
            emit(ResponseState.Loading)
            val datingPreferences = DatingPreferences(lookingFor = Choice(interests, false))
            usersRef.document(userId).update(UserFields.DATING_PREFERENCES, datingPreferences).await()
            emit(ResponseState.Success(Unit))
        } catch (e: Exception) {
            Timber.w("updateUserDataInterests::failure", e.message)
            emit(ResponseState.Failure(e, e.message))
        }
    }

    override suspend fun updateUserDataWorkAndEducation(
        userId: String,
        work: Work?,
        education: Education?
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
        isDrinker: Boolean?,
        isSmoker: Boolean?,
        hasChildren: Boolean?,
        religion: String?
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
        hobbies: List<String>?
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
        bio: String?
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

    override fun getUserData(userId: String): Flow<User?> = flow {
        val documentSnapshot = usersRef.document(userId).get().await()
        if (documentSnapshot.exists()) {
            val user = documentSnapshot.toObject(User::class.java)
            emit(user)
        } else {
            emit(User())
        }
    }.catch { e ->
        // Handle exception appropriately, for example:
        Timber.i("getUserData.error: ${e.message}")
        emit(null)
    }


    override fun addUserSnapshotListener(
        userId: String,
        responseState: (ResponseState<User?>) -> Unit
    ) {
        responseState(ResponseState.Loading)
        usersRef.document(userId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Timber.w("Listen failed.", e)
                responseState(ResponseState.Failure(e))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Timber.d("Current data: ${snapshot.data}")
                responseState(ResponseState.Success(snapshot.toObject<User?>()))
            } else {
                responseState(
                    ResponseState.Failure(Exception("Current data: null"))
                )
                Timber.d("Current data: null")
            }
        }
    }

    override fun getDistances(): Flow<List<Int>> = flow {
        try {
            val preferencesRef = db.collection(PREFERENCES_PATH).document(PREFERENCES_DISTANCE)
            val snapshot = preferencesRef.get().await()
            val currentDistances = (snapshot?.data?.get("km") as? List<*>)?.map { (it as Long).toInt() }
            if (currentDistances != null) {
                emit(currentDistances)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

//    override suspend fun getUserData(userId: String) = flow {
//        try {
//            emit(ResponseState.Loading)
//            val documentSnapshot = usersRef.document(userId).get().await()
//            if (documentSnapshot.exists()) {
//                val user = documentSnapshot.toObject(User::class.java)
//                if (user != null) {
//                    emit(ResponseState.Success(user))
//                } else {
//                    Timber.w("getUserData::failure user null")
//                    emit(ResponseState.Failure(errorMessage = "usuario nulo"))
//                }
//            }else{
//                Timber.w("getUserData::success usuario não existe")
//                emit(ResponseState.Success(null))
//            }
//        } catch (e: Exception) {
//            Timber.w("getUserData::failure", e.message)
//            emit(ResponseState.Failure(e, e.message))
//        }
//    }
}
