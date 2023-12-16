package com.example.playlistmaker.currentplaylist.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import com.example.playlistmaker.databinding.CurrentPlaylistFragmentBinding
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.search.ui.FindAdapter
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class CurrentPlayListFragment : Fragment() {
    private var _binding: CurrentPlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private var adapter: FindAdapter? = null

    private lateinit var trackClickDebounce: (TrackUI) -> Unit

    private val currentPlayListVM: CurrentPlayListViewModel by viewModel<CurrentPlayListViewModel> {
        parametersOf(arguments?.getInt(CURRENT_PLAYLIST_ID) ?: "")
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
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.currentPlaylistBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        trackClickDebounce = debounce<TrackUI>(CLICKDEBOUNCE, lifecycleScope, false) {
            findNavController().navigate(
                R.id.action_currentPlayListFragment2_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(currentPlayListVM.json.toJson(it))
            )
        }
        currentPlayListVM.getCurrentPlayListById()
        currentPlayListVM.getPlayList().observe(viewLifecycleOwner) { currentAlbum ->
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_maker_album"
            )
            val file = File(filePath, currentAlbum.playListCover)
            Glide
                .with(this)
                .load(file.toUri())
                .placeholder(R.drawable.placeholder_current_playlist)
                .transform(CenterCrop())
                .into(binding.currentPlaylistCover)

            with(binding) {
                currentPlaylistName.text = currentAlbum.playListName
                quantity.text = currentAlbum.counterTrack.toString()
                if (currentAlbum.playListDescription != "") {
                    currentPlaylistDescr.isVisible = true
                    currentPlaylistDescr.text = currentAlbum.playListDescription
                } else {
                    currentPlaylistDescr.isVisible = false
                }
                point.isVisible = currentAlbum.counterTrack.first() != '0'
            }

        }
        adapter = FindAdapter { trackClickDebounce(it) }

        binding.albumsList.adapter = adapter
        binding.albumsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        currentPlayListVM.getCurrentScreenState().observe(viewLifecycleOwner) { result ->
            when (result) {
                is CurrentPlayListScreenState.Empty -> {
                    showEmpty()
                }

                is CurrentPlayListScreenState.Content -> {
                    showContent(currentPlayListVM.mapper.mapList(result.data), result.time)
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

    private fun showEmpty() {
        with(binding) {
        }
    }

    private fun showContent(listTracks: List<TrackUI>, time: String) {
        with(binding) {
            adapter?.submitList(listTracks)
            albumsList.isVisible = true
            summaryTime.text = time
        }
    }

    companion object {
        private const val CURRENT_PLAYLIST_ID = "current_playlist_id"
        private const val CLICKDEBOUNCE = 300L
        fun createArgs(currentPlayListId: Int): Bundle =
            bundleOf(CURRENT_PLAYLIST_ID to currentPlayListId)
    }
}