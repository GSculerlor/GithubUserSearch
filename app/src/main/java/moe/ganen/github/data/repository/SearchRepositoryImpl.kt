package moe.ganen.github.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import moe.ganen.github.data.network.ResultProvider
import moe.ganen.github.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val resultProvider: ResultProvider,
    private val scope: CoroutineScope
) : SearchRepository {

    override suspend fun searchUser(query: String) = Pager(PagingConfig(20)) {
        PagedKeyedUserPagingSource(
            query = query,
            resultProvider = resultProvider
        )
    }.flow.cachedIn(scope)
}