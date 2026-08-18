package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.useCase.aisle.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddAisleUseCaseTest {

    private lateinit var aisleRepository: AisleRepository
    private lateinit var addAisleUseCase: AddAisleUseCase

    @BeforeEach
    fun setUp() {
        aisleRepository = mockk()
        addAisleUseCase = AddAisleUseCase(aisleRepository)
    }

    @Test
    fun `invoke should call addAisle on repository with correct Aisle object`() = runTest {
        val aisleNumber = "12"
        val aisle = Aisle(number = aisleNumber)
        val expectedResult = DataResult.Success(Unit)
        coEvery { aisleRepository.addAisle(aisle) } returns expectedResult

        val result = addAisleUseCase(aisleNumber)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { aisleRepository.addAisle(aisle) }
    }
}
