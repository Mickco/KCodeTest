package com.example.kcodetest.di

import android.content.Context
import androidx.room.Room
import com.example.kcodetest.BuildConstant.APP_DATABASE_NAME
import com.example.kcodetest.datasource.local.AppDatabase
import com.example.kcodetest.datasource.local.dao.AlbumBookmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, APP_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideAlbumBookmarkDao(
        appDatabase: AppDatabase
    ): AlbumBookmarkDao {
        return appDatabase.albumBookmarkDao()
    }

}