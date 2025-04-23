package com.onursahin.data.base

import com.onursahin.domain.base.BaseError
import com.onursahin.domain.base.UiResponse

sealed class RemoteResponse<out T : Any> {
    data class Success<out T : Any>(val result: T) : RemoteResponse<T>()
    data class Error(val exception: BaseError) : RemoteResponse<Nothing>()

    fun <R : Any> toUiResponse(mapper : (T) -> R): UiResponse<R>{
        return when(this){
            is Success -> UiResponse.Success(mapper(result))
            is Error -> UiResponse.Error(exception)
        }
    }
}