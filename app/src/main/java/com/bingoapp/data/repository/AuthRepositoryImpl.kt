package com.bingoapp.data.repository

import com.bingoapp.data.remote.FirebaseAuthService
import com.bingoapp.data.remote.FirestoreService
import com.bingoapp.domain.repository.AuthRepository
import com.bingoapp.util.Resource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authService: FirebaseAuthService,
    private val firestoreService: FirestoreService
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Resource<String> {
        return suspendCancellableCoroutine { continuation ->
            authService.sendOtp(
                phoneNumber = phoneNumber,
                onCodeSent = { verificationId ->
                    if (continuation.isActive) {
                        continuation.resume(Resource.Success(verificationId))
                    }
                },
                onError = { exception ->
                    if (continuation.isActive) {
                        val message = when (exception) {
                            is com.google.firebase.auth.FirebaseAuthInvalidPhoneNumberException ->
                                "Invalid phone number format."
                            is com.google.firebase.auth.FirebaseTooManyRequestsException ->
                                "Too many requests. Please try again later."
                            else -> exception.message ?: "Failed to send OTP."
                        }
                        continuation.resume(Resource.Error(message, exception))
                    }
                }
            )
        }
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Resource<Boolean> {
        val result = authService.verifyOtp(otp)
        return result.fold(
            onSuccess = { success ->
                if (success) {
                    val uid = authService.getCurrentUserId()
                    val phone = authService.getCurrentUserPhone()
                    if (uid != null && phone != null) {
                        val existingUser = firestoreService.getUser(uid)
                        if (existingUser == null) {
                            firestoreService.createUser(uid, phone)
                        }
                    }
                    Resource.Success(true)
                } else {
                    Resource.Error("OTP verification failed.")
                }
            },
            onFailure = { exception ->
                val message = when (exception) {
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                        "Invalid OTP code. Please try again."
                    else -> exception.message ?: "OTP verification failed."
                }
                Resource.Error(message, exception)
            }
        )
    }

    override suspend fun checkUsernameAvailable(username: String): Resource<Boolean> {
        return try {
            val exists = firestoreService.checkUsernameExists(username)
            Resource.Success(!exists)
        } catch (e: Exception) {
            Resource.Error("Failed to check username availability.", e)
        }
    }

    override suspend fun setUsername(username: String): Resource<Boolean> {
        return try {
            val uid = authService.getCurrentUserId() ?: return Resource.Error("Not logged in.")
            firestoreService.updateUsername(uid, username)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Failed to set username.", e)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return authService.isLoggedIn()
    }

    override suspend fun logout() {
        authService.logout()
    }

    override fun getCurrentUserId(): String? {
        return authService.getCurrentUserId()
    }

    override suspend fun getCurrentUserPhone(): String? {
        return authService.getCurrentUserPhone()
    }
}
