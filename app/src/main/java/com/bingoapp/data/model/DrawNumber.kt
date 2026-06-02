package com.bingoapp.data.model

import com.google.firebase.firestore.PropertyName

data class DrawnNumber(
    @PropertyName("number")
    val number: Long = 0,
    @PropertyName("drawnOrder")
    val drawnOrder: Long = 0,
    @PropertyName("drawnAt")
    val drawnAt: Long = System.currentTimeMillis()
) {
    constructor() : this(0, 0, System.currentTimeMillis())
}
