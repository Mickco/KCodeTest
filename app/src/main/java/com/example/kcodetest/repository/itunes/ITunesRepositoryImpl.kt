package com.example.kcodetest.repository.itunes

import com.example.kcodetest.datasource.local.dao.AlbumBookmarkDao
import com.example.kcodetest.datasource.local.model.AlbumBookmark
import com.example.kcodetest.datasource.remote.service.ITunesApiService
import com.example.kcodetest.repository.BaseRepository
import com.example.kcodetest.repository.model.ITunesAlbum
import com.example.kcodetest.repository.model.KResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class ITunesRepositoryImpl(
    private val iTunesApiService: ITunesApiService,
    private val albumBookmarkDao: AlbumBookmarkDao
) : BaseRepository() {

    val bookmarkListFlow: Flow<KResult<List<Int>>> = albumBookmarkDao.getAll().map {
        KResult.Success(it.map { it.collectionId }) as KResult<List<Int>>
    }.catch { e ->
        emit(handleException(e))
    }.flowOn(Dispatchers.IO)


    suspend fun getITunesResultAsync(coroutineScope: CoroutineScope): Deferred<KResult<List<ITunesAlbum>>> {
        return executeAsyncCall(coroutineScope) {
            val apiResult = iTunesApiService.getSearchResponse()
            apiResult.results.map {
                ITunesAlbum(
                    collectionId = it.collectionId,
                    artworkUrl100 = it.artworkUrl100,
                    artistName = it.artistName,
                    collectionName = it.collectionName
                )
            }
        }
    }


    suspend fun addBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: Int
    ): Deferred<KResult<Unit>> {
        return executeAsyncCall(coroutineScope) {
            albumBookmarkDao.insert(albumBookmark = AlbumBookmark(collectionId))
        }
    }

    suspend fun deleteBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: Int
    ): Deferred<KResult<Unit>> {
        return executeAsyncCall(coroutineScope) {
            albumBookmarkDao.delete(albumBookmark = AlbumBookmark(collectionId))
        }
    }

}