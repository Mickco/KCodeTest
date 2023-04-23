package com.example.kcodetest.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kcodetest.R
import com.example.kcodetest.databinding.FragmentHomeBinding
import com.example.kcodetest.util.launchAndRepeatWithViewLifecycle
import com.example.kcodetest.view.common.AlbumRecyclerViewAdapter
import com.example.kcodetest.viewmodel.album.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AlbumViewModel by navGraphViewModels(R.id.main_nav_graph) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initViewMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val adapter = AlbumRecyclerViewAdapter(viewModel::onBookmarkClicked)
            homeFragmentRecyclerView.apply {
                layoutManager = GridLayoutManager(context, NUM_COLUMN_COUNT)
                this.adapter = adapter
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }


            homeFragmentPullToRefresh.setOnRefreshListener {
                viewModel.loadNewAlbumList()
            }

            launchAndRepeatWithViewLifecycle {
                launch {
                    viewModel.albumListUI.collect {
                        adapter.uiList = it
                    }
                }
                launch {
                    viewModel.isLoading.collect {
                        homeFragmentPullToRefresh.isRefreshing = it
                    }
                }
            }
        }
    }

    companion object {
        const val NUM_COLUMN_COUNT = 3
    }
}