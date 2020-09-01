package moe.ganen.github.utils.modules

import android.app.Application
import com.apollographql.apollo.ApolloClient
import moe.ganen.github.utils.network.TLSSocketFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
    .serverUrl("https://api.github.com/graphql")
    .okHttpClient(okHttpClient)
    .build()

fun provideOkHttpClient(cache: Cache): OkHttpClient {
    val tlsSocketFactory = TLSSocketFactory()

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder().method(original.method(), original.body())
        builder.addHeader(
            "Authorization",
            "Bearer $TOKEN"
        )
        chain.proceed(builder.build())
    }
    return OkHttpClient.Builder()
        .cache(cache)
        .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager())
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .build()
}

fun provideOkHttpCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024 // 10 MB
    return Cache(application.cacheDir, cacheSize.toLong())
}

const val TOKEN = ""