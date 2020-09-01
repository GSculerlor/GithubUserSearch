package moe.ganen.github.data.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import moe.ganen.github.SearchUserByQuery

class ResultProviderImpl(private val apolloClient: ApolloClient) : ResultProvider {

    override suspend fun getSearchUserQueryResult(
        query: String,
        before: String?,
        after: String?
    ): SearchUserByQuery.Data? =
        apolloClient.query(
            SearchUserByQuery(
                query = query,
                limit = Input.fromNullable(10),
                before = Input.optional(before),
                after = Input.optional(after)
            )
        ).toDeferred().await().data
}