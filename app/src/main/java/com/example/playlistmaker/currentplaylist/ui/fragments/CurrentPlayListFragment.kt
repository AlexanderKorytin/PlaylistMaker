package com.example.playlistmaker.currentplaylist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import com.example.playlistmaker.databinding.CurrentPlaylistFragmentBinding
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.search.ui.FindAdapter
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CurrentPlayListFragment : Fragment() {
    private var _binding: CurrentPlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private var adapter: FindAdapter? = null

    private lateinit var trackClickDebounce: (TrackUI) -> Unit

    private val currentPlayListVM: CurrentPlayListViewModel by viewModel<CurrentPlayListViewModel> {
        parametersOf(arguments?.getString(CURRENTPLAYLIST) ?: "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackClickDebounce = debounce<TrackUI>(CLICKDEBOUNCE, lifecycleScope, false) {
            findNavController().navigate(
                R.id.action_currentPlayListFragment2_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(currentPlayListVM.json.toJson(it))
            )
        }
        val currentPL = currentPlayListVM.json.fromJson(
            arguments?.getString(CURRENTPLAYLIST),
            PlayList::class.java
        )
        binding.currentPlaylistName.text = currentPL.playListName
        adapter = FindAdapter { trackClickDebounce(it) }

        binding.albumsList.adapter = adapter
        binding.albumsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        currentPlayListVM.getTracks()
        currentPlayListVM.getCurrentScreenState().observe(viewLifecycleOwner) { result ->
            when (result) {
                is CurrentPlayListScreenState.Empty -> {
                    showEmpty(result.playListName)
                }

                is CurrentPlayListScreenState.Content -> {
                    showContent(result.playListName, currentPlayListVM.mapper.mapList(result.data))
                }
            }
        }

        binding.backCurrentPlaylist.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEmpty(name: String) {
        with(binding) {
            albumsList.isVisible = false
            placeholderListAlbums.isVisible = true
            placeholderText.isVisible = true
        }
    }

    private fun showContent(name: String, listTracks: List<TrackUI>) {
        with(binding) {
            adapter?.submitList(listTracks)
            albumsList.isVisible = true
            placeholderListAlbums.isVisible = false
            placeholderText.isVisible = false
        }
    }

    companion object {
        private const val CURRENTPLAYLIST = "current_playlist"
        private const val CLICKDEBOUNCE = 300L
        fun createArgs(currentPlayList: String?): Bundle =
            bundleOf(CURRENTPLAYLIST to currentPlayList)
    }
}