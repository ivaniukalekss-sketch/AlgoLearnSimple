package com.ivaniuk.algolearnsimple.domain.model

enum class Speed(val delayMs: Long) {
    SLOW(1000L),
    MEDIUM(500L),
    FAST(200L),
    VERY_FAST(100L)
}