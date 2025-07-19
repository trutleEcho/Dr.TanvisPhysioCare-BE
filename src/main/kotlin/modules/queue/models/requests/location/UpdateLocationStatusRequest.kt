package com.modules.queue.models.requests.location

import kotlinx.serialization.Serializable

@Serializable
data class UpdateLocationStatusRequest(
    val organizationId: String,
    val locationId: String,
    val open: Boolean,
)
