package com.example.kcodetest.view.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kcodetest.R
import com.example.kcodetest.databinding.FragmentBookmarkBinding
import com.example.kcodetest.util.launchAndRepeatWithViewLifecycle
import com.example.kcodetest.view.common.AlbumRecyclerViewAdapter
import com.example.kcodetest.viewmodel.album.AlbumViewModel
import kotlinx.coroutines.launch


class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: AlbumViewModel by navGraphViewModels(R.id.main_nav_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val adapter = AlbumRecyclerViewAdapter(viewModel::onBookmarkClicked)
            bookmarkFragmentRecyclerView.apply {
                layoutManager = GridLayoutManager(context, NUM_COLUMN_COUNT)
                this.adapter = adapter
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            bookmarkFragmentPullToRefresh.setOnRefreshListener {
                viewModel.loadNewAlbumList()
            }

            launchAndRepeatWithViewLifecycle {
                launch {
                    viewModel.bookmarkedAlbumListUI.collect {
                        adapter.uiList = it
                    }
                }
                launch {
                    viewModel.isLoading.collect {
                        bookmarkFragmentPullToRefresh.isRefreshing = it
                    }
                }
            }
        }
    }

    companion object {
        const val NUM_COLUMN_COUNT = 3
    }
}