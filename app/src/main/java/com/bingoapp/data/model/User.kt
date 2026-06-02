package com.bingoapp.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("phoneNumber")
    val phoneNumber: String = "",
    @PropertyName("username")
    val username: String = "",
    @PropertyName("coins")
    val coins: Long = 0,
    @PropertyName("hasClaimedWelcomeBonus")
    val hasClaimedWelcomeBonus: Boolean = false,
    @PropertyName("totalGamesPlayed")
    val totalGamesPlayed: Long = 0,
    @PropertyName("totalWins")
    val totalWins: Long = 0,
    @PropertyName("totalCoinsSpent")
    val totalCoinsSpent: Long = 0,
    @PropertyName("totalCoinsWon")
    val totalCoinsWon: Long = 0,
    @PropertyName("accountStatus")
    val accountStatus: String = "active",
    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("lastLoginAt")
    val lastLoginAt: Long = System.currentTimeMillis(),
    @PropertyName("fcmToken")
    val fcmToken: String = ""
) {
    constructor() : this(
        phoneNumber = "",
        username = "",
        coins = 0,
        hasClaimedWelcomeBonus = false,
        totalGamesPlayed = 0,
        totalWins = 0,
        totalCoinsSpent = 0,
        totalCoinsWon = 0,
        accountStatus = "active",
        createdAt = System.currentTimeMillis(),
        lastLoginAt = System.currentTimeMillis(),
        fcmToken = ""
    )
}
