package com.bingoapp.data.repository

import com.bingoapp.data.local.PreferencesDataStore
import com.bingoapp.data.model.Transaction
import com.bingoapp.data.model.User
import com.bingoapp.data.remote.CloudFunctionsService
import com.bingoapp.data.remote.FirestoreService
import com.bingoapp.domain.repository.UserRepository
import com.bingoapp.util.Constants
import com.bingoapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService,
    private val cloudFunctions: CloudFunctionsService,
    private val preferencesDataStore: PreferencesDataStore,
    private val auth: FirebaseAuth
) : UserRepository {

    override fun observeCurrentUser(): Flow<Resource<User>> {
        val uid = auth.currentUser?.uid ?: return kotlinx.coroutines.flow.flow {
            emit(Resource.Error("Not logged in."))
        }
        return firestoreService.observeUser(uid).map { user ->
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User data not found.")
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe user.", e))
        }
    }

    override suspend fun getUserProfile(): Resource<User> {
        return try {
            val uid = auth.currentUser?.uid ?: return Resource.Error("Not logged in.")
            val user = firestoreService.getUser(uid)
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User not found.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to get profile.", e)
        }
    }

    override suspend fun updateFcmToken(token: String) {
        try {
            val uid = auth.currentUser?.uid ?: return
            firestoreService.updateFcmToken(uid, token)
        } catch (_: Exception) {
            // Silent fail
        }
    }

    override suspend fun getTransactionHistory(limit: Long): Flow<Resource<List<Transaction>>> {
        val uid = auth.currentUser?.uid ?: return kotlinx.coroutines.flow.flow {
            emit(Resource.Error("Not logged in."))
        }
        return firestoreService.observeTransactions(uid, limit).map { transactions ->
            Resource.Success(transactions)
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to load transactions.", e))
        }
    }

    override suspend fun purchaseCoins(productId: String, purchaseToken: String): Resource<Long> {
        return try {
            val result = cloudFunctions.purchaseCoins(purchaseToken, productId)
            val newBalance = (result["newBalance"] as? Number)?.toLong() ?: 0L
            Resource.Success(newBalance)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to purchase coins.", e)
        }
    }

    override suspend fun checkAccountAge(): Resource<Boolean> {
        return try {
            val uid = auth.currentUser?.uid ?: return Resource.Error("Not logged in.")
            val user = firestoreService.getUser(uid) ?: return Resource.Error("User not found.")
            val accountAgeMs = System.currentTimeMillis() - user.createdAt
            val thirtyDaysMs = Constants.MIN_ACCOUNT_AGE_DAYS * 24L * 60L * 60L * 1000L
            Resource.Success(accountAgeMs >= thirtyDaysMs)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to check account age.", e)
        }
    }
}
