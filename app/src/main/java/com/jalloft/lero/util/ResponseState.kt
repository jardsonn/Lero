package com.jalloft.lero.util


sealed class ResponseState<out T> {
    data object Loading: ResponseState<Nothing>()

    data class Success<out T>(
        val data: T
    ): ResponseState<T>()

    data class Failure(
        val exception: Exception? = null,
        val errorMessage: String? = null
    ): ResponseState<Nothing>()
}