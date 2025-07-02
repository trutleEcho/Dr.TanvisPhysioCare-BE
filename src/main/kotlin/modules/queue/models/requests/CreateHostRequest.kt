package com.modules.queue.models.requests

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateHostRequest(
    val organizationId: String,
    val employeeId: String,
    val name: String,
    val meta: Meta
)