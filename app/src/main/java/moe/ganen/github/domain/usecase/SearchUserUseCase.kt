package moe.ganen.github.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import moe.ganen.github.domain.model.User
import moe.ganen.github.domain.repository.SearchRepository

class SearchUserUseCase(private val repository: SearchRepository) {

    suspend fun execute(query: String): Flow<PagingData<User>> = repository.searchUser(query)
}