package com.bingoapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bingoapp.ui.theme.Gold
import com.bingoapp.ui.theme.White

@Composable
fun CoinBalanceBar(coins: Long) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("🪙", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$coins",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Gold
        )
    }
}
