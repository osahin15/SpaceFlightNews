package com.onursahin.data.base

import com.onursahin.domain.base.BaseError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


suspend fun <T : Any> getResult(call: suspend () -> Response<T>): RemoteResponse<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                RemoteResponse.Success(it)
            } ?: RemoteResponse.Error(
                BaseError(
                    code = response.code(),
                    message = response.message()
                )
            )
        } else {
            RemoteResponse.Error(BaseError(code = response.code(), message = response.message()))
        }
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                RemoteResponse.Error(BaseError(code = e.code(), message = e.message))
            }

            is IOException -> {
                if (e is SocketTimeoutException) {
                    RemoteResponse.Error(NetworkError.SocketTimeOut(e))
                } else {
                    RemoteResponse.Error(NetworkError.NoInternetConnection(e))
                }
            }

            else -> {
                RemoteResponse.Error(NetworkError.Default(e))
            }
        }
    }
}