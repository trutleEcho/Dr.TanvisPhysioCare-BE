package com.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val createdBy: String? = null,
    val createdAt: Long,
)