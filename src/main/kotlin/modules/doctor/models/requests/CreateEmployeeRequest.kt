package com.modules.doctor.models.requests

import com.modules.doctor.models.valueObjects.EmployeeTypes
import com.shared.models.Meta
import kotlinx.serialization.Serializable

@Serializable
data class CreateEmployeeRequest(
    val organizationId: String,
    val name: String,
    val employeeType: EmployeeTypes,
    val meta: Meta
)