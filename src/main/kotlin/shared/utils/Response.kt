package com.shared.utils

import com.shared.models.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import kotlinx.serialization.json.Json
import kotlin.to
import kotlin.toString

/**
 * Utility object for sending API responses.
 */
object Response {

    /**
     * Sends an encrypted response using the [apiResponse] models, determining the correct response based on status code.
     *
     * @param call The [ApplicationCall] to respond to.
     * @param apiResponse The [apiResponse] object containing the status, data, and error message.
     */
    suspend inline fun <reified T> send(call: ApplicationCall, apiResponse: ApiResponse<T>, route: String = "") {
        val jsonResponse = if (apiResponse.data != null) {
            logger(
                data = apiResponse.data,
                route,
                "API executed successfully.",
                "success"
            )
            Json.encodeToString(mapOf("data" to apiResponse.data))
        } else {
            logger(
                details = apiResponse.error.toString(),
                endpoint = route,
                data = "Something went wrong.",
                level = "error"
            )
            Json.encodeToString(mapOf("error" to apiResponse.error))
        }
//        val encryptedResponse: String = if (ServerProperties.IS_DEBUG)
//            jsonResponse // Keep as string in debug mode
//        else CryptoUtil.encrypt(jsonResponse) // Encrypt in production


        val statusCode =
            HttpStatusCode.Companion.fromValue(apiResponse.statusCode)

        when (statusCode) {
            HttpStatusCode.Companion.OK -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.BadRequest -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.Unauthorized -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.Forbidden -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.NotFound -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.NoContent -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.TooManyRequests -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.InternalServerError -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            HttpStatusCode.Companion.Conflict -> call.respondText(
                jsonResponse,
                status = statusCode,
                contentType = ContentType.Application.Json
            )

            else -> call.respondText(jsonResponse, status = statusCode, contentType = ContentType.Application.Json)
        }
    }
}
