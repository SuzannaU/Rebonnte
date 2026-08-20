package com.openclassrooms.rebonnte.data.repositoryImpl

import com.google.firebase.Timestamp
import com.openclassrooms.rebonnte.data.datasource.HistoryDataSource
import com.openclassrooms.rebonnte.data.dto.HistoryDto
import com.openclassrooms.rebonnte.domain.exception.UnknownException
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class HistoryRepositoryFirestoreImplTest {

    private lateinit var historyDataSource: HistoryDataSource
    private lateinit var historyRepository: HistoryRepositoryFirestoreImpl

    @BeforeEach
    fun setUp() {
        historyDataSource = mockk()
        historyRepository = HistoryRepositoryFirestoreImpl(historyDataSource)
    }

    @Test
    fun `getHistoriesByMedicineId returns flow of mapped histories`() = runTest {
        val medicineId = "med1"
        val now = Date()
        val historyDtos = listOf(
            HistoryDto(id = "h1", medicineId = medicineId, dateTime = Timestamp(now), creation = true),
            HistoryDto(id = "h2", medicineId = medicineId, dateTime = Timestamp(now))
        )
        every { historyDataSource.getHistoriesByMedicineId(medicineId) } returns flowOf(historyDtos)

        val result = historyRepository.getHistoriesByMedicineId(medicineId).first()

        assertTrue(result is DataResult.Success)
        val data = (result as DataResult.Success).data
        assertEquals(2, data.size)
        assertEquals("h1", data[0].id)
        assertEquals(true, data[0].isCreation)
        assertEquals("h2", data[1].id)
        assertEquals(false, data[1].isCreation)
        verify(exactly = 1) { historyDataSource.getHistoriesByMedicineId(match { it == medicineId }) }
    }

    @Test
    fun `getHistoriesByMedicineId returns failure when datasource fails`() = runTest {
        val medicineId = "med1"
        val exception = Exception("test")
        every { historyDataSource.getHistoriesByMedicineId(medicineId) } returns flow { throw exception }

        val result = historyRepository.getHistoriesByMedicineId(medicineId).first()

        assertTrue(result is DataResult.Failure)
        assertTrue((result as DataResult.Failure).exception is UnknownException)
        assertEquals("test", result.exception.message)
        verify(exactly = 1) { historyDataSource.getHistoriesByMedicineId(match { it == medicineId }) }
    }
}
