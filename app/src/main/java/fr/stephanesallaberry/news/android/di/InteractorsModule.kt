

package fr.stephanesallaberry.news.android.di

import fr.stephanesallaberry.news.android.domain.external.AccountInteractor
import fr.stephanesallaberry.news.android.domain.external.NewsInteractor
import org.koin.dsl.module

val interactorsModule = module {
    single { NewsInteractor(get()) }
    single { AccountInteractor(get(), get()) }
}
