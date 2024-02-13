package com.jalloft.lero.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.jalloft.lero.repositories.FirebaseAuthRepository
import com.jalloft.lero.repositories.FirebaseAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by Jardson Costa on 06/02/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseAuthModule {

    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): FirebaseAuthRepository = FirebaseAuthRepositoryImpl(auth)

}