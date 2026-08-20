package com.openclassrooms.rebonnte.ui

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.UpdatableFields
import com.openclassrooms.rebonnte.domain.model.UpdatedField
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicineByIdUseCase
import com.openclassrooms.rebonnte.domain.useCase.medicine.UpdateMedicineUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.medicineDetail.EditMedicineViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class EditMedicineViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var getMedicineById: GetMedicineByIdUseCase
    private lateinit var updateMedicine: UpdateMedicineUseCase
    private lateinit var checkAisleExists: CheckAisleExistsUseCase
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: EditMedicineViewModel
    private val medicineId = "med1"
    private val medicine = Medicine(id = medicineId, name = "Aspirin", aisleNumber = "1", currentStock = 10)

    @BeforeEach
    fun setUp() {
        getMedicineById = mockk()
        updateMedicine = mockk()
        checkAisleExists = mockk()
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    private fun initViewModel() {
        val savedStateHandle = SavedStateHandle(mapOf("medicineId" to medicineId))
        coEvery { getMedicineById(medicineId) } returns flowOf(DataResult.Success(medicine))
        viewModel = EditMedicineViewModel(getMedicineById, updateMedicine, checkAisleExists, dispatcherProvider, savedStateHandle)
    }

    @Test
    fun `init loads initial medicine data`() = runTest(testDispatcher) {
        initViewModel()

        val state = viewModel.formState.value
        assertEquals("Aspirin", state.name)
        assertEquals("1", state.aisleNumber)
        assertEquals("10", state.stock)
    }

    @Test
    fun `onSaveName blank input sets nameBlankError`() = runTest(testDispatcher) {
        initViewModel()
        viewModel.onSaveName(" ")
        assertTrue(viewModel.formState.value.formError.nameBlankError)
    }

    @Test
    fun `onSaveName too long input sets nameLengthError`() = runTest(testDispatcher) {
        initViewModel()
        viewModel.onSaveName("A very long name that exceeds twenty five characters")
        assertTrue(viewModel.formState.value.formError.nameLengthError)
    }

    @Test
    fun `onSaveName success updates isSuccess`() = runTest(testDispatcher) {
        initViewModel()
        val updatedField = UpdatedField(UpdatableFields.NAME, "Aspirin", "Paracetamol")
        coEvery { updateMedicine(medicineId, updatedField) } returns DataResult.Success(Unit)

        viewModel.onSaveName("Paracetamol")

        assertTrue(viewModel.formState.value.isSuccess)
        coVerify { updateMedicine(medicineId, updatedField) }
    }

    @Test
    fun `onSaveAisle invalid input sets aisleDigitError`() = runTest(testDispatcher) {
        initViewModel()
        viewModel.onSaveAisle("1a")
        assertTrue(viewModel.formState.value.formError.aisleDigitError)
    }

    @Test
    fun `onSaveAisle non-existent aisle sets aisleDoesNotExistError`() = runTest(testDispatcher) {
        initViewModel()
        coEvery { checkAisleExists("2") } returns DataResult.Success(false)

        viewModel.onSaveAisle("2")

        assertTrue(viewModel.formState.value.formError.aisleDoesNotExistError)
    }

    @Test
    fun `onSaveAisle success updates isSuccess`() = runTest(testDispatcher) {
        initViewModel()
        coEvery { checkAisleExists("2") } returns DataResult.Success(true)
        val updatedField = UpdatedField(UpdatableFields.AISLE, "1", "2")
        coEvery { updateMedicine(medicineId, updatedField) } returns DataResult.Success(Unit)

        viewModel.onSaveAisle("2")

        assertTrue(viewModel.formState.value.isSuccess)
    }

    @Test
    fun `onSaveStock invalid input sets stockBlankError`() = runTest(testDispatcher) {
        initViewModel()
        viewModel.onSaveStock("abc")
        assertTrue(viewModel.formState.value.formError.stockBlankError)
    }

    @Test
    fun `onSaveStock success updates isSuccess`() = runTest(testDispatcher) {
        initViewModel()
        val updatedField = UpdatedField(UpdatableFields.STOCK, "10", "20")
        coEvery { updateMedicine(medicineId, updatedField) } returns DataResult.Success(Unit)

        viewModel.onSaveStock("20")

        assertTrue(viewModel.formState.value.isSuccess)
    }

    @Test
    fun `updateField failure sets errorId`() = runTest(testDispatcher) {
        initViewModel()
        val updatedField = UpdatedField(UpdatableFields.NAME, "Aspirin", "Paracetamol")
        coEvery { updateMedicine(medicineId, updatedField) } returns DataResult.Failure(DatabaseException("test"))

        viewModel.onSaveName("Paracetamol")

        assertEquals(R.string.database_error, viewModel.formState.value.errorId)
    }

    @Test
    fun `resetSuccessState resets success and errors`() = runTest(testDispatcher) {
        initViewModel()
        coEvery { checkAisleExists("2") } returns DataResult.Success(false)
        viewModel.onSaveAisle("2")
        
        viewModel.resetSuccessState()

        val state = viewModel.formState.value
        assertFalse(state.isSuccess)
        assertFalse(state.formError.aisleDoesNotExistError)
        assertNull(state.errorId)
    }
}
