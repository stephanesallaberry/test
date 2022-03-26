package fr.stephanesallaberry.news.android.transport.home

import androidx.lifecycle.ViewModel
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.domain.external.NewsInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class HomeViewModel(private val newsInteractor: NewsInteractor) :
    ContainerHost<HomeScreenState, HomeScreenSideEffect>, ViewModel() {

    override val container =
        container<HomeScreenState, HomeScreenSideEffect>(
            HomeScreenState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: HomeScreenState) {
        getArticles()
    }

    @SuppressWarnings("TooGenericExceptionCaught") // TODO catch and throw our own errors
    fun getArticles() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = newsInteractor.getNews()

            reduce {
                state.copy(articles = list, isLoading = false)
            }
        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(articles = emptyList(), isLoading = false)
            }

            postSideEffect(HomeScreenSideEffect.Toast(R.string.general_error))
        }
    }
}
