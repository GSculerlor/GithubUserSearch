package moe.ganen.github.data.network

import moe.ganen.github.SearchUserByQuery

interface ResultProvider {

    suspend fun getSearchUserQueryResult(
        query: String,
        before: String?,
        after: String?
    ): SearchUserByQuery.Data?
}