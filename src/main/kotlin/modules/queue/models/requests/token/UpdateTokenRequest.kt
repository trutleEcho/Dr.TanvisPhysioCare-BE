package com.modules.queue.models.requests.token

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenRequest(
    val organizationId: String,
    val tokenId: String,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val token: Int? = null,
    val date: Long? = null
)