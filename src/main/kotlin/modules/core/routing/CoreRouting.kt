package com.modules.core.routing

import io.ktor.server.application.Application

fun Application.coreRouting() {
    organizationRouting()
    employeeRouting()
}