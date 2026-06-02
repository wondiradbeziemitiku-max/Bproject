package com.bingoapp.domain.usecase.game

import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.util.Resource
import javax.inject.Inject

class CallBingoUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: String): Resource<Boolean> {
        return gameRepository.callBingo(gameId)
    }
}
