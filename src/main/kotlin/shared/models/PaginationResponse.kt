package com.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    val data:List<T>,
    val totalCount: Long,
)