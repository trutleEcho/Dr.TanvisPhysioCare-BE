package com.shared.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.BsonDateTime
import org.bson.types.ObjectId

object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toHexString())
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        val string = decoder.decodeString()
        return ObjectId(string)
    }
}


object BsonDateTimeSerializer : KSerializer<BsonDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BsonDateTime", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: BsonDateTime) {
        encoder.encodeLong(value.value) // serialize as epoch millis
    }

    override fun deserialize(decoder: Decoder): BsonDateTime {
        val millis = decoder.decodeLong()
        return BsonDateTime(millis)
    }
}