package com.bingoapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingoapp.ui.theme.Gold

@Composable
fun CountdownTimer(seconds: Int, maxSeconds: Int = 30) {
    val progress by animateFloatAsState(
        targetValue = seconds.toFloat() / maxSeconds,
        animationSpec = tween(1000)
    )
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(80.dp),
            color = Gold,
            strokeWidth = 6.dp
        )
        Text(
            text = "${seconds}s",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Gold
        )
    }
}
