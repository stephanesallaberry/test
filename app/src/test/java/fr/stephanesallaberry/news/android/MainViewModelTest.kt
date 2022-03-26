package fr.stephanesallaberry.news.android

import fr.stephanesallaberry.news.android.domain.external.NewsInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.transport.home.HomeScreenState
import fr.stephanesallaberry.news.android.transport.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.orbitmvi.orbit.test
import java.util.Date

class MainViewModelTest {

    private val articleOrigin = Article(
        title = "1",
        description = "Description",
        urlToImage = "https://placekitten.com/200/300",
        url = "https://placekitten.com",
        content = "Full content",
        publishedAt = Date()
    )
    private val listArticles = listOf(
        articleOrigin,
        articleOrigin.copy(title = "3"),
        articleOrigin.copy(title = "2")
    )

    @ExperimentalCoroutinesApi
    @Test
    fun loadarticlesAtStart() {
        runBlockingTest {
            val newsInteractorTest = mock(NewsInteractor::class.java)
            val testSubject =
                HomeViewModel(newsInteractorTest).test(HomeScreenState())

            `when`(newsInteractorTest.getNews()).thenReturn(listArticles)

            testSubject.runOnCreate() // must be invoked once and before `testIntent`
            testSubject.assert(HomeScreenState()) {
                states(
                    { copy(articles = emptyList(), isLoading = true) },
                    { copy(articles = listArticles, isLoading = false) }
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun toggleLoadingStateWhileKeepingExistingList() {
        runBlockingTest {
            val newsInteractorTest = mock(NewsInteractor::class.java)
            val testSubject =
                HomeViewModel(newsInteractorTest).test(HomeScreenState())

            `when`(newsInteractorTest.getNews()).thenReturn(listArticles)

            testSubject.runOnCreate() // must be invoked once and before `testIntent`
            testSubject.testIntent { getArticles() }

            testSubject.assert(HomeScreenState()) {
                states(
                    { copy(articles = emptyList(), isLoading = true) },
                    { copy(articles = listArticles, isLoading = false) },
                    { copy(articles = listArticles, isLoading = true) },
                    { copy(articles = listArticles, isLoading = false) }
                )
            }
        }
    }
}
