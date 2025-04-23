package com.onursahin.ui.base

import com.onursahin.domain.base.BaseError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ErrorManager {
    private val _errorFlow = MutableSharedFlow<BaseError>()
    val errorFlow: SharedFlow<BaseError> = _errorFlow.asSharedFlow()

    suspend fun emitError(baseError: BaseError) {
        _errorFlow.emit(baseError)
    }
}