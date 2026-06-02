package com.bingoapp.domain.model

data class UserDomain(
    val uid: String = "",
    val phoneNumber: String = "",
    val username: String = "",
    val coins: Long = 0,
    val hasClaimedWelcomeBonus: Boolean = false,
    val totalGamesPlayed: Long = 0,
    val totalWins: Long = 0,
    val totalCoinsSpent: Long = 0,
    val totalCoinsWon: Long = 0,
    val accountStatus: String = "active",
    val createdAt: Long = 0,
    val lastLoginAt: Long = 0,
    val winRate: Float = 0f
)
