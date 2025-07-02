package com.modules.queue.models.entities

import com.shared.models.Meta
import com.shared.utils.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Location(
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId val _id: ObjectId = ObjectId(),
    val organizationId: String,
    val name: String,
    val open: Boolean,
    val token: Int? = null,
    val entityIn: Boolean,
    val locationMeta: LocationMeta? = null,
    val meta: Meta
)

@Serializable
data class LocationMeta(
    val address: String? = null,
    val closeTime: Long? = null,
    val openTime: Long? = null,
    val schedule: List<DailySchedule> = emptyList(),
    val phone: String? = null
)

@Serializable
data class DailySchedule(
    val day: String,
    val openTime: Long,
    val closeTime: Long
)