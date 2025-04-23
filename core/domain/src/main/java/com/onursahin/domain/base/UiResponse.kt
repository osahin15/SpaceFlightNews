package com.onursahin.domain.base

sealed class UiResponse<out T> {
    object Loading : UiResponse<Nothing>()
    data class Success<out T : Any>(val result: T) : UiResponse<T>()
    data class Error(val exception: BaseError) : UiResponse<Nothing>()
}


inline fun <T> UiResponse<T>.onSuccess(action: (T) -> Unit): UiResponse<T> {
    if (this is UiResponse.Success<T>) action(this.result)
    return this
}

inline fun <T> UiResponse<T>.onError(action: (BaseError) -> Unit): UiResponse<T> {
    if (this is UiResponse.Error) action(this.exception)
    return this
}

inline fun <T> UiResponse<T>.onLoading(action: () -> Unit): UiResponse<T> {
    if (this is UiResponse.Loading) action()
    return this
}