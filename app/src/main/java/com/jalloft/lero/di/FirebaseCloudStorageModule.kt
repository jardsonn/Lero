package com.jalloft.lero.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jalloft.lero.repositories.FirebaseCloudStorage
import com.jalloft.lero.repositories.FirebaseCloudStorageImpl
import com.jalloft.lero.repositories.FirebaseFirestoreRepository
import com.jalloft.lero.repositories.FirebaseFirestoreRepositoryImpl
import com.jalloft.lero.util.PHOTOS_PATH
import com.jalloft.lero.util.USERS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * Created by Jardson Costa on 25/02/2024.
 */

@Module
@InstallIn(SingletonComponent::class)
object FirebaseCloudStorageModule {

    @Provides
    fun provideFirebaseCloudStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseCloudStorageRepository(
        @Named(PHOTOS_PATH) photoRef: StorageReference,
    ): FirebaseCloudStorage = FirebaseCloudStorageImpl(photoRef)

    @Provides
    @Named(PHOTOS_PATH)
    fun providePhotosRef(storage: FirebaseStorage) = storage.getReference(PHOTOS_PATH)

}