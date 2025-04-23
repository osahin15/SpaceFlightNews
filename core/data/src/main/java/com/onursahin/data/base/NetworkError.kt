package com.onursahin.data.base

import com.onursahin.domain.base.BaseError

sealed class NetworkError(code: Int?, message: String?, throwable: Throwable?) :
    BaseError(code, message, throwable) {
    class Default(throwable: Throwable?) :
        NetworkError(GENERAL_ERROR_CODE, GENERIC_ERROR_MESSAGE, throwable)

    class SocketTimeOut(throwable: Throwable?) :
        NetworkError(GENERAL_ERROR_CODE, TIMEOUT_MESSAGE, throwable)

    class NoInternetConnection(throwable: Throwable?) :
        NetworkError(GENERAL_ERROR_CODE, NO_INTERNET_CONNECTION_MESSAGE, throwable)
}


const val GENERAL_ERROR_CODE = -1
const val GENERIC_ERROR_MESSAGE = "Something went wrong"
const val NO_INTERNET_CONNECTION_MESSAGE = "No internet connection"
const val TIMEOUT_MESSAGE = "Request timed out"