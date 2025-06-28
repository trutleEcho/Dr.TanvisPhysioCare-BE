package com.modules.core.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrganizationServicesRequest(
    val id: String,
    val services: List<String>
)