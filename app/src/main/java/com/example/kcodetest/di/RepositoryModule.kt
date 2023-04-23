package com.example.kcodetest.di

import com.example.kcodetest.datasource.local.dao.AlbumBookmarkDao
import com.example.kcodetest.datasource.remote.service.ITunesApiService
import com.example.kcodetest.repository.itunes.ITunesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    fun provideITunesRepository(
        iTunesApiService: ITunesApiService,
        albumBookmarkDao: AlbumBookmarkDao
    ): ITunesRepositoryImpl {
        return ITunesRepositoryImpl(
            iTunesApiService = iTunesApiService,
            albumBookmarkDao = albumBookmarkDao
        )
    }
}