package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.repository.HistoryRepository
import com.openclassrooms.rebonnte.domain.useCase.medicine.GetHistoriesByMedicineUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class GetHistoriesByMedicineUseCaseTest {

    private lateinit var historyRepository: HistoryRepository
    private lateinit var getHistoriesByMedicineUseCase: GetHistoriesByMedicineUseCase

    @BeforeEach
    fun setUp() {
        historyRepository = mockk()
        getHistoriesByMedicineUseCase = GetHistoriesByMedicineUseCase(historyRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val medicineId = "med_id"
        val histories = listOf(
            History(id = "1", medicineId = medicineId, userId = "user1", dateTime = Date(), isCreation = true)
        )
        val expectedFlow = flowOf(histories)
        every { historyRepository.getHistoriesByMedicineId(medicineId) } returns expectedFlow

        val resultFlow = getHistoriesByMedicineUseCase(medicineId)
        val result = resultFlow.toList()

        assertEquals(listOf(histories), result)
        verify(exactly = 1) { historyRepository.getHistoriesByMedicineId(medicineId) }
    }
}
