package com.modules.queue.models

import kotlinx.serialization.Serializable

@Serializable
data class Host(
    val hostId: String,
    val name: String,
    val open: Boolean
)
