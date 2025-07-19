package com.modules.queue.mappers

import com.modules.queue.models.entities.Host
import com.modules.queue.models.requests.host.CreateHostRequest

fun CreateHostRequest.toDomain(): Host{
    return Host(
        name = name,
        meta = meta,
        locationId = locationId,
        locationName = locationName,
        phoneNumber = phoneNumber,
        hostIn = false
    )
}