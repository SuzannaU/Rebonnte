package com.openclassrooms.rebonnte.ui

import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.useCase.aisle.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.aisle.GetAislesUseCase
import com.openclassrooms.rebonnte.domain.useCase.user.LogOutUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.aisleList.AisleListScreenState
import com.openclassrooms.rebonnte.ui.aisleList.AisleListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class AisleListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var getAisles: GetAislesUseCase
    private lateinit var checkAisleExists: CheckAisleExistsUseCase
    private lateinit var addAisle: AddAisleUseCase
    private lateinit var logOut: LogOutUseCase
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: AisleListViewModel

    @BeforeEach
    fun setUp() {
        getAisles = mockk()
        checkAisleExists = mockk()
        addAisle = mockk()
        logOut = mockk()
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    private fun initViewModel() {
        viewModel = AisleListViewModel(getAisles, checkAisleExists, addAisle, logOut, dispatcherProvider)
    }

    @Test
    fun `loadAisles success with data updates uiState to AislesFound`() = runTest(testDispatcher) {
        val aisles = listOf(Aisle("1"), Aisle("2"))
        coEvery { getAisles() } returns flowOf(DataResult.Success(aisles))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is AisleListScreenState.AislesFound)
        assertEquals(2, (state as AisleListScreenState.AislesFound).aisles.size)
    }

    @Test
    fun `loadAisles success with no data updates uiState to NoAisleFound`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))

        initViewModel()

        assertEquals(AisleListScreenState.NoAisleFound, viewModel.uiState.value)
    }

    @Test
    fun `loadAisles error updates uiState to Error`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Failure(DatabaseException("test")))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is AisleListScreenState.Error)
        assertEquals(R.string.database_error, (state as AisleListScreenState.Error).errorMessageId)
    }

    @Test
    fun `onAddAisle blank input sets aisleBlankError`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        initViewModel()

        viewModel.onAddAisle(" ")

        assertTrue(viewModel.addAisleState.value.aisleBlankError)
    }

    @Test
    fun `onAddAisle non-digit input sets aisleDigitError`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        initViewModel()

        viewModel.onAddAisle("12a")

        assertTrue(viewModel.addAisleState.value.aisleDigitError)
    }

    @Test
    fun `onAddAisle existing aisle sets aisleExistsError`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        coEvery { checkAisleExists("1") } returns DataResult.Success(true)
        initViewModel()

        viewModel.onAddAisle("1")

        assertTrue(viewModel.addAisleState.value.aisleExistsError)
    }

    @Test
    fun `onAddAisle checkAisle failure updates state to error`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        coEvery { checkAisleExists("1") } returns DataResult.Failure(DatabaseException("test"))
        initViewModel()

        viewModel.onAddAisle("1")

        val state = viewModel.uiState.value
        assertTrue(state is AisleListScreenState.Error)
        assertEquals(R.string.database_error, (state as AisleListScreenState.Error).errorMessageId)
    }

    @Test
    fun `onAddAisle success updates isSuccess`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        coEvery { checkAisleExists("1") } returns DataResult.Success(false)
        coEvery { addAisle("1") } returns DataResult.Success(Unit)
        initViewModel()

        viewModel.onAddAisle("1")

        assertTrue(viewModel.addAisleState.value.isSuccess)
    }

    @Test
    fun `onAddAisle failure updates uiState error`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        coEvery { checkAisleExists("1") } returns DataResult.Success(false)
        coEvery { addAisle("1") } returns DataResult.Failure(DatabaseException("test"))
        initViewModel()

        viewModel.onAddAisle("1")

        val state = viewModel.uiState.value
        assertTrue(state is AisleListScreenState.Error)
        assertEquals(R.string.database_error, (state as AisleListScreenState.Error).errorMessageId)
    }

    @Test
    fun `resetAddAisleState resets state`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        initViewModel()
        viewModel.onAddAisle(" ")
        
        viewModel.resetAddAisleState()

        assertFalse(viewModel.addAisleState.value.aisleBlankError)
    }

    @Test
    fun `onLogout calls logOut use case`() = runTest(testDispatcher) {
        coEvery { getAisles() } returns flowOf(DataResult.Success(emptyList()))
        coEvery { logOut() } returns Unit

        initViewModel()

        viewModel.onLogout()

        coVerify { logOut() }
    }
}
