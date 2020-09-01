package moe.ganen.github.utils.modules

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import moe.ganen.github.data.network.ResultProvider
import moe.ganen.github.data.network.ResultProviderImpl
import moe.ganen.github.data.repository.SearchRepositoryImpl
import moe.ganen.github.domain.repository.SearchRepository
import moe.ganen.github.domain.usecase.SearchUserUseCase
import moe.ganen.github.presentation.viewmodel.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val githubModule = module {
    factory { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    single { provideOkHttpCache(get()) }
    single { provideOkHttpClient(get()) }
    single { provideApolloClient(get()) }
    single<ResultProvider> { ResultProviderImpl(get()) }

    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }

    factory { SearchUserUseCase(get()) }

    viewModel { SearchViewModel(get()) }

}