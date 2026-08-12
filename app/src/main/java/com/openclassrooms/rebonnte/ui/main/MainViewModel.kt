package com.openclassrooms.rebonnte.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.ui.DispatcherProvider
import com.openclassrooms.rebonnte.ui.util.toErrorMessageId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: DispatcherProvider,
    private val userRepository: UserRepository,
    private val authService: AuthService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch(dispatcher.main) {
            authService.authState
                .catch { _uiState.value = _uiState.value.copy(isAuthConnected = false) }
                .collectLatest { uid ->
                    _uiState.value = _uiState.value.copy(
                        isUserAuthenticated = uid != null,
                        userId = uid,
                        isAuthConnected = true,
                        isLoading = false
                    )
                }
        }
    }

    fun createUser() {
        viewModelScope.launch(dispatcher.io) {
            try {
                val authUser = authService.getAuthUser()
                userRepository.createUser(
                    User(
                        id = authUser.uid,
                        username = authUser.displayName,
                        email = authUser.email,
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(errorMessageId = e.toErrorMessageId()) }
            }
        }
    }
}