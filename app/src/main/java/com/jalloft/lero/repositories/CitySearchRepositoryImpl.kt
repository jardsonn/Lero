package com.jalloft.lero.repositories

import com.jalloft.lero.data.nominatim.Place
import com.jalloft.lero.remote.CitySearchRemoteDataSource
import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CitySearchRepositoryImpl @Inject constructor(private val remoteDataSource: CitySearchRemoteDataSource) :
    CitySearchRepository {

    override suspend fun searchCity(query: String, limit: Int): Flow<ResponseState<List<Place>>> {
        return remoteDataSource.searchCity(query, limit)
    }


}