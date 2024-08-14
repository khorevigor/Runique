package com.dsphoenix.auth.data

import com.dsphoenix.core.domain.auth.AuthError
import com.dsphoenix.core.domain.auth.AuthRepository
import com.dsphoenix.core.domain.util.EmptyResult
import com.dsphoenix.core.domain.util.Result
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository : AuthRepository {

    private val auth = Firebase.auth

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in")
    }

    override suspend fun login(email: String, password: String): EmptyResult<AuthError> {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(Unit))
                    }
                    else {
                        Timber.e(task.exception?.message)

                        val result = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.INVALID_CREDENTIALS)
                            else -> Result.Error(AuthError.UNKNOWN)
                        }

                        continuation.resume(result)
                    }
                }
        }
    }

    override suspend fun register(email: String, password: String): EmptyResult<AuthError> {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(Unit))
                    } else {
                        Timber.e(task.exception?.message)

                        val result = when (task.exception) {
                            is FirebaseAuthUserCollisionException -> Result.Error(AuthError.USER_ALREADY_EXISTS)
                            else -> Result.Error(AuthError.UNKNOWN)
                        }

                        continuation.resume(result)
                    }
                }
        }
    }

    override fun logout() {
        auth.signOut()
    }
}
