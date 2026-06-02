package com.bingoapp.domain.repository

import com.bingoapp.data.model.Transaction
import com.bingoapp.data.model.User
import com.bingoapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeCurrentUser(): Flow<Resource<User>>
    suspend fun getUserProfile(): Resource<User>
    suspend fun updateFcmToken(token: String)
    suspend fun getTransactionHistory(limit: Long = 20): Flow<Resource<List<Transaction>>>
    suspend fun purchaseCoins(productId: String, purchaseToken: String): Resource<Long>
    suspend fun checkAccountAge(): Resource<Boolean>
}
