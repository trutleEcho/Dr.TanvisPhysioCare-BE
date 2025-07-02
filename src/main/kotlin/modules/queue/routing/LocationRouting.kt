package com.modules.queue.routing

import com.modules.queue.models.requests.CreateLocationRequest
import com.modules.queue.models.requests.UpdateLocationRequest
import com.modules.queue.models.requests.UpdateLocationStatusRequest
import com.modules.queue.useCases.LocationUseCase
import com.mongodb.client.model.Filters
import com.shared.models.ApiResponse
import com.shared.utils.Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.port
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.bson.conversions.Bson
import org.koin.ktor.ext.inject

fun Application.locationRouting() {

    // Dependencies injection
    val locationUseCase by inject<LocationUseCase>()

    routing {
        get(QueueRoutes.Locations.GET) {
            runCatching {
                val orgId = call.parameters["orgId"]
                val query = call.parameters["query"]

                var filters: Bson = Filters.empty()
                when {
                    query != null -> filters = Filters.and(filters, Filters.regex("name", query, "i"))
                }
                Response.send(
                    call,
                    locationUseCase.get(orgId = orgId, filter = filters),
                    QueueRoutes.Locations.GET
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Locations.GET
                )
            }
        }

        post(QueueRoutes.Locations.CREATE){
            runCatching {
                val request = call.receive<CreateLocationRequest>()
                Response.send(
                    call,
                    locationUseCase.create(request),
                    QueueRoutes.Locations.CREATE
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Locations.CREATE
                )
            }
        }

        patch(QueueRoutes.Locations.UPDATE) {
            runCatching {
                val request = call.receive<UpdateLocationRequest>()
                Response.send(
                    call,
                    locationUseCase.update(request),
                    QueueRoutes.Locations.UPDATE
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Locations.UPDATE
                )
            }
        }

        patch(QueueRoutes.Locations.UPDATE_STATUS) {
            runCatching {
                val request = call.receive<UpdateLocationStatusRequest>()
                Response.send(
                    call,
                    locationUseCase.updateStatus(request),
                    QueueRoutes.Locations.UPDATE_STATUS
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Locations.UPDATE_STATUS
                )
            }
        }
    }
}