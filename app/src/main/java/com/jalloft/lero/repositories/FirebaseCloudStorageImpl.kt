package com.jalloft.lero.repositories

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.jalloft.lero.data.domain.Photo
import com.jalloft.lero.util.PHOTOS_PATH
import com.jalloft.lero.util.PHOTO_PROFILE
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.Timestamp
import com.jalloft.lero.util.PHOTO_COLLECTION
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.Date
import javax.inject.Named


class FirebaseCloudStorageImpl @Inject constructor(
    @Named(PHOTOS_PATH) private val photoRef: StorageReference
) : FirebaseCloudStorage {
    override suspend fun saveProfilePhoto(photo: Photo?, userId: String?) = flow {
        emit(ResponseState.Loading)
        try {
            val fileName = "PROFILE_$userId.jpg"
            val profilePhotoRef = photoRef.child("$PHOTO_PROFILE/$userId/$fileName")
            val uploadTask = profilePhotoRef.putFile(Uri.parse(photo?.url)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            val creationTimeMillis =
                uploadTask.metadata?.creationTimeMillis ?: System.currentTimeMillis()
            emit(
                ResponseState.Success(
                    Photo(
                        url = downloadUrl?.toString(),
                        name = fileName,
                        userCreatedIn = Timestamp(Date(creationTimeMillis))
                    )
                )
            )
        } catch (e: Exception) {
            emit(ResponseState.Failure(e))
        }


    }

    override suspend fun savePhotos(photos: List<Photo>, userId: String?) = flow {
        emit(ResponseState.Loading)
        try {
            val uploadTasks = photos.map { photo ->
                val fileName = "COLLECTION_${System.currentTimeMillis()}.jpg"
                val collectionPhotoRef = photoRef.child("$PHOTO_COLLECTION/$userId/$fileName")
                collectionPhotoRef.putFile(Uri.parse(photo.url)).await()
            }
            val savedPhotos = uploadTasks.map { taskSnapshot ->
                val downloadUrl = taskSnapshot.storage.downloadUrl.await()
                val creationTimeMillis =
                    taskSnapshot.metadata?.creationTimeMillis ?: System.currentTimeMillis()
                val fileName = taskSnapshot.metadata?.name
                Photo(
                    url = downloadUrl?.toString(),
                    name = fileName,
                    userCreatedIn = Timestamp(Date(creationTimeMillis))
                )
            }
            emit(ResponseState.Success(savedPhotos))
        } catch (e: Exception) {
            emit(ResponseState.Failure(e))
        }
    }

    override suspend fun deleteProfilePhoto(
        photo: Photo,
        userId: String?
    ) = flow {
        emit(ResponseState.Loading)
        try {
            photoRef.child("$PHOTO_PROFILE/$userId/${photo.name}")
                .delete().await()
            emit(ResponseState.Success(photo))
        } catch (e: Exception) {
            emit(ResponseState.Failure(e))
        }
    }

    override suspend fun deletePhotos(
        photos: List<Photo>,
        userId: String?
    ) = flow {
        emit(ResponseState.Loading)
        try {
            for (photo in photos) {
                photoRef.child("$PHOTO_COLLECTION/$userId${photo.name}")
                    .delete().await()
            }
            emit(ResponseState.Success(photos))
        } catch (e: Exception) {
            emit(ResponseState.Failure(e))
        }
    }

}