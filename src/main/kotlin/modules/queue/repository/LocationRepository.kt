package com.modules.queue.repository

import com.modules.queue.models.entities.Location
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.reactivestreams.client.ClientSession
import com.shared.Repository
import com.shared.models.PaginationResponse
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection

class LocationRepository : Repository<Location> {
    override suspend fun exists(
        collection: CoroutineCollection<Location>,
        filter: Bson
    ): Boolean {
        return collection.countDocuments(filter) > 0
    }

    override suspend fun count(
        collection: CoroutineCollection<Location>,
        filter: Bson
    ): Long {
        return collection.countDocuments(filter)
    }

    override suspend fun get(
        collection: CoroutineCollection<Location>,
        filter: Bson
    ): Location? {
        return collection.findOne(filter)
    }

    override suspend fun getById(
        collection: CoroutineCollection<Location>,
        id: String
    ): Location? {
        return collection.findOneById(ObjectId(id))
    }

    override suspend fun getAll(
        collection: CoroutineCollection<Location>,
        filter: Bson
    ): List<Location> {
        return collection.find(filter).toList()
    }

    override suspend fun getPaginated(
        collection: CoroutineCollection<Location>,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): PaginationResponse<Location> {
        val finalFilter =
            if (lastEntryId != null) Filters.and(filter, Filters.gt("meta.createdAt", lastEntryId)) else filter
        val locations = collection.find(finalFilter).sort(descending("meta.createdAt")).limit(limit).toList()
        val total = collection.countDocuments(filter)
        return PaginationResponse(locations, total)
    }

    override suspend fun create(
        collection: CoroutineCollection<Location>,
        session: ClientSession?,
        entity: Location
    ): Boolean {
        return collection.insertOne(entity).wasAcknowledged()
    }

    override suspend fun update(
        collection: CoroutineCollection<Location>,
        session: ClientSession?,
        filter: Bson,
        update: Bson
    ): Boolean {
        return collection.updateOne(filter, update).wasAcknowledged()
    }

    override suspend fun updateById(
        collection: CoroutineCollection<Location>,
        session: ClientSession?,
        id: String,
        update: Bson
    ): Boolean {
        return collection.updateOneById(ObjectId(id), update).wasAcknowledged()
    }

    override suspend fun delete(
        collection: CoroutineCollection<Location>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun deleteById(
        collection: CoroutineCollection<Location>,
        session: ClientSession?,
        id: String
    ): Boolean {
        return collection.deleteOneById(ObjectId(id)).wasAcknowledged()
    }
}