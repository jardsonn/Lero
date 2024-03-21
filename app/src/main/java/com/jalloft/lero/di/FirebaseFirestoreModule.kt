package com.jalloft.lero.di

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jalloft.lero.repositories.FirebaseAuthRepository
import com.jalloft.lero.repositories.FirebaseFirestoreRepository
import com.jalloft.lero.repositories.FirebaseFirestoreRepositoryImpl
import com.jalloft.lero.util.USERS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * Created by Jardson Costa on 06/02/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseFirestoreModule {

    @Provides
    fun provideFirebaseFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseFirestoreRepository(
        db: FirebaseFirestore,
        @Named(USERS) usersRef: CollectionReference,
    ): FirebaseFirestoreRepository = FirebaseFirestoreRepositoryImpl(db, usersRef)


    @Provides
    @Named(USERS)
    fun provideUsersRef(db: FirebaseFirestore) = db.collection(USERS)


}