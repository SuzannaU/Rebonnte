package com.openclassrooms.rebonnte.ui

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.useCase.medicine.ArchiveMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetHistoriesByMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicineByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.user.GetUsernameByIdUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailState
import com.openclassrooms.rebonnte.ui.medicineDetail.MedicineDetailViewModel
import com.openclassrooms.rebonnte.ui.util.UiText
import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var getMedicineById: GetMedicineByIdUseCase
    private lateinit var getHistoriesByMedicine: GetHistoriesByMedicineUseCase
    private lateinit var getUsernameById: GetUsernameByIdUseCase
    private lateinit var archiveMedicine: ArchiveMedicineUseCase
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: MedicineDetailViewModel
    private val medicineId = "med1"

    @BeforeEach
    fun setUp() {
        getMedicineById = mockk()
        getHistoriesByMedicine = mockk()
        getUsernameById = mockk()
        archiveMedicine = mockk()
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    private fun initViewModel() {
        val savedStateHandle = SavedStateHandle(mapOf("medicineId" to medicineId))
        viewModel = MedicineDetailViewModel(
            getMedicineById, getHistoriesByMedicine, getUsernameById, archiveMedicine, dispatcherProvider, savedStateHandle
        )
    }

    @Test
    fun `loadMedicine success updates uiState to MedicineFound`() = runTest(testDispatcher) {
        val medicine = Medicine(id = medicineId, name = "Aspirin", aisleNumber = "1", currentStock = 10)
        val histories = listOf(History(medicineId = medicineId, userId = "user1", dateTime = Date(), isCreation = true))
        
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(medicine))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(histories))
        coEvery { getUsernameById("user1") } returns DataResult.Success("John Doe")

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is MedicineDetailState.MedicineFound)
        val foundState = state as MedicineDetailState.MedicineFound
        assertEquals("Aspirin", foundState.medicine.name)
        assertEquals(1, foundState.histories.size)
        assertEquals("John Doe", (foundState.histories[0].username as UiText.RawString).value)
    }

    @Test
    fun `loadMedicine success with unknown user updates uiState to MedicineFound`() = runTest(testDispatcher) {
        val medicine = Medicine(id = medicineId, name = "Aspirin", aisleNumber = "1", currentStock = 10)
        val histories = listOf(History(medicineId = medicineId, userId = "user1", dateTime = Date(), isCreation = true))

        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(medicine))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(histories))
        coEvery { getUsernameById("user1") } returns DataResult.Failure(DatabaseException("test"))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is MedicineDetailState.MedicineFound)
        val foundState = state as MedicineDetailState.MedicineFound
        assertEquals("Aspirin", foundState.medicine.name)
        assertEquals(1, foundState.histories.size)
        assertEquals(R.string.unknown_user, (foundState.histories[0].username as UiText.StringResource).resId)
    }

    @Test
    fun `loadMedicine with null medicine updates uiState to MedicineNotFound`() = runTest(testDispatcher) {
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(null))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(emptyList()))

        initViewModel()

        assertEquals(MedicineDetailState.MedicineNotFound, viewModel.uiState.value)
    }

    @Test
    fun `loadMedicine error updates uiState to Error`() = runTest(testDispatcher) {
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Failure(DatabaseException("test")))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(emptyList()))

        initViewModel()

        val state = viewModel.uiState.value
        assertTrue(state is MedicineDetailState.Error)
        assertEquals(R.string.database_error, (state as MedicineDetailState.Error).messageId)
    }

    @Test
    fun `onArchiveClick success calls use case`() = runTest(testDispatcher) {
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(null))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(emptyList()))
        coEvery { archiveMedicine(medicineId) } returns DataResult.Success(Unit)
        initViewModel()

        viewModel.onArchiveClick()

        coVerify { archiveMedicine(medicineId) }
    }

    @Test
    fun `onArchiveClick failure sets error message`() = runTest(testDispatcher) {
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(null))
        coEvery { getHistoriesByMedicine(medicineId) } returns flowOf(DataResult.Success(emptyList()))
        coEvery { archiveMedicine(medicineId) } returns DataResult.Failure(DatabaseException("test"))
        initViewModel()

        viewModel.onArchiveClick()

        val state = viewModel.uiState.value
        assertTrue(state is MedicineDetailState.Error)
        assertEquals(R.string.database_error, (state as MedicineDetailState.Error).messageId)
    }
}
