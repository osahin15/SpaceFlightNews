package com.onursahin.domain

sealed class UiResponse<out T> {
    object Loading : UiResponse<Nothing>()
    data class Success<out T : Any>(val result: T) : UiResponse<T>()
    data class Error(val exception: Exception) : UiResponse<Nothing>()
}