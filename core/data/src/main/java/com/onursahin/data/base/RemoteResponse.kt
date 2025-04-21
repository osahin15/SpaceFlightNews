package com.onursahin.data.base

import com.onursahin.domain.UiResponse

sealed class RemoteResponse<out T : Any> {
    data class Success<out T : Any>(val result: T) : RemoteResponse<T>()
    data class Error(val exception: Exception) : RemoteResponse<Nothing>()

    fun toUiResponse(): UiResponse<T>{
        return when(this){
            is Success -> UiResponse.Success(result)
            is Error -> UiResponse.Error(exception)
        }
    }
}