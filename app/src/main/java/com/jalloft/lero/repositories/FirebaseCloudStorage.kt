package com.jalloft.lero.repositories

import android.net.Uri
import com.jalloft.lero.data.domain.Photo
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.Flow


interface FirebaseCloudStorage {

    suspend fun saveProfilePhoto(photo: Photo?, userId: String?): Flow<ResponseState<Photo>>

    suspend fun savePhotos(photos: List<Photo>, userId: String?): Flow<ResponseState<List<Photo>>>

    suspend fun deleteProfilePhoto(photo: Photo, userId: String?): Flow<ResponseState<Photo>>
    suspend fun deletePhotos(photos: List<Photo>, userId: String?): Flow<ResponseState<List<Photo>>>

}