package fr.stephanesallaberry.news.android.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import fr.stephanesallaberry.news.android.BuildConfig
import fr.stephanesallaberry.news.android.data.network.NewsApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "API_KEY"

private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gsonBuilder = GsonBuilder()
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))

    return retrofitBuilder.build()
}

val networkModule = module {

    single(named(API_KEY)) { BuildConfig.API_KEY }

    single() { createRetrofit(get<OkHttpClient>()) }

    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
        // add interceptor
        clientBuilder.addInterceptor(
            Interceptor {
                // add api key and execute the request
                val request = it.request()
                val updateRequest = request.newBuilder()
                    .url(
                        request.url.newBuilder().addQueryParameter("apiKey", get(named(API_KEY)))
                            .build()
                    )
                    .build()
                it.proceed(updateRequest)
            }
        )

        // add logger
        if (BuildConfig.DEBUG) {
            val prettyLogger = LoggingInterceptor.Builder()
                .setLevel(Level.BODY)
                .build()
            // //Enable mock for develop app with mock data
            // .enableMock(BuildConfig.MOCK, 1000L, object : BufferListener {
            //     override fun getJsonResponse(request: Request?): String? {
            //         val segment = request?.url?.pathSegments?.getOrNull(0)
            //         return mAssetManager.open(String.format("mock/%s.json", segment)).source().buffer().readUtf8()
            //     }
            // })
            clientBuilder.addInterceptor(prettyLogger)
        }
        clientBuilder.addInterceptor(ChuckerInterceptor(get()))

        // Refresh Token interceptor we don't need it in this simple project
        // clientBuilder.addInterceptor(
        //     TokenInterceptor(
        //         get(),
        //         BuildConfig.API_BASE_URL
        //     )
        // )
        return@single clientBuilder.build()
    }

    single { get<Retrofit>().create(NewsApi::class.java) }
}
