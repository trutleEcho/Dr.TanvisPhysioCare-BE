package com.modules.queue.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String?=null,
    val name: String,
    val email: String?=null,
    val phoneNumber: String
)
