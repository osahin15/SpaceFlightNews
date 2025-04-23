package com.onursahin.feature.list.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onursahin.domain.model.News
import com.onursahin.domain.usecase.SpaceNewsPagingUseCase
import com.onursahin.ui.base.BaseComposeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticlesViewModel @Inject constructor(
    private val newsUseCase: SpaceNewsPagingUseCase
) : BaseComposeViewModel<ListScreenContract.Event, ListScreenContract.State, ListScreenContract.Effect>() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    private val articlesUiFlow: StateFlow<PagingData<News>> =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                newsUseCase(query).cachedIn(viewModelScope)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )

    init {
        articlesUiFlow()
    }

    private fun setSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
            setState {
                copy(searchQuery = query, list = articlesUiFlow)
            }
        }
    }

    private fun articlesUiFlow() {
        viewModelScope.launch {
            setState {
                copy(list = articlesUiFlow)
            }
        }
    }

    override fun setInitialState(): ListScreenContract.State {
        return ListScreenContract.State(
            list = flowOf(PagingData.empty()),
            searchQuery = ""
        )
    }

    override fun handleEvents(event: ListScreenContract.Event) {
        when (event) {
            is ListScreenContract.Event.OnSearchQueryChanged -> setSearchQuery(query = event.query)
            is ListScreenContract.Event.OnErrorSnackBar -> {
                viewModelScope.launch {
                    setEffect {
                        ListScreenContract.Effect.ShowSnackBar(
                            message = event.error.message.orEmpty(),
                            actionLabel = event.actionLabel,
                            isDismiss = event.isDismiss
                        )
                    }
                }
            }
        }
    }
}