package com.bingoapp.domain.model

data class GameDomain(
    val gameId: String = "",
    val status: String = "waiting",
    val playerCount: Long = 0,
    val countdownSeconds: Long = 30,
    val cardPriceCoins: Long = 10,
    val prizePool: Long = 0,
    val winnerUid: String = "",
    val winnerUsername: String = "",
    val winningLine: String = "",
    val numberPoolHash: String = "",
    val drawnNumbers: List<Long> = emptyList(),
    val startedAt: Long = 0,
    val finishedAt: Long = 0
)
