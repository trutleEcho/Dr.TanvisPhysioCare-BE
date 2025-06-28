package com.modules.core.useCases

import com.modules.core.models.entities.Organization
import com.modules.core.models.requests.UpdateOrganizationServicesRequest
import com.shared.Repository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import com.shared.models.PaginationResponse
import io.ktor.http.HttpStatusCode
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class OrganizationUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>
) {
    suspend fun get(filter: Bson): ApiResponse<Organization> {
        val db = client.getDatabase(Config.DB_NAME)
        val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)
        val organizations = organizationRepository.get(collection = organizationCollection, filter = filter)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)
        return ApiResponse.success(organizations)
    }

    suspend fun getById(id: String?): ApiResponse<Organization> {
        if (id == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest)
        val db = client.getDatabase(Config.DB_NAME)
        val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)
        val organization = organizationRepository.getById(collection = organizationCollection, id = id)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)
        return ApiResponse.success(organization)
    }

    suspend fun getPaginated(
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): ApiResponse<PaginationResponse<Organization>> {
        val db = client.getDatabase(Config.DB_NAME)
        val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)
        val organizations =
            organizationRepository.getPaginated(
                collection = organizationCollection,
                filter = filter,
                lastEntryId = lastEntryId,
                limit = limit
            )
        return ApiResponse.success(organizations)
    }

    suspend fun create(entity: Organization): ApiResponse<Boolean> {
        val db = client.getDatabase(Config.DB_NAME)
        val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)
        client.startSession().use { session ->
            session.startTransaction()
            if (!organizationRepository.create(
                    collection = organizationCollection, session = session, entity = entity
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to create organization"
                )
            }
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun updateServices(request: UpdateOrganizationServicesRequest): ApiResponse<Boolean> {
        val db = client.getDatabase(Config.DB_NAME)
        val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)
        client.startSession().use { session ->
            session.startTransaction()
            if (!organizationRepository.update(
                    collection = organizationCollection,
                    session = session,
                    filter = Filters.eq(Organization::_id.name, request.id),
                    update = Updates.set(
                        Organization::services.name, request.services
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to update organization"
                )
            }
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}