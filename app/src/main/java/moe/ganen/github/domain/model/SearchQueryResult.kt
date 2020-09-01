package moe.ganen.github.domain.model

data class SearchQueryResult(
    val pageInfo: PageInfo?,
    val userCount: Int?,
    val edges: List<Edge?>?
)