package com.ivaniuk.algolearnsimple

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


private fun <T, R> Flow<T>.map(transform: (T) -> R): Flow<R> = flow {
    collect { value ->
        emit(transform(value))
    }
}