package com.modules.queue.useCases

import com.modules.core.models.entities.Organization
import com.modules.queue.mappers.toDomain
import com.modules.queue.models.entities.Host
import com.modules.queue.models.requests.CreateHostRequest
import com.mongodb.client.model.Filters
import com.shared.Repository
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import com.shared.models.PaginationResponse
import io.ktor.http.HttpStatusCode
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class HostUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>,
    private val hostRepository: Repository<Host>
) {

    // Get organization collection from core database.
    val db = client.getDatabase(Config.DB_NAME)
    val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)

    suspend fun get(orgId: String?, query: String?): ApiResponse<Host> {

        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )
        query ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Query is required")

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get host collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val hostCollection = db.getCollection<Host>(Collections.HOSTS)

        // Get host.
        val hosts = hostRepository.get(collection = hostCollection, filter = Filters.regex("name", query, "i"))
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return host.
        return ApiResponse.success(hosts)
    }

    suspend fun getById(orgId: String?, id: String?): ApiResponse<Host> {

        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )
        id ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Host Id is required")

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get host collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val hostCollection = db.getCollection<Host>(Collections.HOSTS)

        // Get host.
        val host = hostRepository.getById(collection = hostCollection, id = id)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return host.
        return ApiResponse.success(host)
    }

    suspend fun getPaginated(
        orgId: String?,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): ApiResponse<PaginationResponse<Host>> {

        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get host collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val hostCollection = db.getCollection<Host>(Collections.HOSTS)

        // Get hosts.
        val hosts = hostRepository.getPaginated(
            collection = hostCollection,
            filter = filter,
            lastEntryId = lastEntryId,
            limit = limit
        )

        // Return hosts.
        return ApiResponse.success(hosts)
    }

    suspend fun create(request: CreateHostRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            session.startTransaction()
            // Get organization.
            val organization =
                organizationRepository.getById(collection = organizationCollection, id = request.organizationId)
                    ?: return ApiResponse.failure(
                        statusCode = HttpStatusCode.BadRequest,
                        error = "Organization not found"
                    )

            // Get host collection from organization database.
            val db = client.getDatabase(organization.dbName)
            val hostCollection = db.getCollection<Host>(Collections.HOSTS)

            // Create host.
            if (!hostRepository.create(collection = hostCollection,session = session, entity = request.toDomain())) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to create host"
                )
            }

            // Commit transaction and return host.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}