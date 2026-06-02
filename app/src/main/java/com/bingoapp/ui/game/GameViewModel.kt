package com.bingoapp.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.data.model.DrawnNumber
import com.bingoapp.data.model.PlayerInGame
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.domain.usecase.game.CallBingoUseCase
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val cardNumbers: List<List<Any>> = emptyList(),
    val daubedNumbers: List<Long> = emptyList(),
    val drawnNumbers: List<Long> = emptyList(),
    val latestNumber: Long? = null,
    val canCallBingo: Boolean = true,
    val callingBingo: Boolean = false,
    val bingoCooldown: Int = 0,
    val gameEnded: Boolean = false,
    val winnerInfo: String = "",
    val error: String? = null
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val callBingoUseCase: CallBingoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameId: String = ""
    private var observeJob: Job? = null

    fun init(gameId: String) {
        this.gameId = gameId
        observeCard()
        observeDrawnNumbers()
    }

    private fun observeCard() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            gameRepository.observeMyCard(gameId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val player = resource.data
                        _uiState.update {
                            it.copy(
                                cardNumbers = player.cardNumbers,
                                daubedNumbers = player.daubedNumbers
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

    private fun observeDrawnNumbers() {
        viewModelScope.launch {
            gameRepository.observeDrawnNumbers(gameId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val numbers = resource.data.map { it.number }
                        _uiState.update {
                            it.copy(
                                drawnNumbers = numbers,
                                latestNumber = numbers.lastOrNull()
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

    fun manualDaub(number: Int) {
        // manual daub is just for UI feedback; actual daubing is server-side
        // but we can optimistically add it
        if (!_uiState.value.daubedNumbers.contains(number.toLong())) {
            _uiState.update {
                it.copy(daubedNumbers = it.daubedNumbers + number)
            }
        }
    }

    fun callBingo() {
        if (_uiState.value.callingBingo || !_uiState.value.canCallBingo) return
        viewModelScope.launch {
            _uiState.update { it.copy(callingBingo = true, error = null) }
            when (val result = callBingoUseCase(gameId)) {
                is Resource.Success -> {
                    val isWinner = result.data
                    if (isWinner) {
                        _uiState.update { it.copy(gameEnded = true, winnerInfo = "You Won!") }
                    } else {
                        startCooldown()
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = result.message) }
                    startCooldown()
                }
                is Resource.Loading -> {}
            }
            _uiState.update { it.copy(callingBingo = false) }
        }
    }

    private fun startCooldown() {
        viewModelScope.launch {
            for (i in 5 downTo 1) {
                _uiState.update { it.copy(bingoCooldown = i, canCallBingo = false) }
                delay(1000)
            }
            _uiState.update { it.copy(bingoCooldown = 0, canCallBingo = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        observeJob?.cancel()
    }
}
