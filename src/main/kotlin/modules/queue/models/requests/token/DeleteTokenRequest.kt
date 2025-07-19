package com.modules.queue.models.requests.token

import kotlinx.serialization.Serializable

@Serializable
data class DeleteTokenRequest(
    val organizationId: String,
    val tokenId: String
)
