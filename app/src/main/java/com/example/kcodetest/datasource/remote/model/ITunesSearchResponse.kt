package com.example.kcodetest.datasource.remote.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ITunesSearchResponse(
    val resultCount: Int,
    val results: List<Result>
)

@JsonClass(generateAdapter = true)
data class Result(
    val amgArtistId: String?,
    val artistId: String,
    val artistName: String,
    val artistViewUrl: String,
    val artworkUrl100: String,
    val artworkUrl60: String,
    val collectionCensoredName: String,
    val collectionExplicitness: String,
    val collectionId: String,
    val collectionName: String,
    val collectionPrice: Double?,
    val collectionType: String,
    val collectionViewUrl: String,
    val contentAdvisoryRating: String?,
    val copyright: String,
    val country: String,
    val currency: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackCount: Int,
    val wrapperType: String
)