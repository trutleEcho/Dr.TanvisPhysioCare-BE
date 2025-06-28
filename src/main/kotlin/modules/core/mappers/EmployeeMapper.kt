package com.modules.core.mappers

import com.modules.core.models.entities.Employee
import com.modules.core.models.requests.CreateEmployeeRequest
import org.bson.types.ObjectId

fun CreateEmployeeRequest.toDomain(): Employee {
    return Employee(
        _id = ObjectId(),
        name = name,
        employeeType = employeeType,
        meta = meta
    )
}