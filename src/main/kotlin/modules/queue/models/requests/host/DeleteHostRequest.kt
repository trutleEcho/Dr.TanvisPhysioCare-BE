package com.modules.queue.models.requests.host

import kotlinx.serialization.Serializable

@Serializable
data class DeleteHostRequest(
    val organizationId: String,
    val locationId: String,
    val hostId: String
)