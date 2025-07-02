package com.modules.queue.models.entities

import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class Host(
    val hostId: String,
    val name: String,
    val meta: Meta
)
