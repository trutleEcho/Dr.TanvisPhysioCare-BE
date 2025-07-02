package com.modules.queue.models.requests

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateTokenRequest(
    val organizationId: String,
    val locationId: String,
    val name: String,
    val phoneNumber: String,
    val email: String?=null,
    val token: String?=null,
    val meta: Meta
)