package com.bingoapp.data.model

import com.google.firebase.firestore.PropertyName

data class GameConfig(
    @PropertyName("cardPriceCoins")
    val cardPriceCoins: Long = 10,
    @PropertyName("drawIntervalSeconds")
    val drawIntervalSeconds: Long = 6,
    @PropertyName("minPlayers")
    val minPlayers: Long = 10,
    @PropertyName("maxPlayers")
    val maxPlayers: Long = 400,
    @PropertyName("countdownSeconds")
    val countdownSeconds: Long = 30,
    @PropertyName("prizePoolPercentage")
    val prizePoolPercentage: Long = 80,
    @PropertyName("welcomeBonusEnabled")
    val welcomeBonusEnabled: Boolean = true,
    @PropertyName("maxFreeCards")
    val maxFreeCards: Long = 1,
    @PropertyName("maintenanceMode")
    val maintenanceMode: Boolean = false
) {
    constructor() : this(10, 6, 10, 400, 30, 80, true, 1, false)
}
