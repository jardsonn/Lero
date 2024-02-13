package com.jalloft.lero.remote

import com.jalloft.lero.util.ResponseState
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber


abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>) = flow<ResponseState<T>> {
        try{
            emit(ResponseState.Loading)
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if(body != null) {
                    emit(ResponseState.Success(body))
                }else{
                    emit(ResponseState.Failure(Exception("code: r${response.code()},  message:${response.message()}")))
                }
            }else{
                emit(ResponseState.Failure(Exception("code: ${response.code()},  message:${response.message()}")))
                Timber.e("A chamada de rede falhou pelo seguinte motivo: ${response.message()}")
            }
        }catch (e: Exception){
            emit(ResponseState.Failure(e))
        }
    }

}