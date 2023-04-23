package com.example.kcodetest.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kcodetest.datasource.local.dao.AlbumBookmarkDao
import com.example.kcodetest.datasource.local.model.AlbumBookmark

@Database(entities = [AlbumBookmark::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumBookmarkDao(): AlbumBookmarkDao
}