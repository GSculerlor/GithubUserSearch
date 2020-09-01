package moe.ganen.github.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import moe.ganen.github.domain.usecase.SearchUserUseCase

class SearchViewModel(
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    private val searchQuery = MutableLiveData<String?>()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResultFlow = flowOf(
        searchQuery
            .asFlow()
            .flatMapLatest { searchUserUseCase.execute(it ?: "") }
    ).flattenMerge(2)

    fun search(query: String) {
        if (query == searchQuery.value)
            return

        searchQuery.value = query
    }

    init {
        if (searchQuery.value.isNullOrEmpty())
            search("pikachu")
    }
}