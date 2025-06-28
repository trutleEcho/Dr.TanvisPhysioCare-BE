package com.modules.core.useCases

import com.modules.core.mappers.toDomain
import com.modules.core.models.entities.Employee
import com.modules.core.models.entities.Organization
import com.modules.core.models.requests.CreateEmployeeRequest
import com.shared.Repository
import com.modules.core.repository.employee.EmployeeRepository
import com.modules.core.repository.organization.OrganizationRepository
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import com.shared.models.PaginationResponse
import io.ktor.http.HttpStatusCode
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class EmployeeUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>,
    private val employeeRepository: Repository<Employee>,
) {
    val db = client.getDatabase(Config.DB_NAME)
    val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)

    suspend fun getById(orgId: String?, id: String?): ApiResponse<Employee> {
        if (orgId == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest)
        val organization = organizationRepository.getById(organizationCollection, orgId) ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization not found"
        )
        if (id == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest)
        val db = client.getDatabase(organization.dbName)
        val employeeCollection = db.getCollection<Employee>(Collections.EMPLOYEES)
        val employee = employeeRepository.getById(collection = employeeCollection, id = id)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)
        return ApiResponse.success(employee)
    }

    suspend fun getPaginated(
        orgId: String?,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): ApiResponse<PaginationResponse<Employee>> {
        if (orgId == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest)
        val organization = organizationRepository.getById(organizationCollection, orgId) ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization not found"
        )
        val db = client.getDatabase(organization.dbName)
        val employeeCollection = db.getCollection<Employee>(Collections.EMPLOYEES)
        val employees = employeeRepository.getPaginated(
            collection = employeeCollection,
            filter = filter,
            lastEntryId = lastEntryId,
            limit = limit
        )
        return ApiResponse.success(employees)
    }

    suspend fun create(request: CreateEmployeeRequest): ApiResponse<Boolean> {
        val organization = organizationRepository.getById(organizationCollection, request.organizationId)
            ?: return ApiResponse.failure(
                statusCode = HttpStatusCode.BadRequest,
                error = "Organization not found"
            )
        val db = client.getDatabase(organization.dbName)
        val employeeCollection = db.getCollection<Employee>(Collections.EMPLOYEES)
        client.startSession().use { session ->
            session.startTransaction()
            if (!employeeRepository.create(
                    collection = employeeCollection,
                    session = session,
                    entity = request.toDomain()
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to create employee"
                )
            }
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}