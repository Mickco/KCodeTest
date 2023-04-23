package com.example.kcodetest.viewmodel.main

import com.example.kcodetest.R
import com.example.kcodetest.repository.model.ErrorCode
import com.example.kcodetest.repository.model.KResult
import com.example.kcodetest.viewmodel.common.BaseViewModel
import com.example.kcodetest.viewmodel.common.ResourceWrap
import com.example.kcodetest.viewmodel.common.TextWrap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

// This class process errors and display default error message if requested
@HiltViewModel
class ErrorViewModel @Inject constructor(
    @Named("errorDisplayRequest") private val _errorDisplayRequest: MutableSharedFlow<KResult.Fail>
) : BaseViewModel() {


    val displayErrorSnackbar: Flow<KSnackBarUI> = _errorDisplayRequest.map {
        when (it.errorCode) {
            ErrorCode.ConnectionError,
            ErrorCode.HTTPError -> KSnackBarUI(
                message = ResourceWrap(R.string.network_error_message),
                duration = Snackbar.LENGTH_LONG
            )

            else -> KSnackBarUI(
                message = ResourceWrap(R.string.unknown_error_message),
                duration = Snackbar.LENGTH_LONG
            )
        }
    }
}

data class KSnackBarUI(
    val message: TextWrap,
    val duration: Int
)