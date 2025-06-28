package com.modules.core.repository.employee

import com.modules.core.models.entities.Employee
import com.shared.Repository
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.ClientSession
import com.shared.models.PaginationResponse
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection

class EmployeeRepository() : Repository<Employee> {

    override suspend fun exists(collection: CoroutineCollection<Employee>, filter: Bson): Boolean {
        return collection.find(filter).limit(1).first() != null
    }

    override suspend fun count(collection: CoroutineCollection<Employee>, filter: Bson): Long {

        return collection.countDocuments(filter)
    }


    override suspend fun get(collection: CoroutineCollection<Employee>, filter: Bson): Employee? {

        return collection.findOne(filter)
    }


    override suspend fun getById(collection: CoroutineCollection<Employee>, id: String): Employee? {

        return collection.findOneById(ObjectId(id))
    }


    override suspend fun getAll(collection: CoroutineCollection<Employee>, filter: Bson): List<Employee> {

        return collection.find(filter).toList()
    }


    override suspend fun getPaginated(
        collection: CoroutineCollection<Employee>,
        filter: Bson,
        lastEntryId: String?,
        limit: Int
    ): PaginationResponse<Employee> {

        val finalFilter =
            if (lastEntryId != null) Filters.and(filter, Filters.gt("meta.createdAt", lastEntryId)) else filter
        val doctors = collection.find(finalFilter).limit(limit).toList()
        val total = collection.countDocuments(filter)
        return PaginationResponse(doctors, total)

    }

    override suspend fun create(
        collection: CoroutineCollection<Employee>,
        session: ClientSession?,
        entity: Employee
    ): Boolean {

        return collection.insertOne(entity).wasAcknowledged()
    }


    override suspend fun update(
        collection: CoroutineCollection<Employee>,
        session: ClientSession?,
        filter: Bson,
        update: Bson
    ): Boolean {

        return collection.updateOne(filter, update).wasAcknowledged()
    }


    override suspend fun updateById(
        collection: CoroutineCollection<Employee>,
        session: ClientSession?,
        id: String,
        update: Bson
    ): Boolean {
        return collection.updateOneById(id, update).wasAcknowledged()
    }

    override suspend fun delete(
        collection: CoroutineCollection<Employee>,
        session: ClientSession?,
        filter: Bson
    ): Boolean {

        return collection.deleteOne(filter).wasAcknowledged()

    }

    override suspend fun deleteById(
        collection: CoroutineCollection<Employee>,
        session: ClientSession?,
        id: String
    ): Boolean {

        return collection.deleteOneById(id).wasAcknowledged()
    }
}