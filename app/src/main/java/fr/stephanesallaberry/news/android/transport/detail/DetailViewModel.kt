package fr.stephanesallaberry.news.android.transport.detail

import androidx.lifecycle.ViewModel
import fr.stephanesallaberry.news.android.domain.external.NewsInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class DetailViewModel(
    private val newsInteractor: NewsInteractor,
    private val articleData: Article?
) :
    ContainerHost<DetailScreenState, DetailScreenSideEffect>, ViewModel() {

    override val container =
        container<DetailScreenState, DetailScreenSideEffect>(
            DetailScreenState(articleData),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: DetailScreenState) {
        // no-op
    }
}
