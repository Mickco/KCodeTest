package com.example.kcodetest.repository

import com.example.kcodetest.repository.model.ErrorCode
import com.example.kcodetest.repository.model.ErrorMessage
import com.example.kcodetest.repository.model.RepositoryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


abstract class BaseRepository {

    suspend fun <T> executeAsyncCall(
        coroutineScope: CoroutineScope,
        apiCall: suspend () -> T
    ): Deferred<RepositoryResult<T>> {
        return coroutineScope.async(Dispatchers.IO, start = CoroutineStart.LAZY) {
            try {
                RepositoryResult.Success(apiCall())
            } catch (e: Throwable) {
                handleException(e)
            }
        }
    }

    protected fun handleException(e: Throwable): RepositoryResult.Fail {
        return when (e) {
            is HttpException -> {
                val res = e.response()
                RepositoryResult.Fail(
                    ErrorCode.HTTPError,
                    ErrorMessage(
                        code = res?.code() ?: DEFAULT_ERROR_MESSAGE_CODE,
                        message = res?.message().orEmpty()
                    )
                )
            }

            is UnknownHostException -> RepositoryResult.Fail(ErrorCode.ConnectionError)
            is IOException -> RepositoryResult.Fail(ErrorCode.IOError)
            else -> RepositoryResult.Fail(ErrorCode.UnknownError)
        }
    }


    companion object {
        const val DEFAULT_ERROR_MESSAGE_CODE = -1
    }
}