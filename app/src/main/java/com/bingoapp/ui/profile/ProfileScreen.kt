package com.bingoapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingoapp.ui.theme.*
import com.bingoapp.util.toCoinString
import com.bingoapp.util.maskPhoneNumber
import com.bingoapp.util.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBlue)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Gold, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.username.firstOrNull()?.uppercase() ?: "?",
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = state.username,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Text(
                    text = state.phoneNumber.maskPhoneNumber(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariantDark
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Coin balance
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariantDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Balance", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariantDark)
                            Text(
                                text = state.coinBalance.toCoinString(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Gold
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { /* trigger buy coins */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Gold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Buy Coins", color = DeepBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Stats
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariantDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Statistics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = White)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("Games", "${state.totalGamesPlayed}")
                            StatItem("Wins", "${state.totalWins}")
                            StatItem("Win Rate", "${state.winRate}%")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Total Coins Won: ${state.totalCoinsWon.toCoinString()} 🪙", color = BingoGreen, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Transaction History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = White, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(state.transactions) { transaction ->
                TransactionItem(transaction)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BingoRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Gold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariantDark)
    }
}

@Composable
private fun TransactionItem(transaction: com.bingoapp.data.model.Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariantDark.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (transaction.type) {
                    "purchase" -> "🎟️"
                    "prize" -> "🏆"
                    "free_bonus" -> "🎁"
                    "coin_pack" -> "💰"
                    else -> "💳"
                },
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.type.replace("_", " ").replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
                Text(
                    text = transaction.createdAt.toDateString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariantDark
                )
            }
            Text(
                text = if (transaction.type == "prize" || transaction.type == "free_bonus" || transaction.type == "coin_pack") 
                    "+${transaction.amount} 🪙" 
                else "-${transaction.amount} 🪙",
                style = MaterialTheme.typography.bodyMedium,
                color = if (transaction.amount >= 0) BingoGreen else BingoRed,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
