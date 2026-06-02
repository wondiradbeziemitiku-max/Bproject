package com.bingoapp.ui.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.data.model.Game
import com.bingoapp.data.model.GameConfig
import com.bingoapp.data.model.User
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.domain.repository.UserRepository
import com.bingoapp.domain.usecase.game.JoinGameUseCase
import com.bingoapp.domain.usecase.game.ObserveGameUseCase
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LobbyUiState(
    val gameStatus: String = "idle",
    val playerCount: Long = 0,
    val countdownSeconds: Long = 30,
    val coinBalance: Long = 0,
    val hasNewPlayerBonus: Boolean = false,
    val cardPrice: Long = 10,
    val isJoining: Boolean = false,
    val hasJoined: Boolean = false,
    val currentGameId: String? = null,
    val recentPlayers: List<String> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val observeGameUseCase: ObserveGameUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    init {
        observeCurrentGame()
        observeUser()
        observeConfig()
    }

    private fun observeCurrentGame() {
        viewModelScope.launch {
            observeGameUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val game = resource.data
                        _uiState.update {
                            it.copy(
                                gameStatus = game.status,
                                playerCount = game.playerCount,
                                countdownSeconds = game.countdownSeconds,
                                currentGameId = game.gameId.takeIf { id -> id.isNotEmpty() }
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = resource.message) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            userRepository.observeCurrentUser().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val user = resource.data
                        _uiState.update {
                            it.copy(
                                coinBalance = user.coins,
                                hasNewPlayerBonus = !user.hasClaimedWelcomeBonus
                            )
                        }
                    }
                    is Resource.Error -> {
                        // ignore
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun observeConfig() {
        viewModelScope.launch {
            gameRepository.observeGameConfig().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val config = resource.data
                        _uiState.update {
                            it.copy(
                                cardPrice = config.cardPriceCoins,
                                hasNewPlayerBonus = config.welcomeBonusEnabled && it.hasNewPlayerBonus
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun joinGame() {
        val gameId = _uiState.value.currentGameId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isJoining = true, error = null) }
            when (val result = joinGameUseCase(gameId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isJoining = false,
                            hasJoined = true,
                            coinBalance = it.coinBalance - (if (it.hasNewPlayerBonus) 0 else it.cardPrice)
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isJoining = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
