package com.modules.queue.models.requests.token

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateTokenRequest(
    val organizationId: String,
    val locationId: String,
    val hostId: String,
    val name: String,
    val phoneNumber: String,
    val email: String?=null,
    val date: Long?=null,
    val meta: Meta
)