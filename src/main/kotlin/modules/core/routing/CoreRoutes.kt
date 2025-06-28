package com.modules.core.routing

object CoreRoutes {
    private const val BASE = "/core"
    const val ORGANIZATIONS = "$BASE/organization"
    const val EMPLOYEES = "$BASE/employee"

    object Organizations {
        const val GET = ORGANIZATIONS
        const val GET_PAGINATED = "$ORGANIZATIONS/paginated"
        const val CREATE = "$ORGANIZATIONS/create"
        const val UPDATE_SERVICES = "$ORGANIZATIONS/update-services"
    }

    object Employees {
        const val GET = EMPLOYEES
        const val GET_PAGINATED = "$EMPLOYEES/paginated"
        const val CREATE = "$EMPLOYEES/create"
    }
}