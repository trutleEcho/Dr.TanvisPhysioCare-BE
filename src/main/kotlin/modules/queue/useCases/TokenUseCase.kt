package com.modules.queue.useCases

import com.modules.core.models.entities.Organization
import com.modules.queue.mappers.toDomain
import com.modules.queue.models.entities.Token
import com.modules.queue.models.requests.token.CreateTokenRequest
import com.modules.queue.models.requests.token.DeleteTokenRequest
import com.modules.queue.models.requests.token.UpdateTokenRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.shared.Repository
import com.shared.config.Collections
import com.shared.config.Config
import com.shared.models.ApiResponse
import com.shared.models.PaginationResponse
import io.ktor.http.HttpStatusCode
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait

class TokenUseCase(
    private val client: CoroutineClient,
    private val organizationRepository: Repository<Organization>,
    private val tokenRepository: Repository<Token>
) {

    // Get organization collection from core database.
    val db = client.getDatabase(Config.DB_NAME)
    val organizationCollection = db.getCollection<Organization>(Collections.ORGANIZATIONS)

    suspend fun get(orgId: String?, filter: Bson): ApiResponse<Token> {
        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get token collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

        // Get token.
        val token = tokenRepository.get(collection = tokenCollection, filter = filter)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return token.
        return ApiResponse.success(token)
    }

    suspend fun getAll(orgId: String?, filter: Bson): ApiResponse<List<Token>> {
        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get token collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

        // Get tokens.
        val tokens = tokenRepository.getAll(collection = tokenCollection, filter = filter)

        // Return tokens.
        return ApiResponse.success(tokens)
    }

    suspend fun getById(orgId: String?, id: String?): ApiResponse<Token> {
        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )
        id ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Host Id is required")

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get token collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

        // Get token.
        val token = tokenRepository.getById(collection = tokenCollection, id = id)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.NoContent)

        // Return token.
        return ApiResponse.success(token)
    }

    suspend fun getPaginated(
        orgId: String?,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): ApiResponse<PaginationResponse<Token>> {
        // Validating parameters.
        orgId ?: return ApiResponse.failure(
            statusCode = HttpStatusCode.BadRequest,
            error = "Organization Id is required"
        )

        // Get organization.
        val organization = organizationRepository.getById(collection = organizationCollection, id = orgId)
            ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

        // Get token collection from organization database.
        val db = client.getDatabase(organization.dbName)
        val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

        // Get tokens.
        val tokens = tokenRepository.getPaginated(
            collection = tokenCollection,
            filter = filter,
            lastEntryId = lastEntryId,
            limit = limit
        )

        // Return tokens.
        return ApiResponse.success(tokens)
    }

    suspend fun create(request: CreateTokenRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            session.startTransaction()

            // Get organization.
            val organization =
                organizationRepository.getById(collection = organizationCollection, id = request.organizationId)
                    ?: return ApiResponse.failure(
                        statusCode = HttpStatusCode.BadRequest,
                        error = "Organization not found"
                    )

            // Get token collection from organization database.
            val db = client.getDatabase(organization.dbName)
            val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

            // Create token.
            if (!tokenRepository.create(collection = tokenCollection, session = session, entity = request.toDomain())) {
                session.abortTransactionAndAwait()
            }

            // Commit transaction and return.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun update(request: UpdateTokenRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            session.startTransaction()

            // Get organization.
            val organization =
                organizationRepository.getById(collection = organizationCollection, id = request.organizationId)
                    ?: return ApiResponse.failure(
                        statusCode = HttpStatusCode.BadRequest,
                        error = "Organization not found"
                    )

            // Get token collection from organization database.
            val db = client.getDatabase(organization.dbName)
            val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

            // Update token.
            if (!tokenRepository.update(
                    collection = tokenCollection,
                    session = session,
                    filter = Filters.eq(Token::_id.name, ObjectId(request.tokenId)),
                    update = Updates.combine(
                        Updates.set(Token::name.name, request.name),
                        Updates.set(Token::phoneNumber.name, request.phoneNumber),
                        Updates.set(Token::email.name, request.email),
                        Updates.set(Token::token.name, request.token),
                    )
                )
            ) {
                session.abortTransactionAndAwait()
            }

            // Commit transaction and return.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }

    suspend fun delete(request: DeleteTokenRequest): ApiResponse<Boolean> {
        client.startSession().use { session ->
            session.startTransaction()

            // Get organization.
            val organization = organizationRepository.getById(organizationCollection, request.organizationId)
                ?: return ApiResponse.failure(statusCode = HttpStatusCode.BadRequest, error = "Organization not found")

            // Get tokens collection within organization database.
            val db = client.getDatabase(organization.dbName)
            val tokenCollection = db.getCollection<Token>(Collections.TOKENS)

            // Delete token within organization database.
            if (!tokenRepository.delete(
                    collection = tokenCollection,
                    session = session,
                    filter = Filters.eq(Token::_id.name, ObjectId(request.tokenId))
                )
            ) {
                session.abortTransactionAndAwait()
                return ApiResponse.failure(
                    statusCode = HttpStatusCode.InternalServerError,
                    error = "Failed to delete token"
                )
            }

            // Commit transaction and return.
            session.commitTransactionAndAwait()
            return ApiResponse.success(true)
        }
    }
}