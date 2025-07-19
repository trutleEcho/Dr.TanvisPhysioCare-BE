package com.shared.plugins

import com.modules.core.routing.coreRouting
import com.modules.doctor.routing.doctorRouting
import com.modules.queue.routing.queueRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.webjars.*

fun Application.configureRouting() {
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    install(RequestValidation)

    /** Routes */
    coreRouting()
    doctorRouting()
    queueRouting()
}
