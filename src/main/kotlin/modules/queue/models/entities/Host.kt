package com.modules.queue.models.entities

import com.shared.models.Meta
import com.shared.utils.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Host(
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId val _id: ObjectId = ObjectId(),
    val locationId: String,
    val locationName: String,
    val name: String,
    val phoneNumber: String? = null,
    val token: String? = null,
    val hostIn: Boolean,
    val meta: Meta
)
