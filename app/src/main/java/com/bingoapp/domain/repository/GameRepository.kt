package com.bingoapp.domain.repository

import com.bingoapp.data.model.BingoCard
import com.bingoapp.data.model.DrawnNumber
import com.bingoapp.data.model.Game
import com.bingoapp.data.model.GameConfig
import com.bingoapp.data.model.PlayerInGame
import com.bingoapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeCurrentGame(): Flow<Resource<Game>>
    fun observePlayerCount(gameId: String): Flow<Resource<Long>>
    fun observeDrawnNumbers(gameId: String): Flow<Resource<List<DrawnNumber>>>
    fun observeMyCard(gameId: String): Flow<Resource<PlayerInGame>>
    fun observeGameConfig(): Flow<Resource<GameConfig>>
    suspend fun joinGame(gameId: String): Resource<BingoCard>
    suspend fun callBingo(gameId: String): Resource<Boolean>
    suspend fun leaveGame(gameId: String)
}
