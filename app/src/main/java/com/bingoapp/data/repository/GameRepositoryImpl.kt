package com.bingoapp.data.repository

import com.bingoapp.data.model.BingoCard
import com.bingoapp.data.model.DrawnNumber
import com.bingoapp.data.model.Game
import com.bingoapp.data.model.GameConfig
import com.bingoapp.data.model.PlayerInGame
import com.bingoapp.data.remote.CloudFunctionsService
import com.bingoapp.data.remote.FirestoreService
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService,
    private val cloudFunctions: CloudFunctionsService
) : GameRepository {

    override fun observeCurrentGame(): Flow<Resource<Game>> {
        return firestoreService.observeCurrentGame().map { game ->
            if (game != null) {
                Resource.Success(game)
            } else {
                Resource.Success(Game(status = "idle"))
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe game.", e))
        }
    }

    override fun observePlayerCount(gameId: String): Flow<Resource<Long>> {
        return firestoreService.observePlayerCount(gameId).map { count ->
            Resource.Success(count)
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe player count.", e))
        }
    }

    override fun observeDrawnNumbers(gameId: String): Flow<Resource<List<DrawnNumber>>> {
        return firestoreService.observeDrawnNumbers(gameId).map { numbers ->
            Resource.Success(numbers)
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe drawn numbers.", e))
        }
    }

    override fun observeMyCard(gameId: String): Flow<Resource<PlayerInGame>> {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
        return firestoreService.observeMyCard(gameId, uid).map { player ->
            if (player != null) {
                Resource.Success(player)
            } else {
                Resource.Error("Card not found.")
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe card.", e))
        }
    }

    override fun observeGameConfig(): Flow<Resource<GameConfig>> {
        return firestoreService.observeGameConfig().map { config ->
            if (config != null) {
                Resource.Success(config)
            } else {
                Resource.Success(GameConfig())
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Failed to observe config.", e))
        }
    }

    override suspend fun joinGame(gameId: String): Resource<BingoCard> {
        return try {
            val result = cloudFunctions.joinGame(gameId)
            val cardNumbersRaw = result["cardNumbers"] as? List<List<Any>> ?: emptyList()
            val cardHash = result["cardHash"] as? String ?: ""
            val isFreeCard = result["isFreeCard"] as? Boolean ?: false
            Resource.Success(BingoCard(cardNumbers = cardNumbersRaw, cardHash = cardHash, isFreeCard = isFreeCard))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to join game.", e)
        }
    }

    override suspend fun callBingo(gameId: String): Resource<Boolean> {
        return try {
            val result = cloudFunctions.callBingo(gameId)
            val isWinner = result["isWinner"] as? Boolean ?: false
            Resource.Success(isWinner)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to call bingo.", e)
        }
    }

    override suspend fun leaveGame(gameId: String) {
        try {
            cloudFunctions.leaveGame(gameId)
        } catch (_: Exception) {
            // Silent fail on leave
        }
    }
}
