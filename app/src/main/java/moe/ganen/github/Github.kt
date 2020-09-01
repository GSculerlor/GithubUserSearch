package moe.ganen.github

import androidx.multidex.MultiDexApplication
import moe.ganen.github.utils.modules.githubModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Github : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Github)
            androidLogger(Level.ERROR)

            modules(listOf(githubModule))
        }
    }
}