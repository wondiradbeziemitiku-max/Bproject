package com.bingoapp.data.model

import com.google.firebase.firestore.PropertyName

data class Transaction(
    @PropertyName("uid")
    val uid: String = "",
    @PropertyName("type")
    val type: String = "",
    @PropertyName("amount")
    val amount: Long = 0,
    @PropertyName("balanceAfter")
    val balanceAfter: Long = 0,
    @PropertyName("gameId")
    val gameId: String = "",
    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", 0, 0, "", System.currentTimeMillis())
}
