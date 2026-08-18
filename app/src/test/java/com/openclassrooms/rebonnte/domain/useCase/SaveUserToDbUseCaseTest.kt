package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveUserToDbUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var saveUserToDbUseCase: SaveUserToDbUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        saveUserToDbUseCase = SaveUserToDbUseCase(userRepository)
    }

    @Test
    fun `invoke should call createUser on repository`() = runTest {
        val user = User(id = "user1", email = "test@test.com", username = "tester")
        val expectedResult = DataResult.Success(Unit)
        coEvery { userRepository.createUser(user) } returns expectedResult

        val result = saveUserToDbUseCase(user)

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { userRepository.createUser(user) }
    }
}
