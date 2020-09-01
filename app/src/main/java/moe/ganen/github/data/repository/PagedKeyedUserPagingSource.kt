package moe.ganen.github.data.repository

import androidx.paging.PagingSource
import com.apollographql.apollo.exception.ApolloException
import moe.ganen.github.data.mapper.toDomainModel
import moe.ganen.github.data.network.ResultProvider
import moe.ganen.github.domain.model.User
import java.io.IOException

class PagedKeyedUserPagingSource(
    private val query: String,
    private val resultProvider: ResultProvider
) : PagingSource<String, User>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, User> {
        return try {
            val response = resultProvider.getSearchUserQueryResult(
                query = query,
                before = if (params is LoadParams.Prepend) params.key else null,
                after = if (params is LoadParams.Append) params.key else null
            )
            val result = response?.search?.toDomainModel()

            val hasNextPage = result?.pageInfo?.hasNextPage ?: false
            val endCursor: String? = result?.pageInfo?.endCursor

            val hasPrevPage = result?.pageInfo?.hasPreviousPage ?: false
            val startCursor: String? = result?.pageInfo?.startCursor

            LoadResult.Page(
                data = result?.edges?.mapNotNull { node -> node?.user } ?: emptyList(),
                prevKey = startCursor?.takeIf { hasPrevPage },
                nextKey = endCursor?.takeIf { hasNextPage }
            )
        } catch (e: ApolloException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}