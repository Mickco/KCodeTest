package com.example.kcodetest.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumBookmark(
    @PrimaryKey val collectionId: Int,
)
