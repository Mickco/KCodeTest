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

/** This class demonstrate both the kotlin flow reactive way and coroutine/async way **/
class ITunesRepositoryImpl(
    private val iTunesApiService: ITunesApiService,
    private val albumBookmarkDao: AlbumBookmarkDao
) : BaseRepository(), ITunesRepository {

    override val bookmarkListFlow: Flow<KResult<List<String>>> = albumBookmarkDao.getAll().map {
        KResult.Success(it.map { it.collectionId }) as KResult<List<String>>
    }.catch { e ->
        emit(handleException(e))
    }.flowOn(Dispatchers.IO)


    override suspend fun getITunesResultAsync(coroutineScope: CoroutineScope): Deferred<KResult<List<ITunesAlbum>>> {
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


    override suspend fun addBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: String
    ): Deferred<KResult<Unit>> {
        return executeAsyncCall(coroutineScope) {
            albumBookmarkDao.insert(albumBookmark = AlbumBookmark(collectionId))
        }
    }

    override suspend fun deleteBookmarkAsync(
        coroutineScope: CoroutineScope,
        collectionId: String
    ): Deferred<KResult<Unit>> {
        return executeAsyncCall(coroutineScope) {
            albumBookmarkDao.delete(albumBookmark = AlbumBookmark(collectionId))
        }
    }

}