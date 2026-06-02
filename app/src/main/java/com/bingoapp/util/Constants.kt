package com.bingoapp.util

object Constants {
    const val CARD_PRICE_COINS = 10
    const val DRAW_INTERVAL_SECONDS = 6L
    const val COUNTDOWN_SECONDS = 30L
    const val MIN_PLAYERS = 10
    const val MAX_PLAYERS = 400
    const val PRIZE_POOL_PERCENTAGE = 80
    const val BINGO_COOLDOWN_SECONDS = 5L
    const val DISCONNECT_TIMEOUT_SECONDS = 120L

    const val CARD_ROWS = 5
    const val CARD_COLS = 5
    const val FREE_ROW = 2
    const val FREE_COL = 2
    const val TOTAL_NUMBERS = 75

    const val COLLECTION_USERS = "users"
    const val COLLECTION_GAMES = "games"
    const val COLLECTION_TRANSACTIONS = "transactions"
    const val COLLECTION_CONFIG = "config"
    const val SUBCOLLECTION_PLAYERS = "players"
    const val SUBCOLLECTION_DRAWN_NUMBERS = "drawnNumbers"
    const val DOC_GLOBAL_CONFIG = "global"

    const val COUNTRY_CODE_DEFAULT = "+251"
    const val MIN_USERNAME_LENGTH = 3
    const val MAX_USERNAME_LENGTH = 20
    const val MIN_ACCOUNT_AGE_DAYS = 30

    const val FCM_CHANNEL_ID = "bingo_notifications"
    const val FCM_CHANNEL_NAME = "Bingo Game Notifications"
}
