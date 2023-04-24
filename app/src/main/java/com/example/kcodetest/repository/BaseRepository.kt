package com.example.kcodetest.repository

import androidx.annotation.VisibleForTesting
import com.example.kcodetest.repository.model.ErrorCode
import com.example.kcodetest.repository.model.ErrorMessage
import com.example.kcodetest.repository.model.KResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


abstract class BaseRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    suspend fun <T> executeAsyncCall(
        coroutineScope: CoroutineScope,
        apiCall: suspend () -> T
    ): Deferred<KResult<T>> {
        return coroutineScope.async(Dispatchers.IO, start = CoroutineStart.LAZY) {
            try {
                KResult.Success(apiCall())
            } catch (e: Throwable) {
                handleException(e)
            }
        }
    }

    protected fun handleException(e: Throwable): KResult.Fail {
        return when (e) {
            is HttpException -> {
                val res = e.response()
                KResult.Fail(
                    ErrorCode.HTTPError,
                    ErrorMessage(
                        code = res?.code() ?: DEFAULT_ERROR_MESSAGE_CODE,
                        message = res?.message().orEmpty()
                    )
                )
            }

            is UnknownHostException -> KResult.Fail(ErrorCode.ConnectionError)
            is IOException -> KResult.Fail(ErrorCode.IOError)
            else -> KResult.Fail(ErrorCode.UnknownError)
        }
    }


    companion object {
        const val DEFAULT_ERROR_MESSAGE_CODE = -1
    }
}