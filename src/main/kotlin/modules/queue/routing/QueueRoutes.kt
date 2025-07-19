package com.modules.queue.routing

object QueueRoutes {
    private const val BASE = "/queue"
    const val LOCATIONS = "$BASE/locations"
    const val HOSTS = "$BASE/hosts"
    const val TOKENS = "$BASE/tokens"

    object Locations {
        const val GET = LOCATIONS
        const val CREATE = "$LOCATIONS/create"
        const val UPDATE = "$LOCATIONS/update"
        const val UPDATE_STATUS = "$LOCATIONS/update-status"
        const val DELETE = "$LOCATIONS/delete"
    }

    object Hosts {
        const val GET = HOSTS
        const val CREATE = "$HOSTS/create"
        const val UPDATE = "$HOSTS/update"
        const val UPDATE_STATUS = "$HOSTS/update-status"
        const val DELETE = "$HOSTS/delete"
    }

    object Tokens {
        const val GET = TOKENS
        const val CREATE = "$TOKENS/create"
        const val UPDATE = "$TOKENS/update"
        const val DELETE = "$TOKENS/delete"
    }
}