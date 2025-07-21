package com.modules.core.mappers

import com.modules.core.models.entities.Organization
import com.modules.core.models.requests.CreateOrganizationRequest
import com.modules.core.models.response.QueueOrganizationsResponse
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

fun List<Organization>.queueGetOrganizationsResponse(): List<QueueOrganizationsResponse> {
    return this.map {
        QueueOrganizationsResponse(it._id.toHexString(), it.name)
    }
}