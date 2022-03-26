package fr.stephanesallaberry.news.android

import fr.stephanesallaberry.news.android.domain.external.NewsInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.domain.internal.INewsProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class NewsInteractorTest {

    private val itemTemplate = Article(
        title = "1",
        description = "Description",
        urlToImage = "https://placekitten.com/200/300"
    )
    private val listArticles = listOf(
        itemTemplate,
        itemTemplate.copy(title = "3"),
        itemTemplate.copy(title = "2")
    )

    @Test
    fun `returns list of articles as is`() {
        val provider = object : INewsProvider {
            override suspend fun getNews(): List<Article> {
                return listArticles
            }
        }
        val interactor = NewsInteractor(provider)
        runBlockingTest {
            val list = interactor.getNews()
            assertEquals(listArticles, list)
        }
    }
}
