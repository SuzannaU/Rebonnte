package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.MedicineDataSource
import com.openclassrooms.rebonnte.data.dto.MedicineDto
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.MedicineSortOption
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

class MedicineRepositoryFirestoreImplTest {

    private lateinit var medicineDataSource: MedicineDataSource
    private lateinit var medicineRepository: MedicineRepositoryFirestoreImpl

    @BeforeEach
    fun setUp() {
        medicineDataSource = mockk()
        medicineRepository = MedicineRepositoryFirestoreImpl(medicineDataSource)
    }

    @Test
    fun `getMedicineById returns flow of mapped medicine`() = runTest {
        val medicineId = "med1"
        val medicineDto = MedicineDto(id = medicineId, name = "Med A")
        every { medicineDataSource.getMedicineById(medicineId) } returns flowOf(medicineDto)

        val result = medicineRepository.getMedicineById(medicineId).first()

        assertEquals("Med A", result?.name)
        assertEquals(medicineId, result?.id)
        verify { medicineDataSource.getMedicineById(match { it == medicineId }) }
    }

    @Test
    fun `getMedicineById returns flow of null when not found`() = runTest {
        val medicineId = "med1"
        every { medicineDataSource.getMedicineById(medicineId) } returns flowOf(null)

        val result = medicineRepository.getMedicineById(medicineId).first()

        assertEquals(null, result)
        verify { medicineDataSource.getMedicineById(match { it == medicineId }) }
    }

    @Test
    fun `getMedicineById throws when datasource fails`() = runTest {
        val medicineId = "med1"
        every { medicineDataSource.getMedicineById(medicineId) } throws Exception()

        assertThrows<Exception> { medicineRepository.getMedicineById(medicineId).first() }

        verify { medicineDataSource.getMedicineById(match { it == medicineId }) }
    }

    @Test
    fun `getMedicinesOrderedBy returns mapped medicines`() = runTest {
        val sortOption = MedicineSortOption.NAME_ASCENDING
        val medicineDtos = listOf(
            MedicineDto(id = "1", name = "A"),
            MedicineDto(id = "2", name = "B")
        )
        every { medicineDataSource.getUnarchivedMedicinesOrderedBy(sortOption) } returns flowOf(
            medicineDtos
        )

        val result = medicineRepository.getMedicinesOrderedBy(sortOption).first()

        assertEquals(2, result.size)
        assertEquals("A", result[0].name)
        assertEquals("B", result[1].name)
        verify { medicineDataSource.getUnarchivedMedicinesOrderedBy(match { it == sortOption }) }
    }

    @Test
    fun `getMedicinesOrderedBy throws when datasource fails`() = runTest {
        val sortOption = MedicineSortOption.NAME_ASCENDING
        every { medicineDataSource.getUnarchivedMedicinesOrderedBy(sortOption) } throws Exception()

        assertThrows<Exception> { medicineRepository.getMedicinesOrderedBy(sortOption).first() }

        verify { medicineDataSource.getUnarchivedMedicinesOrderedBy(match { it == sortOption }) }
    }

    @Test
    fun `getMedicinesByAisleNumber returns mapped medicines`() = runTest {
        val medicineDtos = listOf(
            MedicineDto(id = "1", name = "A"),
            MedicineDto(id = "2", name = "B")
        )
        every { medicineDataSource.getMedicinesByAisleNumber(any()) } returns flowOf(
            medicineDtos
        )

        val result = medicineRepository.getMedicinesByAisleNumber("10").first()

        assertEquals(2, result.size)
        verify { medicineDataSource.getMedicinesByAisleNumber(any()) }
    }

    @Test
    fun `getMedicinesByAisleNumber throws when datasource fails`() = runTest {
        every { medicineDataSource.getMedicinesByAisleNumber(any()) } throws Exception()

        assertThrows<Exception> { medicineRepository.getMedicinesByAisleNumber("10").first() }

        verify { medicineDataSource.getMedicinesByAisleNumber(any()) }
    }

    @Test
    fun `addMedicineWithHistory returns Success when dataSource succeeds`() = runTest {
        val medicine = Medicine(id = "1", name = "A", aisleNumber = "1", currentStock = 10)
        val history = History(
            id = "h1",
            medicineId = "1",
            userId = "u1",
            dateTime = Date(),
            isCreation = true
        )
        coEvery { medicineDataSource.addMedicineWithHistory(any(), any()) } returns Unit

        val result = medicineRepository.addMedicineWithHistory(medicine, history)

        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { medicineDataSource.addMedicineWithHistory(any(), any()) }
    }

    @Test
    fun `addMedicineWithHistory returns Failure when dataSource fails`() = runTest {
        val medicine = Medicine(id = "1", name = "A", aisleNumber = "1", currentStock = 10)
        val history = History(
            id = "h1",
            medicineId = "1",
            userId = "u1",
            dateTime = Date(),
            isCreation = true
        )
        coEvery { medicineDataSource.addMedicineWithHistory(any(), any()) } throws Exception()

        val result = medicineRepository.addMedicineWithHistory(medicine, history)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { medicineDataSource.addMedicineWithHistory(any(), any()) }
    }

    @Test
    fun `updateMedicineWithHistory returns Success when dataSource succeeds`() = runTest {
        val medicineId = "1"
        val history = History(id = "h1", medicineId = "1", userId = "u1", dateTime = Date())
        coEvery { medicineDataSource.updateMedicineWithHistory(any(), any()) } returns Unit

        val result = medicineRepository.updateMedicineWithHistory(medicineId, history)

        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { medicineDataSource.updateMedicineWithHistory(any(), any()) }
    }

    @Test
    fun `updateMedicineWithHistory returns Failure when dataSource fails`() = runTest {
        val medicineId = "1"
        val history = History(id = "h1", medicineId = "1", userId = "u1", dateTime = Date())
        coEvery { medicineDataSource.updateMedicineWithHistory(any(), any()) } throws Exception()

        val result = medicineRepository.updateMedicineWithHistory(medicineId, history)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { medicineDataSource.updateMedicineWithHistory(medicineId, any()) }
    }

    @Test
    fun `archiveMedicineById returns Success when dataSource succeeds`() = runTest {
        val medicineId = "1"
        val history = History(medicineId = "1", userId = "u1", dateTime = Date(), isArchiving = true)
        coEvery { medicineDataSource.archiveMedicineWithHistory(any(), any()) } returns Unit

        val result = medicineRepository.archiveMedicineWithHistory(medicineId, history)

        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { medicineDataSource.archiveMedicineWithHistory(medicineId, any()) }
    }

    @Test
    fun `archiveMedicineById returns Failure when dataSource fails`() = runTest {
        val medicineId = "1"
        val history = History(medicineId = "1", userId = "u1", dateTime = Date(), isArchiving = true)
        coEvery { medicineDataSource.archiveMedicineWithHistory(any(), any()) } throws Exception()

        val result = medicineRepository.archiveMedicineWithHistory(medicineId, history)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { medicineDataSource.archiveMedicineWithHistory(medicineId, any()) }
    }
}
