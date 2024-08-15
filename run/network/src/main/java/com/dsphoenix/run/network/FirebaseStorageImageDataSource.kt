package com.dsphoenix.run.network

import com.dsphoenix.core.domain.auth.AuthRepository
import com.dsphoenix.core.domain.run.RemoteImageDataSource
import com.dsphoenix.core.domain.run.RunId
import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.Result
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageImageDataSource(
    authRepository: AuthRepository
) : RemoteImageDataSource {

    private val storageReference =
        Firebase.storage.reference.child("/images/${authRepository.getUserId()}")

    override suspend fun uploadImage(
        runId: RunId,
        image: ByteArray
    ): Result<String, DataError.Network> {
        val imageRef = storageReference.child(runId)

        return suspendCoroutine { continuation ->
            imageRef.putBytes(image).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                imageRef.downloadUrl
            }
                .addOnSuccessListener { uri ->
                    Timber.d("Image uploaded")
                    continuation.resume(Result.Success(uri.toString()))
                }
                .addOnFailureListener { exception ->
                    Timber.d(exception.message)

                    when (exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(Result.Error(DataError.Network.NO_INTERNET))
                        }

                        else -> continuation.resume(Result.Error(DataError.Network.UNKNOWN))
                    }
                }
        }
    }
}
