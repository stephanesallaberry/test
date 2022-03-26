

package fr.stephanesallaberry.news.android.di

import fr.stephanesallaberry.news.android.domain.external.AccountInteractor
import fr.stephanesallaberry.news.android.domain.external.CatInteractor
import org.koin.dsl.module

val interactorsModule = module {
    single { CatInteractor(get()) }
    single { AccountInteractor(get(), get()) }
}
