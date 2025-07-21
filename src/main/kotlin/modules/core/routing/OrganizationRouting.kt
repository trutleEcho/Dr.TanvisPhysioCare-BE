package com.modules.core.routing

import com.modules.core.mappers.toDomain
import com.modules.doctor.mappers.toDomain
import com.modules.core.models.entities.Organization
import com.modules.core.models.requests.CreateOrganizationRequest
import com.modules.core.models.requests.UpdateOrganizationServicesRequest
import com.modules.core.useCases.OrganizationUseCase
import com.mongodb.client.model.Filters
import com.shared.models.ApiResponse
import com.shared.utils.Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Application.organizationRouting() {
    val organizationUseCase by inject<OrganizationUseCase>()

    routing {
        get(CoreRoutes.Organizations.GET) {
            runCatching {
                val orgId = call.parameters["orgId"]
                Response.send(
                    call,
                    organizationUseCase.get(orgId),
                    CoreRoutes.Organizations.GET
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Organizations.GET
                )
            }
        }

        get(CoreRoutes.Organizations.GET_ALL) {
            runCatching {
                Response.send(
                    call,
                    organizationUseCase.getAll(),
                    CoreRoutes.Organizations.GET_ALL
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Organizations.GET_ALL
                )
            }
        }

        get(CoreRoutes.Organizations.GET_PAGINATED) {
            runCatching {
                val lastEntryId = call.parameters["lastEntryId"]
                val limit = call.parameters["limit"]
                Response.send(
                    call,
                    organizationUseCase.getPaginated(
                        filter = Filters.empty(),
                        lastEntryId = lastEntryId,
                        limit = limit?.toInt() ?: 10
                    ),
                    CoreRoutes.Organizations.GET_PAGINATED
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    )
                )
            }
        }

        post(CoreRoutes.Organizations.CREATE) {
            runCatching {
                val request = call.receive<CreateOrganizationRequest>()
                Response.send(
                    call,
                    organizationUseCase.create(request.toDomain()),
                    CoreRoutes.Organizations.CREATE
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Organizations.CREATE
                )
            }
        }

        patch(CoreRoutes.Organizations.UPDATE_SERVICES) {
            runCatching {
                val request = call.receive<UpdateOrganizationServicesRequest>()
                Response.send(
                    call,
                    organizationUseCase.updateServices(request),
                    CoreRoutes.Organizations.UPDATE_SERVICES
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Organizations.UPDATE_SERVICES
                )
            }
        }
    }
}