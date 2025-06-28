package com

import com.shared.plugins.configureRateLimiting
import com.shared.plugins.configureFrameworks
import com.shared.plugins.configureHTTP
import com.shared.plugins.configureMonitoring
import com.shared.plugins.configureRouting
import com.shared.plugins.configureSecurity
import com.shared.plugins.configureSerialization
import com.shared.plugins.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    /** Common configurations */
    configureRateLimiting()
    configureSockets()
    configureFrameworks()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting()

    /** Modules */
}
