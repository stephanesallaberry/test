package fr.stephanesallaberry.news.android.transport.detail

import androidx.lifecycle.ViewModel
import fr.stephanesallaberry.news.android.domain.external.CatInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class DetailViewModel(
    private val catInteractor: CatInteractor,
    private val breedData: Breed?
) :
    ContainerHost<DetailScreenState, DetailScreenSideEffect>, ViewModel() {

    override val container =
        container<DetailScreenState, DetailScreenSideEffect>(
            DetailScreenState(breedData),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: DetailScreenState) {
        // no-op
    }
}
