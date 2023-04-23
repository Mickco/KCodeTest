package com.example.kcodetest.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kcodetest.datasource.local.model.AlbumBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumBookmarkDao {

    @Query("SELECT * FROM AlbumBookmark")
    fun getAll(): Flow<List<AlbumBookmark>>

    @Insert
    suspend fun insert(albumBookmark: AlbumBookmark)

    @Delete
    suspend fun delete(albumBookmark: AlbumBookmark)
}