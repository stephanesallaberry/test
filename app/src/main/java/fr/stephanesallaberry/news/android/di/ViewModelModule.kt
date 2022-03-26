package fr.stephanesallaberry.news.android.di

import fr.stephanesallaberry.news.android.transport.MainViewModel
import fr.stephanesallaberry.news.android.transport.detail.DetailViewModel
import fr.stephanesallaberry.news.android.transport.home.HomeViewModel
import fr.stephanesallaberry.news.android.transport.login.LoginViewModel
import fr.stephanesallaberry.news.android.transport.utils.validation.EmailValidator
import fr.stephanesallaberry.news.android.transport.utils.validation.PasswordValidator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get(), EmailValidator(), PasswordValidator()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
}
