package com.jalloft.lero.repositories

import com.jalloft.lero.data.nominatim.Place
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.Flow


interface CitySearchRepository {

    suspend fun searchCity(query: String, limit: Int): Flow<ResponseState<List<Place>>>

}