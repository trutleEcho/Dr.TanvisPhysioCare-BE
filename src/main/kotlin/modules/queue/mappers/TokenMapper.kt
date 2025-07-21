package com.modules.queue.mappers

import com.modules.queue.models.entities.Token
import com.modules.queue.models.requests.token.CreateTokenRequest
import org.bson.types.ObjectId

fun CreateTokenRequest.toDomain(token: Int) : Token{
    return Token(
        _id = ObjectId(),
        locationId = locationId,
        hostId = hostId,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        meta = meta,
        token = token,
        date = date
    )
}