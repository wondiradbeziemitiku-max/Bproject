package com.bingoapp.domain.usecase.game

import com.bingoapp.data.model.BingoCard
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.domain.repository.UserRepository
import com.bingoapp.util.Resource
import javax.inject.Inject

class JoinGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(gameId: String): Resource<BingoCard> {
        val ageCheck = userRepository.checkAccountAge()
        if (ageCheck is Resource.Error) return ageCheck
        if (ageCheck is Resource.Success && !ageCheck.data) {
            return Resource.Error("Account must be at least 30 days old to play.")
        }
        return gameRepository.joinGame(gameId)
    }
}
