package com.openclassrooms.rebonnte.domain.useCase

import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUsernameByIdUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getUsernameByIdUseCase: GetUsernameByIdUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        getUsernameByIdUseCase = GetUsernameByIdUseCase(userRepository)
    }

    @Test
    fun `invoke should return Success(username) when user exists`() = runTest {
        val userId = "user1"
        val username = "tester"
        coEvery { userRepository.getUserById(userId) } returns DataResult.Success(User(id = userId, email = "t@t.com", username = username))

        val result = getUsernameByIdUseCase(userId)

        assertEquals(DataResult.Success(username), result)
    }

    @Test
    fun `invoke should return Success(null) when user does not exist`() = runTest {
        val userId = "user1"
        coEvery { userRepository.getUserById(userId) } returns DataResult.Success(null)

        val result = getUsernameByIdUseCase(userId)

        assertEquals(DataResult.Success(null), result)
    }

    @Test
    fun `invoke should return Failure when repository fails`() = runTest {
        val userId = "user1"
        val exception = Exception("Error")
        coEvery { userRepository.getUserById(userId) } returns DataResult.Failure(exception)

        val result = getUsernameByIdUseCase(userId)

        assertTrue(result is DataResult.Failure)
    }
}
