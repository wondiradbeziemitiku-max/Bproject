package com.bingoapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingoapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Game Controls", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Gold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.startGame() }, colors = ButtonDefaults.buttonColors(containerColor = BingoGreen)) {
                    Text("Start Game")
                }
                Button(onClick = { viewModel.cancelGame() }, colors = ButtonDefaults.buttonColors(containerColor = BingoRed)) {
                    Text("Cancel Game")
                }
            }

            var cardPrice by remember { mutableStateOf(state.config.cardPriceCoins.toString()) }
            var drawInterval by remember { mutableStateOf(state.config.drawIntervalSeconds.toString()) }
            var minPlayers by remember { mutableStateOf(state.config.minPlayers.toString()) }

            OutlinedTextField(value = cardPrice, onValueChange = { cardPrice = it }, label = { Text("Card Price (coins)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = drawInterval, onValueChange = { drawInterval = it }, label = { Text("Draw Interval (s)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = minPlayers, onValueChange = { minPlayers = it }, label = { Text("Min Players") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = {
                viewModel.updateConfig(
                    cardPrice.toLongOrNull() ?: 10,
                    drawInterval.toLongOrNull() ?: 6,
                    minPlayers.toLongOrNull() ?: 10
                )
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Gold)) {
                Text("Update Config", color = DeepBlue, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Gold)
            Text("Active players: ${state.activePlayers}", color = White)
            Text("Today's revenue: ${state.todayRevenue} 🪙", color = White)
        }
    }
}
