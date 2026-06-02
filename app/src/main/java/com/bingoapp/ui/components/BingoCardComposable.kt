package com.bingoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingoapp.ui.theme.*

@Composable
fun BingoCardComposable(
    cardNumbers: List<List<Any>>,
    daubedNumbers: List<Int>,
    onCellTap: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header row: B I N G O
        Row {
            listOf("B", "I", "N", "G", "O").forEach { header ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(2.dp)
                        .background(MediumBlue, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = header,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Gold
                    )
                }
            }
        }
        // Number rows
        for (row in 0 until 5) {
            Row {
                for (col in 0 until 5) {
                    val cellValue = cardNumbers.getOrNull(row)?.getOrNull(col)
                    val isFree = (row == 2 && col == 2)
                    val number = if (cellValue is Number) cellValue.toInt() else if (cellValue == "FREE") -1 else -1
                    val isDaubed = number != -1 && daubedNumbers.contains(number)
                    val isFreeCell = isFree || number == -1
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when {
                                    isDaubed -> BingoGreen
                                    isFreeCell -> Gold.copy(alpha = 0.3f)
                                    else -> SurfaceDark
                                }
                            )
                            .border(1.dp, Gray800, RoundedCornerShape(4.dp))
                            .clickable {
                                if (number > 0) onCellTap(number)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isFree) "⭐" else number.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isFree) 20.sp else 14.sp,
                            color = when {
                                isDaubed -> White
                                isFreeCell -> DeepBlue
                                else -> OnSurfaceDark
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
