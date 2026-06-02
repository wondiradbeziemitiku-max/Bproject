package com.bingoapp.ui.winner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingoapp.data.model.Game
import com.bingoapp.data.model.User
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.domain.repository.UserRepository
import com.bingoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WinnerUiState(
    val winnerUsername: String = "",
    val winningLine: String = "",
    val prizeAmount: Long = 0,
    val isUserWinner: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class WinnerViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WinnerUiState())
    val uiState: StateFlow<WinnerUiState> = _uiState.asStateFlow()

    fun loadWinnerInfo(gameId: String) {
        viewModelScope.launch {
            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            // In a real app, fetch game details from Firestore or a dedicated function
            // For now, we'll rely on a Cloud Function or a direct get
            try {
                val gameDoc = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("games").document(gameId).get().await()
                val game = gameDoc.toObject<Game>()
                if (game != null) {
                    val isUserWinner = game.winnerUid == currentUserId
                    var username = "Unknown"
                    if (game.winnerUid.isNotEmpty()) {
                        val userDoc = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            .collection("users").document(game.winnerUid).get().await()
                        username = userDoc.getString("username") ?: "Unknown"
                    }
                    _uiState.update {
                        it.copy(
                            winnerUsername = username,
                            winningLine = game.winningLine,
                            prizeAmount = game.prizePool,
                            isUserWinner = isUserWinner,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

// Helper extension
private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return com.google.firebase.ktx.await(this)
}
