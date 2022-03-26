package fr.stephanesallaberry.news.android.transport.home

import androidx.lifecycle.ViewModel
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.domain.external.CatInteractor
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class HomeViewModel(private val catInteractor: CatInteractor) :
    ContainerHost<HomeScreenState, HomeScreenSideEffect>, ViewModel() {

    override val container =
        container<HomeScreenState, HomeScreenSideEffect>(
            HomeScreenState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: HomeScreenState) {
        getBreeds()
    }

    @SuppressWarnings("TooGenericExceptionCaught") // TODO catch and throw our own errors
    fun getBreeds() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = catInteractor.getBreeds()

            reduce {
                state.copy(breeds = list, isLoading = false)
            }
        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(breeds = emptyList(), isLoading = false)
            }

            postSideEffect(HomeScreenSideEffect.Toast(R.string.general_error))
        }
    }
}
