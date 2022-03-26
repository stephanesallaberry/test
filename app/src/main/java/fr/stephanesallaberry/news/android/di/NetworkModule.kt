package fr.stephanesallaberry.news.android.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import fr.stephanesallaberry.news.android.BuildConfig
import fr.stephanesallaberry.news.android.data.network.CatApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gsonBuilder = GsonBuilder()
    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))

    return retrofitBuilder.build()
}

val networkModule = module {

    single() { createRetrofit(get<OkHttpClient>()) }

    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
        // add interceptor
        clientBuilder.addInterceptor(
            Interceptor {
                // add token and execute the request
                val updateRequest = it.request().newBuilder()
                    .addHeader("x-api-key", "af4591f6-62cd-4e6e-beda-551d6ffcc9a1")
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

    single { get<Retrofit>().create(CatApi::class.java) }
}
