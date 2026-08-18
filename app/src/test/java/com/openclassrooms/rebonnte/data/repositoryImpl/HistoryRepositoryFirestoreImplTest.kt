package com.openclassrooms.rebonnte.data.repositoryImpl

import com.google.firebase.Timestamp
import com.openclassrooms.rebonnte.data.datasource.HistoryDataSource
import com.openclassrooms.rebonnte.data.dto.HistoryDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

        assertEquals(2, result.size)
        assertEquals("h1", result[0].id)
        assertEquals(true, result[0].isCreation)
        assertEquals("h2", result[1].id)
        assertEquals(false, result[1].isCreation)
        verify(exactly = 1) { historyDataSource.getHistoriesByMedicineId(match { it == medicineId }) }
    }

    @Test
    fun `getHistoriesByMedicineId throws when datasource fails`() = runTest {
        val medicineId = "med1"
        every { historyDataSource.getHistoriesByMedicineId(medicineId) } throws Exception()

        assertThrows<Exception> { historyRepository.getHistoriesByMedicineId(medicineId).first() }
        verify(exactly = 1) { historyDataSource.getHistoriesByMedicineId(match { it == medicineId }) }
    }
}
