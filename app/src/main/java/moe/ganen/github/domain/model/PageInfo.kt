package moe.ganen.github.domain.model

data class PageInfo(
    val startCursor: String?,
    val endCursor: String?,
    val hasPreviousPage: Boolean?,
    val hasNextPage: Boolean?
)