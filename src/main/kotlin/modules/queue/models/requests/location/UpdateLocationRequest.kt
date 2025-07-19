package com.modules.queue.models.requests.location

import com.modules.queue.models.entities.LocationMeta
import kotlinx.serialization.Serializable

@Serializable
data class UpdateLocationRequest(
    val organizationId: String,
    val locationId: String,
    val phoneNumber: String? = null,
    val locationMeta: LocationMeta? = null,
)