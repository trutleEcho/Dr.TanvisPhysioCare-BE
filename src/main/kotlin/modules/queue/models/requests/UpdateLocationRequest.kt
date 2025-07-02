package com.modules.queue.models.requests

import com.modules.queue.models.entities.LocationMeta
import kotlinx.serialization.Serializable

@Serializable
data class UpdateLocationRequest(
    val organizationId: String,
    val name: String,
    val locationMeta: LocationMeta? = null,
)