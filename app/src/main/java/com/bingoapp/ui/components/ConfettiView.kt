package com.bingoapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ConfettiView(particleCount: Int = 50) {
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }

    LaunchedEffect(Unit) {
        particles = (0..particleCount).map {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 2f + 1f,
                size = Random.nextFloat() * 8f + 4f,
                color = Color(Random.nextInt())
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // In a real app, use Canvas composable to draw particles with animation
        // For brevity, placeholder
    }
}

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float,
    val color: Color
)
