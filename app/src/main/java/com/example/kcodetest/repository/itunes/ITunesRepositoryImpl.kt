package com.example.kcodetest.repository.itunes

import com.example.kcodetest.datasource.remote.service.ITunesApiService
import com.example.kcodetest.repository.BaseRepository
import com.example.kcodetest.repository.model.ITunesAlbum
import com.example.kcodetest.repository.model.RepositoryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred


class ITunesRepositoryImpl(
    private val iTunesApiService: ITunesApiService,
) : BaseRepository() {


    suspend fun getITunesResult(coroutineScope: CoroutineScope): Deferred<RepositoryResult<List<ITunesAlbum>>> {
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

}