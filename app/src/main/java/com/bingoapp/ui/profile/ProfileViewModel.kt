package com.bingoapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.data.model.Transaction
import com.bingoapp.data.model.User
import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.domain.repository.UserRepository
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val username: String = "",
    val phoneNumber: String = "",
    val coinBalance: Long = 0,
    val totalGamesPlayed: Long = 0,
    val totalWins: Long = 0,
    val winRate: Float = 0f,
    val totalCoinsWon: Long = 0,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadTransactions()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            userRepository.observeCurrentUser().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val user = resource.data
                        val winRate = if (user.totalGamesPlayed > 0) 
                            (user.totalWins.toFloat() / user.totalGamesPlayed * 100).toFloat() 
                        else 0f
                        _uiState.update {
                            it.copy(
                                username = user.username,
                                phoneNumber = user.phoneNumber,
                                coinBalance = user.coins,
                                totalGamesPlayed = user.totalGamesPlayed,
                                totalWins = user.totalWins,
                                winRate = winRate,
                                totalCoinsWon = user.totalCoinsWon,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            userRepository.getTransactionHistory(50).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(transactions = resource.data) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
