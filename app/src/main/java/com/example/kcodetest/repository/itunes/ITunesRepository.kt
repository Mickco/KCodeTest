package com.example.kcodetest.repository.itunes

import com.example.kcodetest.repository.model.ITunesAlbum
import com.example.kcodetest.repository.model.KResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface ITunesRepository {

    val bookmarkListFlow: Flow<KResult<List<String>>>

    suspend fun getITunesResultAsync(coroutineScope: CoroutineScope): Deferred<KResult<List<ITunesAlbum>>>

    suspend fun addBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: String
    ): Deferred<KResult<Unit>>

    suspend fun deleteBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: String
    ): Deferred<KResult<Unit>>
}