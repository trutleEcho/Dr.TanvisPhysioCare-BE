package com.modules.queue.mappers

import com.modules.queue.models.entities.Token
import com.modules.queue.models.requests.CreateTokenRequest
import org.bson.types.ObjectId

fun CreateTokenRequest.toDomain() : Token{
    return Token(
        _id = ObjectId(),
        locationId = locationId,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        meta = meta,
        token = token,
    )
}