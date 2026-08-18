package com.openclassrooms.rebonnte.ui

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.rebonnte.domain.useCase.medicine.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.useCase.aisle.CheckAisleExistsUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.addMedicine.AddMedicineViewModel
import com.openclassrooms.rebonnte.ui.addMedicine.SaveState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class AddMedicineViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var checkAisleExists: CheckAisleExistsUseCase
    private lateinit var addMedicine: AddMedicineUseCase
    private lateinit var testDispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: AddMedicineViewModel

    @BeforeEach
    fun setUp() {
        checkAisleExists = mockk()
        addMedicine = mockk()
        testDispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    @Test
    fun `updateName should update formState`() = runTest(testDispatcher) {
        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle())

        viewModel.updateName("Aspirin")

        assertEquals("Aspirin", viewModel.formState.value.name)
    }

    @Test
    fun `updateAisle should update formState`() = runTest(testDispatcher) {
        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle())

        viewModel.updateAisle("number")

        assertEquals("number", viewModel.formState.value.aisleNumber)
    }

    @Test
    fun `updateStock should update formState`() = runTest(testDispatcher) {
        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle())

        viewModel.updateStock("111")

        assertEquals("111", viewModel.formState.value.currentStock)
    }

    @Test
    fun `onAddMedicine should call addMedicine when validation succeeds and update state`() = runTest(testDispatcher) {
        coEvery { checkAisleExists(any()) } returns DataResult.Success(true)
        coEvery { addMedicine(any()) } returns DataResult.Success(Unit)
        
        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle(mapOf("aisleNumber" to "1")))
        viewModel.updateName("Aspirin")
        viewModel.updateStock("10")

        viewModel.onAddMedicine()

        assertEquals(SaveState.MedicineSaved, viewModel.saveState.value)
        coVerify(exactly = 1) { addMedicine(match { it.name == "Aspirin" && it.aisleNumber == "1" && it.currentStock == 10 }) }
    }

    @Test
    fun `onAddMedicine should call addMedicine when validation succeeds and update state when save fails`() = runTest(testDispatcher) {
        coEvery { checkAisleExists(any()) } returns DataResult.Success(true)
        coEvery { addMedicine(any()) } returns DataResult.Failure(Exception("error"))

        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle(mapOf("aisleNumber" to "1")))
        viewModel.updateName("Aspirin")
        viewModel.updateStock("10")

        viewModel.onAddMedicine()

        assertTrue(viewModel.saveState.value is SaveState.Error)
        coVerify(exactly = 1) { addMedicine(match { it.name == "Aspirin" && it.aisleNumber == "1" && it.currentStock == 10 }) }
    }

    @Test
    fun `onAddMedicine should set error when validation fails (aisle does not exist)`() = runTest(testDispatcher) {
        coEvery { checkAisleExists(any()) } returns DataResult.Success(false)
        
        viewModel = AddMedicineViewModel(checkAisleExists, addMedicine, testDispatcherProvider, SavedStateHandle(mapOf("aisleNumber" to "99")))
        viewModel.updateName("Aspirin")
        viewModel.updateStock("10")

        viewModel.onAddMedicine()

        assertTrue(viewModel.formState.value.formErrors.aisleDoesNotExistError)
        assertEquals(SaveState.Idle, viewModel.saveState.value)
        coVerify(exactly = 0) { addMedicine(any()) }
    }
}
