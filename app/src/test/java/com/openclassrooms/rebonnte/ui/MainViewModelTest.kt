package com.openclassrooms.rebonnte.ui

import com.openclassrooms.rebonnte.domain.model.AuthUser
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.domain.useCase.user.CheckUserExistsUseCase
import com.openclassrooms.rebonnte.domain.useCase.user.SaveUserToDbUseCase
import com.openclassrooms.rebonnte.domain.util.DataResult
import com.openclassrooms.rebonnte.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension(testDispatcher)

    private lateinit var authService: AuthService
    private lateinit var saveUserToDb: SaveUserToDbUseCase
    private lateinit var checkUserExists: CheckUserExistsUseCase
    private lateinit var testDispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: MainViewModel

    private val authStateFlow = MutableStateFlow<String?>(null)

    @BeforeEach
    fun setUp() {
        authService = mockk()
        saveUserToDb = mockk()
        checkUserExists = mockk()
        testDispatcherProvider = TestDispatcherProvider(testDispatcher)

        every { authService.authState } returns authStateFlow
    }

    @Test
    fun `init should observe auth state and update uiState`() = runTest(testDispatcher) {
        authStateFlow.value = "user123"

        viewModel =
            MainViewModel(testDispatcherProvider, saveUserToDb, checkUserExists, authService)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isUserAuthenticated)
        assertEquals("user123", state.userId)
        assertTrue(state.isAuthConnected)
    }

    @Test
    fun `saveNewUserToDb should call saveUserToDb when user does not exist`() =
        runTest(testDispatcher) {
            val authUser =
                AuthUser(uid = "user123", email = "test@test.com", displayName = "Tester")
            every { authService.getAuthUser() } returns authUser
            coEvery { checkUserExists(authUser.uid) } returns DataResult.Success(false)
            coEvery { saveUserToDb(any()) } returns DataResult.Success(Unit)

            viewModel =
                MainViewModel(testDispatcherProvider, saveUserToDb, checkUserExists, authService)

            viewModel.saveNewUserToDb()

            coVerify(exactly = 1) { saveUserToDb(match { it.id == authUser.uid }) }
        }

    @Test
    fun `saveNewUserToDb should call saveUserToDb when user does not exist and update state if creation fails`() =
        runTest(testDispatcher) {
            val authUser =
                AuthUser(uid = "user123", email = "test@test.com", displayName = "Tester")
            every { authService.getAuthUser() } returns authUser
            coEvery { checkUserExists(authUser.uid) } returns DataResult.Success(false)
            coEvery { saveUserToDb(any()) } returns DataResult.Failure(Exception())

            viewModel =
                MainViewModel(testDispatcherProvider, saveUserToDb, checkUserExists, authService)

            viewModel.saveNewUserToDb()

            val state = viewModel.uiState
            assertNotNull(state.value.errorMessageId)
            coVerify(exactly = 1) { saveUserToDb(match { it.id == authUser.uid }) }
        }

    @Test
    fun `saveNewUserToDb should not call saveUserToDb when user already exists`() =
        runTest(testDispatcher) {
            val authUser =
                AuthUser(uid = "user123", email = "test@test.com", displayName = "Tester")
            every { authService.getAuthUser() } returns authUser
            coEvery { checkUserExists(authUser.uid) } returns DataResult.Success(true)

            viewModel =
                MainViewModel(testDispatcherProvider, saveUserToDb, checkUserExists, authService)

            viewModel.saveNewUserToDb()

            coVerify(exactly = 0) { saveUserToDb(any()) }
        }
}
