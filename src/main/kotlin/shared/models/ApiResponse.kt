package com.shared.models

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

/**
 * A generic API response model that can be used to standardize the structure of API responses.
 *
 * @param T The type of data being returned in the response.
 * @property statusCode The HTTP status code of the response.
 * @property data The data returned in the response, if any.
 * @property error An error message, if any.
 */
@Serializable
data class ApiResponse<T>(
    val statusCode: Int,
    val data: T? = null,
    val error: String? = null
) {
    companion object {
        fun <T> success(data: T, statusCode: HttpStatusCode = HttpStatusCode.OK): ApiResponse<T> {
            return ApiResponse(statusCode = statusCode.value, data = data)
        }

        fun <T> failure(statusCode: HttpStatusCode,error: String? = null): ApiResponse<T> {
            return ApiResponse(statusCode = statusCode.value, error = error)
        }
    }
}
