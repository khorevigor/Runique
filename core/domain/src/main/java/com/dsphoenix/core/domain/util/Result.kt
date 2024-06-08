package com.dsphoenix.core.domain.util

sealed interface Result<out D, out E: Error> {

    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: com.dsphoenix.core.domain.util.Error>(val error: E): Result<Nothing, E>
}

// Maps Result underlying data type to new type
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result <R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyDataResult<E> {
    return map { /*Unit*/ }
}

typealias EmptyDataResult<E> = Result<Unit, E>

