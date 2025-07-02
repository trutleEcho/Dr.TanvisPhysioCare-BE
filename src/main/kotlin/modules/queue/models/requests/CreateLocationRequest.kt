package com.modules.queue.models.requests

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateLocationRequest(
    val organizationId: String,
    val name: String,
    val meta: Meta
)