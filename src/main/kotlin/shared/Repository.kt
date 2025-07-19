package com.shared

import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.ClientSession
import com.shared.models.PaginationResponse
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineCollection

interface Repository<T : Any> {
    suspend fun exists(collection: CoroutineCollection<T>, filter: Bson): Boolean
    suspend fun count(collection: CoroutineCollection<T>, filter: Bson): Long
    suspend fun get(collection: CoroutineCollection<T>, filter: Bson): T?
    suspend fun getById(collection: CoroutineCollection<T>, id: String): T?
    suspend fun getAll(collection: CoroutineCollection<T>, filter: Bson = Filters.empty()): List<T>
    suspend fun getPaginated(
        collection: CoroutineCollection<T>,
        filter: Bson = Filters.empty(),
        lastEntryId: String? = null,
        limit: Int = 10
    ): PaginationResponse<T>

    suspend fun create(collection: CoroutineCollection<T>, session: ClientSession? = null, entity: T): Boolean
    suspend fun update(
        collection: CoroutineCollection<T>,
        session: ClientSession? = null,
        filter: Bson,
        update: Bson
    ): Boolean

    suspend fun updateById(
        collection: CoroutineCollection<T>,
        session: ClientSession? = null,
        id: String,
        update: Bson
    ): Boolean

    suspend fun delete(collection: CoroutineCollection<T>, session: ClientSession? = null, filter: Bson): Boolean
    suspend fun deleteAll(collection: CoroutineCollection<T>, session: ClientSession? = null, filter: Bson): Boolean
    suspend fun deleteById(collection: CoroutineCollection<T>, session: ClientSession? = null, id: String): Boolean
}