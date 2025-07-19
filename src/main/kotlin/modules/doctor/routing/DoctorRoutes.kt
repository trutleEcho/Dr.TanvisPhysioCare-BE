package com.modules.doctor.routing

object DoctorRoutes {
    private const val BASE = "/doctor"
    const val EMPLOYEES = "$BASE/employee"

    object Employees {
        const val GET = EMPLOYEES
        const val GET_PAGINATED = "$EMPLOYEES/paginated"
        const val CREATE = "$EMPLOYEES/create"
    }
}