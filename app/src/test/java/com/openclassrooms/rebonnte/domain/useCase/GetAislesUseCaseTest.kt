package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAislesUseCaseTest {

    private lateinit var aisleRepository: AisleRepository
    private lateinit var getAislesUseCase: GetAislesUseCase

    @BeforeEach
    fun setUp() {
        aisleRepository = mockk()
        getAislesUseCase = GetAislesUseCase(aisleRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        val aisles = listOf(Aisle(number = "1"), Aisle(number = "2"))
        val expectedFlow = flowOf(aisles)
        every { aisleRepository.getAisles() } returns expectedFlow

        val resultFlow = getAislesUseCase()
        val result = resultFlow.toList()

        assertEquals(listOf(aisles), result)
        verify(exactly = 1) { aisleRepository.getAisles() }
    }
}
