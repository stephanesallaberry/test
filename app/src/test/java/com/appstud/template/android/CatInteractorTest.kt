package fr.stephanesallaberry.news.android

import fr.stephanesallaberry.news.android.domain.external.CatInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import fr.stephanesallaberry.news.android.domain.external.entity.NetworkImage
import fr.stephanesallaberry.news.android.domain.internal.ICatProvider
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class CatInteractorTest {

    private val breedOrigin = Breed(
        name = "1",
        description = "Description",
        image = NetworkImage("1", "https://placekitten.com/200/300")
    )
    private val listBreeds = listOf(
        breedOrigin,
        breedOrigin.copy(name = "3"),
        breedOrigin.copy(name = "2")
    )

    @Test
    fun `returns list of breeds as is`() {
        val catProvider = object : ICatProvider {
            override suspend fun getBreeds(): List<Breed> {
                return listBreeds
            }
        }
        val interactor = CatInteractor(catProvider)
        runBlockingTest {
            val list = interactor.getBreeds()
            assertEquals(listBreeds, list)
        }
    }
}
