package com.dsphoenix.run.network

import com.dsphoenix.core.domain.auth.AuthRepository
import com.dsphoenix.core.domain.run.RemoteImageDataSource
import com.dsphoenix.core.domain.run.RemoteRunDataSource
import com.dsphoenix.core.domain.run.Run
import com.dsphoenix.core.domain.util.DataError
import com.dsphoenix.core.domain.util.EmptyResult
import com.dsphoenix.core.domain.util.Result
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreRemoteRunDataSource(
    authRepository: AuthRepository,
    private val imageDataSource: RemoteImageDataSource
) : RemoteRunDataSource {

    private val firestore = Firebase.firestore

    private val currentUserId = authRepository.getUserId()
    private val runsCollection = firestore.collection("runsCollection")
        .document(currentUserId)
        .collection("runs")

    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return suspendCoroutine { continuation ->
            runsCollection
                .get()
                .addOnSuccessListener { result ->
                    Timber.d("Pulled ${result.size()} documents")
                    val runs = result.mapNotNull { doc ->
                        doc.toObject(RunDto::class.java).copy(id = doc.id).toRun()
                    }
                    continuation.resume(Result.Success(runs))
                }
                .addOnFailureListener { exception ->
                    onFailure(continuation, exception)
                }
        }
    }

    override suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network> {
        val runDto = run.toRunDto()
        // Since run saved locally at first - id should never be null at this place
        val runId = run.id
            ?: throw IllegalStateException("ID have to be present")

        val imageResult = imageDataSource.uploadImage(runId, mapPicture)

        if (imageResult is Result.Error)
            return imageResult

        val runDtoWithMapUrl = runDto.copy(mapPictureUrl = (imageResult as Result.Success).data)

        return suspendCoroutine { continuation ->
            runsCollection
                .document(runId)
                .set(runDtoWithMapUrl)
                .addOnSuccessListener {
                    Timber.d("Run added with id: $runId")
                    continuation.resume(Result.Success(run))
                }
                .addOnFailureListener { exception ->
                    onFailure(continuation, exception)
                }
        }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.Network> {
        return suspendCoroutine { continuation ->
            runsCollection
                .document(id)
                .delete()
                .addOnSuccessListener {
                    Timber.d("Successfully deleted run with id: $id")
                }
                .addOnFailureListener { exception ->
                    onFailure(continuation, exception)
                }
        }
    }

    private fun onFailure(
        continuation: Continuation<Result.Error<DataError.Network>>,
        exception: Exception
    ) {
        Timber.d(exception.message)
        when (exception) {
            is FirebaseNetworkException -> {
                continuation.resume(Result.Error(DataError.Network.NO_INTERNET))
            }

            is FirebaseFirestoreException -> {
                when (exception.code) {
                    FirebaseFirestoreException.Code.UNAVAILABLE -> {
                        continuation.resume(Result.Error(DataError.Network.NO_INTERNET))
                    }

                    else -> continuation.resume(Result.Error(DataError.Network.UNKNOWN))
                }
            }

            else -> continuation.resume(Result.Error(DataError.Network.UNKNOWN))
        }
    }
}
