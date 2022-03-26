package fr.stephanesallaberry.news.android.di

import fr.stephanesallaberry.news.android.data.CatProvider
import fr.stephanesallaberry.news.android.data.StorageProvider
import fr.stephanesallaberry.news.android.data.UserApiProvider
import fr.stephanesallaberry.news.android.domain.internal.ICatProvider
import fr.stephanesallaberry.news.android.domain.internal.IStorageProvider
import fr.stephanesallaberry.news.android.domain.internal.IUserApiProvider
import org.koin.dsl.module

val providersModule = module {
    single<ICatProvider> { CatProvider(get()) }
    single<IStorageProvider> { StorageProvider(get()) }
    single<IUserApiProvider> { UserApiProvider() }
}
