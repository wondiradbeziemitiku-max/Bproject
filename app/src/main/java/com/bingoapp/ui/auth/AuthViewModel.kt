package com.bingoapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.domain.usecase.auth.CheckUsernameUseCase
import com.bingoapp.domain.usecase.auth.SendOtpUseCase
import com.bingoapp.domain.usecase.auth.VerifyOtpUseCase
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val verificationId: String? = null,
    val error: String? = null,
    val isVerified: Boolean = false,
    val needsUsername: Boolean = false
)

data class UsernameSetupUiState(
    val isLoading: Boolean = false,
    val isChecking: Boolean = false,
    val isAvailable: Boolean? = null,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _usernameState = MutableStateFlow(UsernameSetupUiState())
    val usernameState: StateFlow<UsernameSetupUiState> = _usernameState.asStateFlow()

    fun sendOtp(phoneNumber: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            when (val result = sendOtpUseCase(phoneNumber)) {
                is Resource.Success -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        verificationId = result.data
                    )
                }
                is Resource.Error -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun verifyOtp(verificationId: String, otp: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null)
            when (val result = verifyOtpUseCase(verificationId, otp)) {
                is Resource.Success -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isVerified = true,
                        needsUsername = true
                    )
                }
                is Resource.Error -> {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun checkUsername(username: String) {
        viewModelScope.launch {
            _usernameState.value = _usernameState.value.copy(isChecking = true, isAvailable = null)
            when (val result = checkUsernameUseCase(username)) {
                is Resource.Success -> {
                    _usernameState.value = _usernameState.value.copy(
                        isChecking = false,
                        isAvailable = result.data
                    )
                }
                is Resource.Error -> {
                    _usernameState.value = _usernameState.value.copy(
                        isChecking = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            _usernameState.value = _usernameState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.setUsername(username)) {
                is Resource.Success -> {
                    _usernameState.value = _usernameState.value.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }
                is Resource.Error -> {
                    _usernameState.value = _usernameState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearError() {
        _loginState.value = _loginState.value.copy(error = null)
        _usernameState.value = _usernameState.value.copy(error = null)
    }

    fun resetLoginState() {
        _loginState.value = LoginUiState()
    }

    fun resetUsernameState() {
        _usernameState.value = UsernameSetupUiState()
    }
}
