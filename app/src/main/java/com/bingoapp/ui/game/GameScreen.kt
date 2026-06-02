package com.bingoapp.ui.game

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingoapp.ui.components.BingoCardComposable
import com.bingoapp.ui.components.CalledNumbersBar
import com.bingoapp.ui.theme.*

@Composable
fun GameScreen(
    gameId: String,
    onGameEnd: (String) -> Unit,
    onBackToLobby: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(gameId) {
        viewModel.init(gameId)
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.gameEnded) {
        if (state.gameEnded) {
            onGameEnd(gameId)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Latest called number
            if (state.latestNumber != null) {
                Text(
                    text = "Last Called",
                    style = MaterialTheme.typography.labelLarge,
                    color = OnSurfaceVariantDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Gold, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${state.latestNumber}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Called numbers history
            CalledNumbersBar(numbers = state.drawnNumbers.map { it.toInt() })

            Spacer(modifier = Modifier.height(12.dp))

            // Bingo Card
            if (state.cardNumbers.isNotEmpty()) {
                BingoCardComposable(
                    cardNumbers = state.cardNumbers,
                    daubedNumbers = state.daubedNumbers.map { it.toInt() },
                    onCellTap = { number -> viewModel.manualDaub(number) }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BINGO Button
            Button(
                onClick = { viewModel.callBingo() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .animateContentSize(),
                enabled = state.canCallBingo && !state.callingBingo,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BingoRed,
                    disabledContainerColor = Gray600
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = SolidColor(Gold)
                )
            ) {
                if (state.callingBingo) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(28.dp))
                } else {
                    Text(
                        text = "BINGO!",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }

            if (state.bingoCooldown > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Wait ${state.bingoCooldown}s before calling again",
                    color = BingoRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
