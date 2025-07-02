package com.modules.queue.routing

import io.ktor.server.application.Application

fun Application.queueRouting() {
    locationRouting()
    hostRouting()
    userRouting()
}