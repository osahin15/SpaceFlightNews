package com.onursahin.domain.usecase


import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.onursahin.domain.model.News
import com.onursahin.domain.repository.SpaceNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}


@ExperimentalCoroutinesApi
class SpaceNewsPagingUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: SpaceNewsRepository
    private lateinit var useCase: SpaceNewsPagingUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = SpaceNewsPagingUseCase(repository, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `invoke returns non empty paging data`() = runTest {
        val dummyNews = listOf(mock<News>())
        whenever(repository.getArticles("")).thenReturn(flowOf(PagingData.from(dummyNews)))

        val resultPagingData = useCase.invoke("").first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = NewsDiffCallback,
            updateCallback = NoopListCallback()
        )
        differ.submitData(resultPagingData)

        assertTrue(differ.snapshot().items.isEmpty().not())
    }

    @Test
    fun `invoke repository returns error`() = runTest {
        val errorMessage = "Repository Error"
        `when`(repository.getArticles(null)).thenReturn(flow { throw Exception(errorMessage) })

        try {
            useCase(null)
        } catch (e: Exception) {
            assert(e.message == errorMessage)
        }

    }

}