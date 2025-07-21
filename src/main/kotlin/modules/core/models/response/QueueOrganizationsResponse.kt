package com.modules.core.models.response

import kotlinx.serialization.Serializable

@Serializable
data class QueueOrganizationsResponse(
    val organizationId: String,
    val organizationName: String
)
