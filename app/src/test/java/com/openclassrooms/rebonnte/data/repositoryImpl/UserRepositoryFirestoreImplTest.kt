package com.openclassrooms.rebonnte.data.repositoryImpl

import com.openclassrooms.rebonnte.data.datasource.UserDataSource
import com.openclassrooms.rebonnte.data.dto.UserDto
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.util.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryFirestoreImplTest {

    private lateinit var userDataSource: UserDataSource
    private lateinit var userRepository: UserRepositoryFirestoreImpl

    @BeforeEach
    fun setUp() {
        userDataSource = mockk()
        userRepository = UserRepositoryFirestoreImpl(userDataSource)
    }

    @Test
    fun `getUserById returns success with user when user exists`() = runTest {
        val userId = "123"
        val userDto = UserDto(id = userId, username = "testuser", email = "test@example.com")
        coEvery { userDataSource.getUserById(userId) } returns userDto

        val result = userRepository.getUserById(userId)

        assertTrue(result is DataResult.Success)
        assertEquals(userId, (result as DataResult.Success).data?.id)
        assertEquals("testuser", result.data?.username)
        coVerify(exactly = 1) { userDataSource.getUserById(userId) }
    }

    @Test
    fun `getUserById returns success with null when user does not exist`() = runTest {
        val userId = "123"
        coEvery { userDataSource.getUserById(userId) } returns null

        val result = userRepository.getUserById(userId)

        assertTrue(result is DataResult.Success)
        assertEquals(null, (result as DataResult.Success).data)
        coVerify(exactly = 1) { userDataSource.getUserById(userId) }
    }

    @Test
    fun `createUser returns Success when dataSource succeeds`() = runTest {
        val user = User(id = "123", username = "testuser", email = "test@example.com")
        coEvery { userDataSource.saveUser(any()) } returns Unit

        val result = userRepository.createUser(user)

        assertTrue(result is DataResult.Success)
        coVerify(exactly = 1) { userDataSource.saveUser(match { it.id == "123" && it.username == "testuser" }) }
    }

    @Test
    fun `createUser returns Failure when dataSource fails`() = runTest {
        val user = User(id = "123", username = "testuser", email = "test@example.com")
        coEvery { userDataSource.saveUser(any()) } throws Exception()

        val result = userRepository.createUser(user)

        assertTrue(result is DataResult.Failure)
        coVerify(exactly = 1) { userDataSource.saveUser(match { it.id == "123" && it.username == "testuser" }) }
    }
}
