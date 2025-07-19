package com.modules.queue.utils

import kotlin.random.Random

/**
 * Generates a 6-digit zero-padded random key as a String.
 */
fun keyGenerator(): String {
    return Random.nextInt(0, 1_000_000).toString().padStart(6, '0')
}
