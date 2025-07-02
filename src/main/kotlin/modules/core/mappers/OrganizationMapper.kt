package com.modules.core.mappers

import com.modules.core.models.entities.Organization
import com.modules.core.models.requests.CreateOrganizationRequest
import org.bson.types.ObjectId

fun CreateOrganizationRequest.toDomain(): Organization {
    return Organization(
        _id = ObjectId(),
        dbName = name.replace(" ", "_"),
        name = name,
        meta = meta
    )
}

fun Organization.toGetResponse(): String {
    return _id.toHexString()
}