package com.modules.queue.repository

import com.modules.queue.models.entities.Token
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.reactivestreams.client.ClientSession
import com.shared.Repository
import com.shared.models.PaginationResponse
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection

class TokenRepository : Repository<Token> {
    override suspend fun exists(
        collection: CoroutineCollection<Token>,
        filter: Bson
    ): Boolean {
        return collection.countDocuments(filter) > 0
    }

    override suspend fun count(
        collection: CoroutineCollection<Token>,
        filter: Bson
    ): Long {
        return collection.countDocuments(filter)
    }

    override suspend fun get(
        collection: CoroutineCollection<Token>,
        filter: Bson
    ): Token? {
        return collection.findOne(filter)
    }

    override suspend fun getById(
        collection: CoroutineCollection<Token>,
        id: String
    ): Token? {
        return collection.findOneById(ObjectId(id))
    }

    override suspend fun getAll(
        collection: CoroutineCollection<Token>,
        filter: Bson
    ): List<Token> {
        return collection.find(filter).toList()
    }

    override suspend fun getPaginated(
        collection: CoroutineCollection<Token>,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): PaginationResponse<Token> {
        val finalFilter =
            if (lastEntryId != null) Filters.and(filter, Filters.gt("meta.createdAt", lastEntryId)) else filter
        val users = collection.find(finalFilter).sort(descending("meta.createdAt")).limit(limit).toList()
        val total = collection.countDocuments(filter)
        return PaginationResponse(users, total)
    }

    override suspend fun create(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        entity: Token
    ): Boolean {
        return collection.insertOne(entity).wasAcknowledged()
    }

    override suspend fun update(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        filter: Bson,
        update: Bson
    ): Boolean {
        return collection.updateOne(filter, update).wasAcknowledged()
    }

    override suspend fun updateById(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        id: String,
        update: Bson
    ): Boolean {
        return collection.updateOneById(ObjectId(id), update).wasAcknowledged()
    }

    override suspend fun delete(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun deleteAll(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {
        return collection.deleteMany(filter).wasAcknowledged()
    }

    override suspend fun deleteById(
        collection: CoroutineCollection<Token>,
        session: ClientSession?,
        id: String
    ): Boolean {
        return collection.deleteOneById(ObjectId(id)).wasAcknowledged()
    }

}