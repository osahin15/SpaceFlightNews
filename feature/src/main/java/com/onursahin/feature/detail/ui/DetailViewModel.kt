package com.onursahin.feature.detail.ui

import androidx.lifecycle.viewModelScope
import com.onursahin.domain.base.onError
import com.onursahin.domain.base.onLoading
import com.onursahin.domain.base.onSuccess
import com.onursahin.domain.usecase.DetailUseCase
import com.onursahin.ui.base.BaseComposeViewModel
import com.onursahin.ui.base.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase
) : BaseComposeViewModel<DetailScreenContract.Event, DetailScreenContract.State, DetailScreenContract.Effect>() {

    private fun getDetail(id: Int) {
        viewModelScope.launch {
            detailUseCase.invoke(id).collect { resource ->
                resource.onLoading {
                    setState {
                        copy(isLoadings = true)
                    }
                }.onSuccess { data ->
                    setState {
                        copy(news = data, isLoadings = false)
                    }
                }.onError { error ->
                    ErrorManager.emitError(error)
                }
            }
        }
    }

    override fun setInitialState(): DetailScreenContract.State {
        return DetailScreenContract.State(
            news = null,
            isLoadings = false
        )
    }

    override fun handleEvents(event: DetailScreenContract.Event) {
        when (event) {
            is DetailScreenContract.Event.OnAddFavorite -> {

            }

            is DetailScreenContract.Event.GetDetailWithId -> {
                getDetail(event.id)
            }
        }
    }
}