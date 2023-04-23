package com.example.kcodetest.repository.model

sealed class KResult<out DATA> {
    data class Success<out DATA>(val data: DATA) : KResult<DATA>()
    data class Fail(val errorCode: ErrorCode, val errorMessage: ErrorMessage? = null) :
        KResult<Nothing>()
}

data class ErrorMessage(
    val code: Int,
    val message: String
)