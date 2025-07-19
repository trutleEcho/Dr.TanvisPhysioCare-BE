package com.modules.core.routing

object CoreRoutes {
    private const val BASE = "/core"
    const val ORGANIZATIONS = "$BASE/organization"

    object Organizations {
        const val GET = ORGANIZATIONS
        const val GET_PAGINATED = "$ORGANIZATIONS/paginated"
        const val CREATE = "$ORGANIZATIONS/create"
        const val UPDATE_SERVICES = "$ORGANIZATIONS/update-services"
    }

    object Employees {
        const val GET = "$BASE/employee"
        const val GET_PAGINATED = "$GET/paginated"
        const val CREATE = "$GET/create"
    }
}