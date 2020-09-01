package moe.ganen.github.data.mapper

import moe.ganen.github.SearchUserByQuery
import moe.ganen.github.domain.model.Edge
import moe.ganen.github.domain.model.PageInfo
import moe.ganen.github.domain.model.SearchQueryResult
import moe.ganen.github.domain.model.User

fun SearchUserByQuery.Search.toDomainModel(): SearchQueryResult = mapSearchFromResponse(this)

private fun mapSearchFromResponse(response: SearchUserByQuery.Search) = SearchQueryResult(
    pageInfo = mapPageInfoFromResponse(response.pageInfo),
    userCount = response.userCount,
    edges = mapEdgesFromResponse(response.edges)
)

private fun mapPageInfoFromResponse(response: SearchUserByQuery.PageInfo) = PageInfo(
    startCursor = response.startCursor,
    endCursor = response.endCursor,
    hasPreviousPage = response.hasPreviousPage,
    hasNextPage = response.hasNextPage
)

private fun mapEdgesFromResponse(edges: List<SearchUserByQuery.Edge?>?): List<Edge?>? {
    return if (edges == null)
        null
    else {
        val mappedEdges = mutableListOf<Edge>()

        for (edge in edges) {
            if (edge?.cursor == null)
                continue

            mappedEdges.add(
                Edge(
                    cursor = edge.cursor,
                    user = mapUserFromResponse(edge.node)
                )
            )
        }

        mappedEdges
    }
}

private fun mapUserFromResponse(node: SearchUserByQuery.Node?): User? {
    return when {
        node?.asUser != null -> {
            val userFromNode = node.asUser

            User(
                id = userFromNode.id,
                username = userFromNode.login,
                name = userFromNode.name,
                avatarUrl = "${userFromNode.avatarUrl}"
            )
        }
        node?.asOrganization != null -> {
            val organizationFromNode = node.asOrganization

            User(
                id = organizationFromNode.id,
                username = organizationFromNode.login,
                name = organizationFromNode.name,
                avatarUrl = "${organizationFromNode.avatarUrl}"
            )
        }
        else -> null
    }
}
