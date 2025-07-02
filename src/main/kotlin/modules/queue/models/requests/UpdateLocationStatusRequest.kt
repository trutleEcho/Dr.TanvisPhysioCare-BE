package com.modules.queue.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateLocationStatusRequest(
    val organizationId: String,
    val name: String,
    val open: Boolean,
    val token: Int,
    val entityIn: Boolean,
)
