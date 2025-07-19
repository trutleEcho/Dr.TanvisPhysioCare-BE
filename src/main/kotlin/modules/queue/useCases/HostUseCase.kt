package com.modules.queue.useCases

import com.modules.core.models.entities.Organization
import com.modules.queue.mappers.toDomain
import com.modules.queue.models.entities.Host
import com.modules.queue.models.entities.Location
import com.modules.queue.models.entities.LocationHost
import com.modules.queue.models.entities.Token
import com.modules.queue.models.requests.host.CreateHostRequest
import com.modules.queue.models.requests.host.DeleteHostRequest
import com.modules.queue.models.requests.host.UpdateHostRequest
import com.modules.queue.models.requests.host.UpdateHostStatusRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.shared.Repository
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import com.shared.models.PaginationResponse
import io.ktor.http.HttpStatusCode
import org.bson.Document
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class HostUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>,
    private val locationRepository: Repository<Location>,
    private val hostRepository: Repository<Host>,
    private val tokenRepository: Repository<Token>
) {

    // Get organization collection from core database.
    val db = client.getDatabase(Config.DB_NAME)
    val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)

    suspend fun get(orgId: String?, hostId: String?): ApiResponse<Host> {

        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )
        hostId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Host Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get host collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val hostCollection = db.getCollection<Host>(Collections.HOSTS)

        // Get host.
        val host =
            hostRepository.getById(collection = hostCollection, id = hostId)
                ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return host.
        return ApiResponse.success(host)
    }

    suspend fun getAll(orgId: String?, locationId: String?): ApiResponse<List<Host>> {

        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )
        locationId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Location Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NotFound, error = "Organization not found")

        // Get host collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val hostCollection = db.getCollection<Host>(Collections.HOSTS)

        // Get host.
        val hosts =
            hostRepository.getAll(collection = hostCollection, filter = Filters.eq(Host::locationId.name, locationId))

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
            val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)

            val host = request.toDomain()
            // Create host.
            if (!hostRepository.create(collection = hostCollection, session = session, entity = host)) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to create host"
                )
            }

            // add host to location
            if (!locationRepository.updateById(
                    collection = locationCollection,
                    session = session,
                    id = request.locationId,
                    update = Updates.addToSet(
                        Location::hosts.name, LocationHost(
                            hostId = host._id.toString(),
                            hostName = host.name,
                            hostPhoneNumber = host.phoneNumber
                        )
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to add host to location"
                )
            }

            // Commit transaction and return host.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun update(request: UpdateHostRequest): ApiResponse<Boolean> {
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

            // Update host.
            if (!hostRepository.updateById(
                    collection = hostCollection, session = session, id = request.hostId, update = Updates.combine(
                        Updates.set(Host::name.name, request.name),
                        Updates.set(Host::phoneNumber.name, request.phoneNumber)
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to update host"
                )
            }

            // Commit transaction and return host.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun updateHostStatus(request: UpdateHostStatusRequest): ApiResponse<Boolean> {
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

            // Update host.
            if (!hostRepository.updateById(
                    collection = hostCollection, session = session, id = request.hostId, update = Updates.combine(
                        Updates.set(Host::hostIn.name, request.hostIn),
                        Updates.set(Host::token.name, request.token)
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to update host"
                )
            }

            // Commit transaction and return host.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun delete(request: DeleteHostRequest): ApiResponse<Boolean> {
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
            val locationCollection = db.getCollection<Location>(Collections.LOCATIONS)
            val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

            // Delete host.
            if (!hostRepository.deleteById(collection = hostCollection, session = session, id = request.hostId)) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to delete host"
                )
            }

            // remove host from location
            if (!locationRepository.updateById(
                    collection = locationCollection,
                    session = session,
                    id = request.locationId,
                    update = Updates.pull(
                        Location::hosts.name,
                        Document("hostId", request.hostId)
                    )
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to remove host from location"
                )
            }

            // remove tokens for host
            if (!tokenRepository.deleteAll(collection = tokenCollection, session = session, filter = Filters.eq(Token::hostId.name, request.hostId))) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to remove tokens for host"
                )
            }

            // Commit transaction and return host.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}