package com.openclassrooms.rebonnte.ui

import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.DatabaseException
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetMedicinesOrderedByUseCase
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListScreenState
import com.openclassrooms.rebonnte.ui.medicineList.MedicineListViewModel
import com.openclassrooms.rebonnte.ui.model.SortOption
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var getMedicinesOrderedBy: GetMedicinesOrderedByUseCase
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: MedicineListViewModel

    @BeforeEach
    fun setUp() {
        getMedicinesOrderedBy = mockk()
        dispatcherProvider = TestDispatcherProvider(testDispatcher)
    }

    private fun initViewModel() {
        viewModel = MedicineListViewModel(getMedicinesOrderedBy, dispatcherProvider)
    }

    @Test
    fun `init loads medicines sorted by name ascending`() = runTest(testDispatcher) {
        val medicines = listOf(
            Medicine(id = "1", name = "Aspirin", aisleNumber = "1", currentStock = 10),
            Medicine(id = "2", name = "Paracetamol", aisleNumber = "2", currentStock = 5)
        )
        coEvery { getMedicinesOrderedBy(any()) } returns flowOf(medicines)

        initViewModel()
        backgroundScope.launch { viewModel.listScreenState.collect() }
        runCurrent()

        val state = viewModel.listScreenState.value
        assertTrue(state is MedicineListScreenState.MedicinesFound)
        assertEquals(2, (state as MedicineListScreenState.MedicinesFound).medicines.size)
    }

    @Test
    fun `onSearchQueryChange filters medicines`() = runTest(testDispatcher) {
        val medicines = listOf(
            Medicine(id = "1", name = "Aspirin", aisleNumber = "1", currentStock = 10),
            Medicine(id = "2", name = "Paracetamol", aisleNumber = "2", currentStock = 5)
        )
        coEvery { getMedicinesOrderedBy(any()) } returns flowOf(medicines)
        initViewModel()
        backgroundScope.launch { viewModel.listScreenState.collect() }
        runCurrent()

        viewModel.onSearchQueryChange("asp")
        runCurrent()

        val state = viewModel.listScreenState.value
        assertTrue(state is MedicineListScreenState.MedicinesFound)
        val foundState = state as MedicineListScreenState.MedicinesFound
        assertEquals(1, foundState.medicines.size)
        assertEquals("Aspirin", foundState.medicines[0].name)
    }

    @Test
    fun `onSortOptionSelected triggers reload with new sort`() = runTest(testDispatcher) {
        coEvery { getMedicinesOrderedBy(any()) } returns flowOf(emptyList())
        initViewModel()
        backgroundScope.launch { viewModel.listScreenState.collect() }
        runCurrent()

        viewModel.onSortOptionSelected(SortOption.STOCK_DESCENDING)
        runCurrent()

        assertEquals(SortOption.STOCK_DESCENDING, viewModel.sortOption.value)
    }

    @Test
    fun `flow catch updates uiState to Error`() = runTest(testDispatcher) {
        coEvery { getMedicinesOrderedBy(any()) } returns flow { throw DatabaseException("test") }

        initViewModel()
        backgroundScope.launch { viewModel.listScreenState.collect() }
        runCurrent()

        val state = viewModel.listScreenState.value
        assertTrue(state is MedicineListScreenState.Error)
        assertEquals(R.string.database_error, (state as MedicineListScreenState.Error).message)
    }
}
