package com.modules.doctor.mappers

import com.modules.doctor.models.entities.Employee
import com.modules.doctor.models.requests.CreateEmployeeRequest
import org.bson.types.ObjectId

fun CreateEmployeeRequest.toDomain(): Employee {
    return Employee(
        _id = ObjectId(),
        name = name,
        employeeType = employeeType,
        meta = meta
    )
}