package com.modules.core.routing

import com.modules.core.models.requests.CreateEmployeeRequest
import com.modules.core.useCases.EmployeeUseCase
import com.mongodb.client.model.Filters
import com.shared.models.ApiResponse
import com.shared.utils.Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.employeeRouting() {

    val employeeUseCase by inject<EmployeeUseCase>()
    routing {

        get(CoreRoutes.Employees.GET) {
            runCatching {
                val orgId = call.parameters["orgId"]
                val id = call.parameters["id"]
                Response.send(
                    call,
                    employeeUseCase.getById(orgId = orgId, id = id),
                    CoreRoutes.Employees.GET
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Employees.GET
                )
            }
        }

        get(CoreRoutes.Employees.GET_PAGINATED) {
            runCatching {
                val orgId = call.parameters["orgId"]
                val lastEntryId = call.parameters["lastEntryId"]
                val limit = call.parameters["limit"]
                Response.send(
                    call,
                    employeeUseCase.getPaginated(
                        orgId = orgId,
                        filter = Filters.empty(),
                        lastEntryId = lastEntryId,
                        limit = limit?.toInt() ?: 10
                    ),
                    CoreRoutes.Employees.GET_PAGINATED
                )
            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Employees.GET_PAGINATED
                )
            }
        }

        post(CoreRoutes.Employees.CREATE) {
            runCatching {
                val request = call.receive<CreateEmployeeRequest>()
                Response.send(
                    call,
                    employeeUseCase.create(request),
                    CoreRoutes.Employees.CREATE
                )

            }.onFailure {
                it.printStackTrace()
                Response.send(
                    call,
                    ApiResponse.failure<String>(
                        statusCode = HttpStatusCode.InternalServerError,
                        error = it.message.toString()
                    ),
                    CoreRoutes.Employees.CREATE
                )
            }
        }
    }
}