package com.bingoapp.ui.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bingoapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How to Play", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
        ) {
            Text("🎱 75-Ball Bingo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Gold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Each card has 5 columns: B (1-15), I (16-30), N (31-45), G (46-60), O (61-75). The center cell is FREE.", color = White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("🏆 Winning", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Gold)
            Text("The first player to complete one full horizontal, vertical, or diagonal line wins.", color = White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("🪙 Coins", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Gold)
            Text("Each card costs 10 Coins. Winners receive 80% of total coins collected in that game. New players get their first game free!", color = White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("🔒 Provably Fair", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Gold)
            Text("The number draw order is pre-shuffled and hashed with SHA-256 before the game starts. You can verify the fairness after each game by comparing the published hash.", color = White)
        }
    }
}
