package com.bingoapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidPhoneNumberException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthService @Inject constructor(
    private val auth: FirebaseAuth
) {
    private var storedVerificationId: String = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendOtp(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    storedVerificationId = credential.smsCode ?: ""
                }

                override fun onVerificationFailed(e: Exception) {
                    onError(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    onCodeSent(verificationId)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun verifyOtp(otp: String): Result<Boolean> {
        return try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getCurrentUserPhone(): String? = auth.currentUser?.phoneNumber

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun logout() {
        auth.signOut()
    }
}

// Extension function for Firebase async operations
private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return com.google.firebase.ktx.await(this)
}
