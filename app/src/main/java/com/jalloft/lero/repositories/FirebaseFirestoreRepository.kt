package com.jalloft.lero.repositories

import com.jalloft.lero.data.domain.City
import com.jalloft.lero.data.domain.Education
import com.jalloft.lero.data.domain.Height
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.data.domain.Work
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.Flow
import java.util.Date


interface FirebaseFirestoreRepository {


    suspend fun saveNewUser(userId: String, user: User): Flow<ResponseState<Unit>>

    suspend fun updateUserEssentialInformation(
        userId: String,
        name: String?,
        dateOfBirth: Date?,
        height: Height?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataGenderAndOrientation(
        userId: String,
        gender: SexualGender?,
        orientation: SexualOrientation?
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataCity(
        userId: String,
        city: City?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataInterests(
        userId: String,
        interests: List<Interests>?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataWorkAndEducation(
        userId: String,
        work: Work?,
        education: Education?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataLifeDetails(
        userId: String,
        isDrinker: Boolean?,
        isSmoker: Boolean?,
        hasChildren: Boolean?,
        religion: String?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataHobbies(
        userId: String,
        hobbies: List<String>?,
    ): Flow<ResponseState<Unit>>

    suspend fun updateUserDataBio(
        userId: String,
        bio: String?,
    ): Flow<ResponseState<Unit>>


    //    suspend fun getUserData(userId: String,): Flow<ResponseState<User?>>
    fun getUserData(userId: String): Flow<User?>

    suspend fun updateOrEdit(userId: String?, updates: Map<String, Any?>): Flow<ResponseState<Unit>>

    fun addUserSnapshotListener(userId: String, responseState: (ResponseState<User?>) -> Unit)

    fun getDistances(): Flow<List<Int>>

}