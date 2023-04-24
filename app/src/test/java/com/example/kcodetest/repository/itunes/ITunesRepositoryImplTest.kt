package com.example.kcodetest.repository.itunes

import com.example.kcodetest.BaseUnitTest
import com.example.kcodetest.datasource.local.dao.AlbumBookmarkDao
import com.example.kcodetest.datasource.local.model.AlbumBookmark
import com.example.kcodetest.datasource.remote.model.ITunesSearchResponse
import com.example.kcodetest.datasource.remote.model.Result
import com.example.kcodetest.datasource.remote.service.ITunesApiService
import com.example.kcodetest.repository.model.ErrorCode
import com.example.kcodetest.repository.model.ITunesAlbum
import com.example.kcodetest.repository.model.KResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class ITunesRepositoryImplTest : BaseUnitTest() {

    val iTunesSearchResponse = ITunesSearchResponse(
        resultCount = 1,
        results = listOf(
            Result(
                amgArtistId = "468749",
                artistId = "909253",
                artistName = "Jack Johnson",
                artistViewUrl = "https://music.apple.com/us/artist/jack-johnson/909253?uo=4",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music114/v4/43/d0/ba/43d0ba6b-6470-ad2d-0c84-171c1daea838/12UMGIM10699.rgb.jpg/100x100bb.jpg",
                artworkUrl60 = "https://is2-ssl.mzstatic.com/image/thumb/Music114/v4/43/d0/ba/43d0ba6b-6470-ad2d-0c84-171c1daea838/12UMGIM10699.rgb.jpg/60x60bb.jpg",
                collectionCensoredName = "Jack Johnson & Friends - Best of Kokua Festival (A Benefit for the Kokua Hawaii Foundation)",
                collectionExplicitness = "notExplicit",
                collectionId = "1440752312",
                collectionName = "Jack Johnson & Friends - Best of Kokua Festival (A Benefit for the Kokua Hawaii Foundation)",
                collectionPrice = 8.99,
                collectionType = "Album",
                collectionViewUrl = "https://music.apple.com/us/album/jack-johnson-friends-best-of-kokua-festival-a/1440752312?uo=4",
                contentAdvisoryRating = null,
                copyright = "This Compilation ℗ 2012 Brushfire Records Inc",
                country = "USA",
                currency = "USD",
                primaryGenreName = "Rock",
                releaseDate = "2012-01-01T08:00:00Z",
                trackCount = 15,
                wrapperType = "collection"
            )
        )
    )

    @RelaxedMockK
    lateinit var service: ITunesApiService

    @RelaxedMockK
    lateinit var bookmarkDao: AlbumBookmarkDao
    lateinit var repository: ITunesRepository

    override fun setup() {
        super.setup()
        repository = ITunesRepositoryImpl(service, bookmarkDao)
    }

    @Test
    fun getITunesResultAsync_success() = runTest {
        coEvery { service.getSearchResponse() } returns iTunesSearchResponse
        val result = repository.getITunesResultAsync(this).await()

        val response = iTunesSearchResponse.results.first()
        val expected = KResult.Success(
            listOf(
                ITunesAlbum(
                    collectionId = response.collectionId,
                    artworkUrl100 = response.artworkUrl100,
                    artistName = response.artistName,
                    collectionName = response.collectionName
                )
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun getITunesResultAsync_fail() = runTest {
        coEvery { service.getSearchResponse() } throws UnknownHostException()
        val result = repository.getITunesResultAsync(this).await()


        val expected = KResult.Fail(ErrorCode.ConnectionError)
        assertEquals(expected, result)
    }


    @Test
    fun addBookmarkAsync() = runTest {
        val collectionId = "5233"

        val result = repository.addBookmarkAsync(this, collectionId).await()
        assertEquals(KResult.Success(Unit), result)

        coVerify {
            bookmarkDao.insert(AlbumBookmark(collectionId))
        }
    }

    @Test
    fun addBookmarkAsync_fail() = runTest {
        coEvery { bookmarkDao.insert(any()) } throws UnknownHostException()
        val collectionId = "155sd"

        val result = repository.addBookmarkAsync(this, collectionId).await()
        assertEquals(KResult.Fail(ErrorCode.ConnectionError), result)

    }

    @Test
    fun deleteBookmarkAsync() = runTest {
        val collectionId = "2341"

        val result = repository.deleteBookmarkAsync(this, collectionId).await()
        assertEquals(KResult.Success(Unit), result)

        coVerify {
            bookmarkDao.delete(AlbumBookmark(collectionId))
        }
    }

    @Test
    fun deleteBookmarkAsync_fail() = runTest {
        coEvery { bookmarkDao.delete(any()) } throws UnknownHostException()
        val collectionId = "5235"

        val result = repository.deleteBookmarkAsync(this, collectionId).await()
        assertEquals(KResult.Fail(ErrorCode.ConnectionError), result)

    }

}