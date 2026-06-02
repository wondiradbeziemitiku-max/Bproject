package com.bingoapp.data.model

data class BingoCard(
    val cardNumbers: List<List<Any>> = emptyList(),
    val cardHash: String = "",
    val isFreeCard: Boolean = false
)

data class PlayerInGame(
    val username: String = "",
    val cardNumbers: List<List<Any>> = emptyList(),
    val cardHash: String = "",
    val isFreeCard: Boolean = false,
    val daubedNumbers: List<Long> = emptyList(),
    val isWinner: Boolean = false,
    val joinedAt: Long = System.currentTimeMillis()
) {
    constructor() : this(
        username = "",
        cardNumbers = emptyList(),
        cardHash = "",
        isFreeCard = false,
        daubedNumbers = emptyList(),
        isWinner = false,
        joinedAt = System.currentTimeMillis()
    )
}
