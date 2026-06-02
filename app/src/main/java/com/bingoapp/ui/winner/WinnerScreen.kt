package com.bingoapp.ui.winner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingoapp.ui.theme.*

@Composable
fun WinnerScreen(
    gameId: String,
    onPlayAgain: () -> Unit,
    viewModel: WinnerViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(gameId) {
        viewModel.loadWinnerInfo(gameId)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉",
                    fontSize = 60.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (state.isUserWinner) "You Won!" else "We Have a Winner!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.winnerUsername,
                    style = MaterialTheme.typography.titleLarge,
                    color = White
                )
                Text(
                    text = "Winning Line: ${state.winningLine}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariantDark
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Prize: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = OnSurfaceVariantDark
                    )
                    Text(
                        text = "${state.prizeAmount} 🪙",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = BingoGreen
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Play Again",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                }
            }
        }
    }
}
