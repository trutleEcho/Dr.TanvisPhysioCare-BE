package com.modules.queue.models.requests.host

import kotlinx.serialization.Serializable

@Serializable
data class UpdateHostRequest(
    val organizationId: String,
    val hostId: String,
    val name: String,
    val phoneNumber: String? = null
)