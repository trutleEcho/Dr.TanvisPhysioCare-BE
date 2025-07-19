package com.modules.queue.mappers

import com.modules.queue.models.entities.Location
import com.modules.queue.models.requests.location.CreateLocationRequest

fun CreateLocationRequest.toDomain(): Location {
    return Location(
        organizationId = organizationId,
        name = name,
        phoneNumber = phoneNumber,
        open = false,
        locationMeta = locationMeta,
        meta = meta
    )
}