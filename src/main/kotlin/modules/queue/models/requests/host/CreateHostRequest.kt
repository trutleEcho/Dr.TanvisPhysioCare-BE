package com.modules.queue.models.requests.host

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateHostRequest(
    val organizationId: String,
    val locationId: String,
    val locationName: String,
    val name: String,
    val phoneNumber: String? = null,
    val meta: Meta
)