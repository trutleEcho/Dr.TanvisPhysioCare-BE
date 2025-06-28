package com.modules.queue.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val organizationId: String,
    val name: String,
    val open: Boolean
)