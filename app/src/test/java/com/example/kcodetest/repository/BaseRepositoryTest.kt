package com.example.kcodetest.repository

import com.example.kcodetest.BaseUnitTest
import com.example.kcodetest.repository.model.ErrorCode
import com.example.kcodetest.repository.model.ErrorMessage
import com.example.kcodetest.repository.model.KResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class BaseRepositoryTest : BaseUnitTest() {


    val repository = object : BaseRepository() {}

    @Test
    fun executeAsyncCall_success() = runTest {
        val result = repository.executeAsyncCall(this) {
            "abc"
        }.await()

        assertEquals(KResult.Success("abc"), result)
    }

    @Test
    fun executeAsyncCall_httpException() = runTest {
        val e = HttpException(
            Response.error<ResponseBody>(
                500,
                ResponseBody.create("plain/text".toMediaTypeOrNull(), "some content")
            )
        )
        val result = repository.executeAsyncCall(this) {
            throw e
        }.await()

        assertEquals(
            KResult.Fail(
                ErrorCode.HTTPError,
                ErrorMessage(500, e.response()?.message().orEmpty())
            ), result
        )

    }

    @Test
    fun executeAsyncCall_UnknownhostException() = runTest {

        val result = repository.executeAsyncCall(this) {
            throw UnknownHostException()
        }.await()

        assertEquals(KResult.Fail(ErrorCode.ConnectionError), result)
    }

    @Test
    fun executeAsyncCall_IOException() = runTest {
        val result = repository.executeAsyncCall(this) {
            throw IOException()
        }.await()

        assertEquals(KResult.Fail(ErrorCode.IOError), result)

    }

    @Test
    fun executeAsyncCall_Throwable() = runTest {
        val result = repository.executeAsyncCall(this) {
            throw Throwable()
        }.await()

        assertEquals(KResult.Fail(ErrorCode.UnknownError), result)

    }

}