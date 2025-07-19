package com.modules.core.models.entities

import com.shared.models.Meta
import com.shared.utils.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Organization(
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId val _id: ObjectId = ObjectId(),
    val dbName: String,
    val name: String,
    val logo: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val website: String? = null,
    val description: String? = null,
    val services: List<String>? = emptyList(),
    val employees: List<String>? = emptyList(),
    val meta: Meta
)