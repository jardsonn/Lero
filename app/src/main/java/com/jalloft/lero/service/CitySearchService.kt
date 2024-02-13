package com.jalloft.lero.service

import com.jalloft.lero.data.nominatim.Place
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface CitySearchService {

    @GET("search?format=json&featureType=city&addressdetails=1")
    suspend fun searchCity(@Query("city") query: String, @Query("limit") limit: String): Response<List<Place>>

}