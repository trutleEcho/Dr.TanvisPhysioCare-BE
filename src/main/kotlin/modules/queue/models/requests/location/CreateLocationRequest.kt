package com.modules.queue.models.requests.location

import com.modules.queue.models.entities.LocationMeta
import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateLocationRequest(
    val organizationId: String,
    val name: String,
    val phoneNumber: String? = null,
    val locationMeta: LocationMeta? = null,
    val meta: Meta
)