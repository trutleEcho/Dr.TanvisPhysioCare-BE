package com.modules.queue.routing

import com.modules.queue.models.requests.CreateHostRequest
import com.modules.queue.useCases.HostUseCase
import com.shared.models.ApiResponse
import com.shared.utils.Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.hostRouting() {

    // Dependency injection
    val hostUseCase by inject<HostUseCase>()

    routing {
        get(QueueRoutes.Hosts.GET) {
            runCatching {
                val orgId = call.parameters["orgId"]
                val query = call.parameters["query"]
                Response.send(call, hostUseCase.get(orgId = orgId, query = query), QueueRoutes.Hosts.GET)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Hosts.GET
                )
            }
        }

        post(QueueRoutes.Hosts.CREATE) {
            runCatching {
                val request = call.receive<CreateHostRequest>()
                Response.send(call, hostUseCase.create(request), QueueRoutes.Hosts.CREATE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Hosts.CREATE
                )
            }
        }
    }
}