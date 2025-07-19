package com.modules.queue.useCases

import com.modules.core.models.entities.Organization
import com.modules.queue.mappers.toDomain
import com.modules.queue.models.entities.Location
import com.modules.queue.models.requests.location.CreateLocationRequest
import com.modules.queue.models.requests.location.UpdateLocationRequest
import com.modules.queue.models.requests.location.UpdateLocationStatusRequest
import com.mongodb.client.model.Updates
import com.shared.Repository
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import io.ktor.http.HttpStatusCode
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class LocationUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>,
    private val locationRepository: Repository<Location>,
) {

    // Get organization collection from core database.
    val db = client.getDatabase(Config.DB_NAME)
    val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)

    suspend fun get(orgId: String?, filter: Bson): ApiResponse<List<Location>> {
        // Validation of parameters
        orgId ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "OrgId is required")

        // Get organization
        val organization = organizationRepository.getById(organizationCollection, orgId) ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization not found"
        )

        // Get locations collection within organization database
        val db = client.getDatabase(organization.dbName)
        val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

        // Get locations
        val locations =
            locationRepository.getAll(
                collection = locationCollection,
                filter = filter
            )
        // Return
        return ApiResponse.success(locations)
    }

    suspend fun getById(orgId: String?, id: String?): ApiResponse<Location> {
        // Validation of parameters
        if (orgId == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest)
        if (id == null) return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Id is required")

        // Get organization
        val organization = organizationRepository.getById(organizationCollection, orgId) ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization not found"
        )

        // Get locations collection within organization database
        val db = client.getDatabase(organization.dbName)
        val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

        // Get location
        val location = locationRepository.getById(collection = locationCollection, id = id)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return
        return ApiResponse.success(location)
    }

    suspend fun create(request: CreateLocationRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            // start transaction
            session.startTransaction()

            // Get organization
            val organization = organizationRepository.getById(organizationCollection, request.organizationId)
                ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

            // Get locations collection within organization database
            val db = client.getDatabase(organization.dbName)
            val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

            // Create location within organization database
            if (!locationRepository.create(
                    collection = locationCollection,
                    session = session,
                    entity = request.toDomain()
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to create location"
                )
            }

            // commit transaction and return
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun update(request: UpdateLocationRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            // start transaction
            session.startTransaction()

            // Get organization
            val organization = organizationRepository.getById(organizationCollection, request.organizationId)
                ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

            // Get locations collection within organization database
            val db = client.getDatabase(organization.dbName)
            val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

            // Update location within organization database
            if (!locationRepository.updateById(
                    collection = locationCollection,
                    session = session,
                    id = request.locationId,
                    update = Updates.combine(
                        Updates.set(Location::phoneNumber.name, request.phoneNumber),
                        Updates.set(Location::locationMeta.name, request.locationMeta)
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to update location"
                )
            }

            // commit transaction and return
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun updateStatus(request: UpdateLocationStatusRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            // start transaction
            session.startTransaction()

            // Get organization
            val organization = organizationRepository.getById(organizationCollection, request.organizationId)
                ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

            // Get locations collection within organization database
            val db = client.getDatabase(organization.dbName)
            val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

            // Update location within organization database
            if (!locationRepository.updateById(
                    collection = locationCollection,
                    session = session,
                    id = request.locationId,
                    update = Updates.combine(
                        Updates.set(Location::open.name, request.open)
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to update location"
                )
            }

            // commit transaction and return
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}