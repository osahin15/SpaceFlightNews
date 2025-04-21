package com.onursahin.data.base

import retrofit2.HttpException
import java.io.IOException


suspend fun <T : Any> getResult(call: suspend () -> T): RemoteResponse<T> {
    return  try {
        val response = call.invoke()
        RemoteResponse.Success(response)
    } catch (e: HttpException) {
        RemoteResponse.Error(e)
    }catch (e : IOException){
        RemoteResponse.Error(e)
    }
}