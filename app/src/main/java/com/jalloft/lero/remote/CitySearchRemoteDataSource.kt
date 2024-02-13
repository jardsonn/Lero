package com.jalloft.lero.remote

import com.jalloft.lero.service.CitySearchService
import retrofit2.http.Query
import javax.inject.Inject


class CitySearchRemoteDataSource @Inject constructor(private val service: CitySearchService) :
    BaseDataSource() {

    suspend fun searchCity(query: String, limit: Int) =
        getResult { service.searchCity(query, limit.toString()) }

}