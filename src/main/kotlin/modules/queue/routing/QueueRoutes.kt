package com.modules.queue.routing

object QueueRoutes {
    private const val BASE = "/queue"
    const val LOCATIONS = "$BASE/locations"
    const val HOSTS = "$BASE/hosts"
    const val TOKENS = "$BASE/tokens"

    object Locations {
        const val GET = LOCATIONS
        const val GET_PAGINATED = "$LOCATIONS/paginated"
        const val CREATE = "$LOCATIONS/create"
        const val UPDATE = "$LOCATIONS/update"
        const val UPDATE_STATUS = "$LOCATIONS/update-status"
    }

    object Hosts {
        const val GET = HOSTS
        const val GET_PAGINATED = "$HOSTS/paginated"
        const val CREATE = "$HOSTS/create"
    }

    object Tokens {
        const val GET = TOKENS
        const val GET_PAGINATED = "$TOKENS/paginated"
        const val CREATE = "$TOKENS/create"
        const val UPDATE = "$TOKENS/update"
        const val DELETE = "$TOKENS/delete"
    }
}