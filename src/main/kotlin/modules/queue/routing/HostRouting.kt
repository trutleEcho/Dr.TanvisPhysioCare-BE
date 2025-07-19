package com.modules.queue.routing

import com.modules.queue.models.requests.host.CreateHostRequest
import com.modules.queue.models.requests.host.DeleteHostRequest
import com.modules.queue.models.requests.host.UpdateHostRequest
import com.modules.queue.models.requests.host.UpdateHostStatusRequest
import com.modules.queue.useCases.HostUseCase
import com.shared.models.ApiResponse
import com.shared.utils.Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
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
                val hosId = call.parameters["hostId"]
                Response.send(call, hostUseCase.get(orgId = orgId, hostId = hosId), QueueRoutes.Hosts.GET)
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

        patch(QueueRoutes.Hosts.UPDATE) {
            runCatching {
                val request = call.receive<UpdateHostRequest>()
                Response.send(call, hostUseCase.update(request), QueueRoutes.Hosts.UPDATE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Hosts.UPDATE
                )
            }
        }

        patch(QueueRoutes.Hosts.UPDATE_STATUS) {
            runCatching {
                val request = call.receive<UpdateHostStatusRequest>()
                Response.send(call, hostUseCase.updateHostStatus(request), QueueRoutes.Hosts.UPDATE_STATUS)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Hosts.UPDATE_STATUS
                )
            }
        }

        delete(QueueRoutes.Hosts.DELETE) {
            runCatching {
                val request = call.receive<DeleteHostRequest>()
                Response.send(call, hostUseCase.delete(request), QueueRoutes.Hosts.DELETE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Hosts.DELETE
                )
            }
        }
    }
}