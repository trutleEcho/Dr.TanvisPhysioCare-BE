package com.modules.queue.mappers

import com.modules.queue.models.entities.Location
import com.modules.queue.models.requests.CreateLocationRequest

fun CreateLocationRequest.toDomain(): Location {
    return Location(
        organizationId = organizationId,
        name = name,
        open = false,
        entityIn = false,
        meta = meta
    )
}