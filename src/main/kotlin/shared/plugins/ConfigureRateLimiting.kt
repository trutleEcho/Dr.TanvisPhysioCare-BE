package com.shared.plugins

import io.github.flaxoos.ktor.server.plugins.ratelimiter.*
import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.*
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimiting() {
    install(RateLimiting) {
        rateLimiter {
            type = TokenBucket::class
            capacity = 100
            rate = 10.seconds
        }
    }
}
