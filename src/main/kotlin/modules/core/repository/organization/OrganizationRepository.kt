package com.modules.core.repository.organization

import com.modules.core.models.entities.Organization
import com.shared.Repository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.reactivestreams.client.ClientSession
import com.shared.models.PaginationResponse
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection

class OrganizationRepository() : Repository<Organization> {

    override suspend fun exists(collection: CoroutineCollection<Organization>, filter: Bson): Boolean {
        return collection.countDocuments(filter) > 0
    }

    override suspend fun count(collection: CoroutineCollection<Organization>, filter: Bson): Long {
        return collection.countDocuments(filter)
    }

    override suspend fun get(collection: CoroutineCollection<Organization>, filter: Bson): Organization? {
        return collection.findOne(filter)
    }

    override suspend fun getById(collection: CoroutineCollection<Organization>, id: String): Organization? {
        return collection.findOneById(ObjectId(id))
    }

    override suspend fun getAll(collection: CoroutineCollection<Organization>, filter: Bson): List<Organization> {
        return collection.find(filter).toList()
    }

    override suspend fun getPaginated(
        collection: CoroutineCollection<Organization>,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): PaginationResponse<Organization> {
        val finalFilter = if (lastEntryId != null) Filters.and(filter, Filters.gt("meta.createdAt", lastEntryId)) else filter
        val organizations = collection.find(finalFilter).sort(descending("meta.createdAt")).limit(limit).toList()
        val total = collection.countDocuments(filter)
        return PaginationResponse(organizations, total)
    }

    override suspend fun create(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        entity: Organization
    ): Boolean {
        return collection.insertOne(entity).wasAcknowledged()
    }

    override suspend fun update(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        filter: Bson,
        update: Bson
    ): Boolean {
        return collection.updateOne(filter, update).wasAcknowledged()
    }

    override suspend fun updateById(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        id: String,
        update: Bson
    ): Boolean {
        return collection.updateOneById(id, update).wasAcknowledged()
    }

    override suspend fun delete(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {
        return collection.deleteOne(filter).wasAcknowledged()
    }

    override suspend fun deleteAll(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {
        return collection.deleteMany(filter).wasAcknowledged()
    }

    override suspend fun deleteById(
        collection: CoroutineCollection<Organization>,
        session: ClientSession?,
        id: String
    ): Boolean {
        return collection.deleteOneById(id).wasAcknowledged()
    }
}