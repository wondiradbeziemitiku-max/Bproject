package com.bingoapp.domain.usecase.game

import com.bingoapp.data.model.Game
import com.bingoapp.domain.repository.GameRepository
import com.bingoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGameUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<Resource<Game>> {
        return gameRepository.observeCurrentGame()
    }
}
