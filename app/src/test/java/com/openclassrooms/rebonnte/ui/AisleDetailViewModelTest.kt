package com.openclassrooms.rebonnte.ui

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicinesByAisleUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailScreenState
import com.openclassrooms.rebonnte.ui.aisleDetail.AisleDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class AisleDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var getMedicinesByAisle: GetMedicinesByAisleUseCase
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: AisleDetailViewModel
    private val aisleNumber = "1"

    @BeforeEach
    fun setUp() {
        getMedicinesByAisle = mockk()
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    private fun initViewModel() {
        val savedStateHandle = SavedStateHandle(mapOf("aisleNumber" to aisleNumber))
        viewModel = AisleDetailViewModel(getMedicinesByAisle, dispatcherProvider, savedStateHandle)
    }

    @Test
    fun `loadAisle success updates uiState to AisleFound`() = runTest(testDispatcher) {
        val medicines = listOf(
            Medicine(id = "1", name = "Aspirin", aisleNumber = aisleNumber, currentStock = 10)
        )
        coEvery { getMedicinesByAisle(aisleNumber) } returns flowOf(DataResult.Success(medicines))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is AisleDetailScreenState.AisleFound)
        val foundState = state as AisleDetailScreenState.AisleFound
        assertEquals(aisleNumber, foundState.aisleNumber)
        assertEquals(1, foundState.medicines.size)
        assertEquals("Aspirin", foundState.medicines[0].name)
    }

    @Test
    fun `loadAisle error updates uiState to Error`() = runTest(testDispatcher) {
        coEvery { getMedicinesByAisle(aisleNumber) } returns flowOf(DataResult.Failure(DatabaseException("test")))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is AisleDetailScreenState.Error)
        val errorState = state as AisleDetailScreenState.Error
        assertEquals(R.string.database_error, errorState.errorMessageId)
    }
}
