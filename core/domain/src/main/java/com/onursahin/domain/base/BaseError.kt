package com.onursahin.domain.base

open class BaseError(val code: Int?, val message: String?, var throwable: Throwable? = null)