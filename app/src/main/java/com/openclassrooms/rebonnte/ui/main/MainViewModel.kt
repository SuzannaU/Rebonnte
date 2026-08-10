package com.openclassrooms.rebonnte.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.exception.AuthException
import com.openclassrooms.rebonnte.domain.exception.NetworkException
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.service.AuthService
import com.openclassrooms.rebonnte.ui.DispatcherProvider
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

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState(isLoading = true))
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
                authUser?.let {
                    userRepository.createUser(
                        User(
                            id = it.uid,
                            username = it.displayName,
                            email = it.email,
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val errorRes = when (e) {
                    is AuthException -> R.string.auth_error
                    is NetworkException -> R.string.network_error
                    else -> R.string.unknown_error
                }
                _uiState.update { it.copy(errorMessageId = errorRes) }
            }
        }
    }
}