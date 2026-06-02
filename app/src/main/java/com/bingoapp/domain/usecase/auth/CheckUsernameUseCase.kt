package com.bingoapp.domain.usecase.auth

import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.util.Constants
import com.bingoapp.util.Resource
import javax.inject.Inject

class CheckUsernameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): Resource<Boolean> {
        if (username.length < Constants.MIN_USERNAME_LENGTH) {
            return Resource.Error("Username must be at least ${Constants.MIN_USERNAME_LENGTH} characters.")
        }
        if (username.length > Constants.MAX_USERNAME_LENGTH) {
            return Resource.Error("Username must be at most ${Constants.MAX_USERNAME_LENGTH} characters.")
        }
        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return Resource.Error("Username can only contain letters, numbers, and underscores.")
        }
        return authRepository.checkUsernameAvailable(username)
    }
}
