package com.example.kcodetest.viewmodel.album

import androidx.lifecycle.viewModelScope
import com.example.kcodetest.R
import com.example.kcodetest.repository.itunes.ITunesRepository
import com.example.kcodetest.repository.model.KResult
import com.example.kcodetest.viewmodel.common.BaseViewModel
import com.example.kcodetest.viewmodel.common.FormatWrap
import com.example.kcodetest.viewmodel.common.ResourceWrap
import com.example.kcodetest.viewmodel.common.StringWrap
import com.example.kcodetest.viewmodel.common.TextWrap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val iTunesRepository: ITunesRepository,
    @Named("errorDisplayRequest") private val _errorDisplayRequest: MutableSharedFlow<KResult.Fail>
) : BaseViewModel() {


    // For internal
    private val _isLoading = MutableStateFlow(false)
    private val _albumListUI = MutableStateFlow<List<AlbumListItemUI>>(listOf())
    private val _bookmarkedAlbumListUI = MutableStateFlow<List<AlbumListItemUI>>(listOf())
    private val _bookmarkAlbumList: StateFlow<List<String>> = iTunesRepository.bookmarkListFlow
        .filterIsInstance<KResult.Success<List<String>>>()
        .map { it.data }
        .onEach { list ->
            _albumListUI.value = _albumListUI.value.map {
                it.copy(isBookmarked = it.collectionId in list)
            }
            _bookmarkedAlbumListUI.value = _albumListUI.value.filter { it.isBookmarked }
        }.stateIn(viewModelScope, started = SharingStarted.Eagerly, listOf())

    // For external
    val isLoading: StateFlow<Boolean> = _isLoading
    val albumListUI: StateFlow<List<AlbumListItemUI>> = _albumListUI
    val bookmarkedAlbumListUI: StateFlow<List<AlbumListItemUI>> = _bookmarkedAlbumListUI

    fun initViewMode() {
        loadNewAlbumList()
    }

    fun loadNewAlbumList() {
        launchLoadingScope {
            val result = iTunesRepository.getITunesResultAsync(this).await()

            when (result) {
                is KResult.Success -> {
                    val albumListUI = result.data.map {
                        AlbumListItemUI(
                            collectionId = it.collectionId,
                            isBookmarked = it.collectionId in _bookmarkAlbumList.value,
                            imageUrl = StringWrap(it.artworkUrl100),
                            albumText = FormatWrap(
                                ResourceWrap(R.string.album_item_title_format),
                                StringWrap(it.artistName),
                                StringWrap(it.collectionName)
                            )
                        )
                    }
                    _albumListUI.value = albumListUI
                    _bookmarkedAlbumListUI.value = albumListUI.filter { it.isBookmarked }
                }

                is KResult.Fail -> {
                    _errorDisplayRequest.emit(result)
                }
            }
        }
    }

    // Add/Remove bookmark when user clicked bookmark icon
    fun onBookmarkClicked(item: AlbumListItemUI) {
        launchLoadingScope {
            when (item.isBookmarked) {
                true -> iTunesRepository.deleteBookmarkAsync(
                    coroutineScope = viewModelScope,
                    collectionId = item.collectionId
                ).await()

                false -> iTunesRepository.addBookmarkAsync(
                    coroutineScope = viewModelScope,
                    collectionId = item.collectionId
                ).await()
            }
        }

    }

    override fun onLoadingCountChanged() {
        _isLoading.value = loadingCount.get() != 0
    }
}

data class AlbumListItemUI(
    val collectionId: String,
    val imageUrl: TextWrap,
    val albumText: TextWrap,
    val isBookmarked: Boolean
)