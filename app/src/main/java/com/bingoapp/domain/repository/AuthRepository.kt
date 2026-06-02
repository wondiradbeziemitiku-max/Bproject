package com.bingoapp.domain.repository

import com.bingoapp.util.Resource

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Resource<String>
    suspend fun verifyOtp(verificationId: String, otp: String): Resource<Boolean>
    suspend fun checkUsernameAvailable(username: String): Resource<Boolean>
    suspend fun setUsername(username: String): Resource<Boolean>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun logout()
    fun getCurrentUserId(): String?
    suspend fun getCurrentUserPhone(): String?
}
