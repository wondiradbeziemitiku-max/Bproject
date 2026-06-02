package com.bingoapp.data.model

import com.google.firebase.firestore.PropertyName

data class Game(
    @PropertyName("status")
    val status: String = "waiting",
    @PropertyName("createdBy")
    val createdBy: String = "",
    @PropertyName("playerCount")
    val playerCount: Long = 0,
    @PropertyName("countdownSeconds")
    val countdownSeconds: Long = 30,
    @PropertyName("drawIntervalSeconds")
    val drawIntervalSeconds: Long = 6,
    @PropertyName("cardPriceCoins")
    val cardPriceCoins: Long = 10,
    @PropertyName("prizePoolPercentage")
    val prizePoolPercentage: Long = 80,
    @PropertyName("numberPoolHash")
    val numberPoolHash: String = "",
    @PropertyName("startedAt")
    val startedAt: Long = 0,
    @PropertyName("finishedAt")
    val finishedAt: Long = 0,
    @PropertyName("winnerUid")
    val winnerUid: String = "",
    @PropertyName("winningLine")
    val winningLine: String = "",
    @PropertyName("totalCoinsCollected")
    val totalCoinsCollected: Long = 0,
    @PropertyName("prizePool")
    val prizePool: Long = 0,
    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this(
        status = "waiting",
        createdBy = "",
        playerCount = 0,
        countdownSeconds = 30,
        drawIntervalSeconds = 6,
        cardPriceCoins = 10,
        prizePoolPercentage = 80,
        numberPoolHash = "",
        startedAt = 0,
        finishedAt = 0,
        winnerUid = "",
        winningLine = "",
        totalCoinsCollected = 0,
        prizePool = 0,
        createdAt = System.currentTimeMillis()
    )
}
