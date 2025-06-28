package com.modules.core.models.requests

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrganizationRequest(
    val name: String,
    val meta: Meta
)
