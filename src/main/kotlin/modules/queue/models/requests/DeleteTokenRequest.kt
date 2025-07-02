package com.modules.queue.models.requests

@kotlinx.serialization.Serializable
data class DeleteTokenRequest(
    val organizationId: String,
    val tokenId: String
)
