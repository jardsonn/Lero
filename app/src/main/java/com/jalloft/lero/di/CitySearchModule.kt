package com.jalloft.lero.di

import com.google.gson.GsonBuilder
import com.jalloft.lero.remote.CitySearchRemoteDataSource
import com.jalloft.lero.repositories.CitySearchRepository
import com.jalloft.lero.repositories.CitySearchRepositoryImpl
import com.jalloft.lero.service.CitySearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Jardson Costa on 11/02/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object CitySearchModule {

    private const val URL_BASE_CITY = "https://nominatim.openstreetmap.org/"
    private val okHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(URL_BASE_CITY)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().create()
            )
        )
        .build()

    @Provides
    fun provideCitySearchService(retrofit: Retrofit): CitySearchService =
        retrofit.create(CitySearchService::class.java)


    @Singleton
    @Provides
    fun provideCitySearchRemoteDataSource(service: CitySearchService) =
        CitySearchRemoteDataSource(service)

    @Singleton
    @Provides
    fun provideCepRepository(remoteDataSource: CitySearchRemoteDataSource): CitySearchRepository =
        CitySearchRepositoryImpl(remoteDataSource)

}