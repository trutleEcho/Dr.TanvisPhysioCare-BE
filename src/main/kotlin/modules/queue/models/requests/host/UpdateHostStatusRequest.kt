package com.modules.queue.models.requests.host

import kotlinx.serialization.Serializable

@Serializable
data class UpdateHostStatusRequest(
    val organizationId: String,
    val hostId: String,
    val hostIn: Boolean,
    val token: String
)