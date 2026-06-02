package com.bingoapp.domain.usecase.auth

import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.util.Resource
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String): Resource<String> {
        val fullNumber = if (phoneNumber.startsWith("+")) phoneNumber else "+$phoneNumber"
        return authRepository.sendOtp(fullNumber)
    }
}
