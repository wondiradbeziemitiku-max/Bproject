package com.bingoapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.data.model.GameConfig
import com.bingoapp.data.remote.CloudFunctionsService
import com.bingoapp.data.remote.FirestoreService
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val config: GameConfig = GameConfig(),
    val activePlayers: Long = 0,
    val todayRevenue: Long = 0,
    val isUpdating: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val cloudFunctions: CloudFunctionsService,
    private val firestoreService: FirestoreService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        viewModelScope.launch {
            firestoreService.observeGameConfig().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(config = resource.data) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            try {
                cloudFunctions.adminStartGame()
                _uiState.update { it.copy(message = "Game started") }
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Error: ${e.message}") }
            }
        }
    }

    fun cancelGame() {
        viewModelScope.launch {
            val currentGameId = "" // fetch from state
            try {
                cloudFunctions.adminCancelGame(currentGameId)
                _uiState.update { it.copy(message = "Game cancelled") }
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Error: ${e.message}") }
            }
        }
    }

    fun updateConfig(cardPrice: Long, drawInterval: Long, minPlayers: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }
            // update via Firestore directly (admin only)
            val updates = mapOf(
                "cardPriceCoins" to cardPrice,
                "drawIntervalSeconds" to drawInterval,
                "minPlayers" to minPlayers
            )
            try {
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("config").document("global")
                    .update(updates).await()
                _uiState.update { it.copy(isUpdating = false, message = "Config updated") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUpdating = false, message = "Error: ${e.message}") }
            }
        }
    }
}

private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    com.google.firebase.ktx.await(this)
