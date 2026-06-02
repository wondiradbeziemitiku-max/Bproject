package com.bingoapp.data.remote

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudFunctionsService @Inject constructor(
    private val functions: FirebaseFunctions
) {
    suspend fun joinGame(gameId: String): Map<String, Any> {
        return functions
            .getHttpsCallable("joinGame")
            .call(mapOf("gameId" to gameId))
            .await()
            .data as Map<String, Any>
    }

    suspend fun callBingo(gameId: String): Map<String, Any> {
        return functions
            .getHttpsCallable("callBingo")
            .call(mapOf("gameId" to gameId))
            .await()
            .data as Map<String, Any>
    }

    suspend fun leaveGame(gameId: String): Map<String, Any> {
        return functions
            .getHttpsCallable("leaveGame")
            .call(mapOf("gameId" to gameId))
            .await()
            .data as Map<String, Any>
    }

    suspend fun purchaseCoins(purchaseToken: String, productId: String): Map<String, Any> {
        return functions
            .getHttpsCallable("purchaseCoins")
            .call(mapOf("purchaseToken" to purchaseToken, "productId" to productId))
            .await()
            .data as Map<String, Any>
    }

    suspend fun adminStartGame(): Map<String, Any> {
        return functions
            .getHttpsCallable("adminStartGame")
            .call()
            .await()
            .data as Map<String, Any>
    }

    suspend fun adminCancelGame(gameId: String): Map<String, Any> {
        return functions
            .getHttpsCallable("adminCancelGame")
            .call(mapOf("gameId" to gameId))
            .await()
            .data as Map<String, Any>
    }
}
