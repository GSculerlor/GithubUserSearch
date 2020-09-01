package moe.ganen.github.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import moe.ganen.github.domain.model.User

interface SearchRepository {

    suspend fun searchUser(query: String): Flow<PagingData<User>>
}