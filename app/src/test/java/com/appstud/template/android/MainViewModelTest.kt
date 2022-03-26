package fr.stephanesallaberry.news.android

import fr.stephanesallaberry.news.android.domain.external.CatInteractor
import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import fr.stephanesallaberry.news.android.domain.external.entity.NetworkImage
import fr.stephanesallaberry.news.android.transport.home.HomeScreenState
import fr.stephanesallaberry.news.android.transport.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.orbitmvi.orbit.test

class MainViewModelTest {

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

    @ExperimentalCoroutinesApi
    @Test
    fun loadBreedsAtStart() {
        runBlockingTest {
            val catInteractorTest = mock(CatInteractor::class.java)
            val testSubject =
                HomeViewModel(catInteractor = catInteractorTest).test(HomeScreenState())

            `when`(catInteractorTest.getBreeds()).thenReturn(listBreeds)

            testSubject.runOnCreate() // must be invoked once and before `testIntent`
            testSubject.assert(HomeScreenState()) {
                states(
                    { copy(breeds = emptyList(), isLoading = true) },
                    { copy(breeds = listBreeds, isLoading = false) }
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun toggleLoadingStateWhileKeepingExistingList() {
        runBlockingTest {
            val catInteractorTest = mock(CatInteractor::class.java)
            val testSubject =
                HomeViewModel(catInteractor = catInteractorTest).test(HomeScreenState())

            `when`(catInteractorTest.getBreeds()).thenReturn(listBreeds)

            testSubject.runOnCreate() // must be invoked once and before `testIntent`
            testSubject.testIntent { getBreeds() }

            testSubject.assert(HomeScreenState()) {
                states(
                    { copy(breeds = emptyList(), isLoading = true) },
                    { copy(breeds = listBreeds, isLoading = false) },
                    { copy(breeds = listBreeds, isLoading = true) },
                    { copy(breeds = listBreeds, isLoading = false) }
                )
            }
        }
    }
}
