package com.modules.queue.mappers

import com.modules.queue.models.entities.Host
import com.modules.queue.models.requests.CreateHostRequest

fun CreateHostRequest.toDomain(): Host{
    return Host(
        hostId = employeeId,
        name = name,
        meta = meta
    )
}