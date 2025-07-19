package com.modules.doctor.models.entities

import com.modules.doctor.models.valueObjects.EmployeeTypes
import com.shared.models.Meta
import com.shared.utils.ObjectIdSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Employee(
    @Serializable(with = ObjectIdSerializer::class)
    @BsonId val _id: ObjectId = ObjectId(),
    val name: String,
    val employeeType: EmployeeTypes,
    val perms: List<String> = emptyList(),
    val meta: Meta
)