package com.shared.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {

        })
    }
}
