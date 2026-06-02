package com.bingoapp.domain.usecase.auth

import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.util.Resource
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(verificationId: String, otp: String): Resource<Boolean> {
        if (otp.length < 6) {
            return Resource.Error("OTP must be 6 digits.")
        }
        return authRepository.verifyOtp(verificationId, otp)
    }
}
