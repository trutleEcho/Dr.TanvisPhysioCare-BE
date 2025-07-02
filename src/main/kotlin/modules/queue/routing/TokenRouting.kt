package com.modules.queue.routing

import com.modules.queue.models.requests.CreateTokenRequest
import com.modules.queue.models.requests.DeleteTokenRequest
import com.modules.queue.models.requests.UpdateTokenRequest
import com.modules.queue.useCases.TokenUseCase
import com.mongodb.client.model.Filters
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
import org.bson.conversions.Bson
import org.koin.ktor.ext.inject

fun Application.userRouting() {

    // Dependency Injection
    val tokenUseCase by inject<TokenUseCase>()

    routing {
        get(QueueRoutes.Tokens.GET) {
            runCatching {
                val orgId = call.parameters["orgId"]
                val query = call.parameters["query"]
                val locId = call.parameters["locId"]
                val startDate = call.parameters["startDate"]
                val endDate = call.parameters["endDate"]

                var filters: Bson = Filters.empty()

                when {
                    query != null -> filters = Filters.and(filters, Filters.regex("name", query, "i"))
                    locId != null -> filters = Filters.and(filters, Filters.regex("locationId", locId, "i"))
                    startDate != null -> filters = Filters.and(filters, Filters.gte("createdAt", startDate))
                    endDate != null -> filters = Filters.and(filters, Filters.lte("createdAt", endDate))
                }
                Response.send(call, tokenUseCase.getAll(orgId = orgId, filter = filters), QueueRoutes.Tokens.GET)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Tokens.GET
                )
            }
        }

        post(QueueRoutes.Tokens.CREATE) {
            runCatching {
                val request = call.receive<CreateTokenRequest>()
                Response.send(call, tokenUseCase.create(request), QueueRoutes.Tokens.CREATE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Tokens.CREATE
                )
            }
        }

        patch(QueueRoutes.Tokens.UPDATE) {
            runCatching {
                val request = call.receive<UpdateTokenRequest>()
                Response.send(call, tokenUseCase.update(request), QueueRoutes.Tokens.UPDATE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Tokens.UPDATE
                )
            }
        }

        delete(QueueRoutes.Tokens.DELETE) {
            runCatching {
                val request = call.receive<DeleteTokenRequest>()
                Response.send(call, tokenUseCase.delete(request), QueueRoutes.Tokens.DELETE)
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    QueueRoutes.Tokens.DELETE
                )
            }
        }
    }
}