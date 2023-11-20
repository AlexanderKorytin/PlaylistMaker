package com.example.playlistmaker.favoritetracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TabFavoritesFragmentsBinding
import com.example.playlistmaker.favoritetracks.ui.models.FavoriteTracksScreenState
import com.example.playlistmaker.favoritetracks.ui.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.FindAdapter
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: TabFavoritesFragmentsBinding? = null
    private val binding get() = _binding!!

    private val favoriteTracksVM: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var clickedTrackDebounce: (TrackUI) -> Unit

    private var favoriteAdapter: FindAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFavoritesFragmentsBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickedTrackDebounce = debounce<TrackUI>(
            CLICED_TRACK_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_mediaActivity,
                MediaActivity.createArgs(favoriteTracksVM.json.toJson(track))
            )
        }

        favoriteAdapter = FindAdapter {
            clickedTrackDebounce(it)
        }

        binding.favoriteTracksList.adapter = favoriteAdapter
        binding.favoriteTracksList.layoutManager = LinearLayoutManager(requireContext())
        favoriteTracksVM.updateFavoriteTracks()
        favoriteTracksVM.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksScreenState.FavoriteTracksEmpty -> {
                    showEmpty()
                }

                is FavoriteTracksScreenState.FavoriteTracksContent -> {
                    showContent(it.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksVM.updateFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.favoriteTracksList.adapter = null
        _binding = null
        favoriteAdapter = null
    }


    private fun showEmpty() {
        binding.favoriteTracksList.isVisible = false
        binding.placeholderGroup.isVisible = true
    }

    private fun showContent(tracks: List<Track>) {
        favoriteAdapter?.trackList?.clear()
        favoriteAdapter?.trackList?.addAll(favoriteTracksVM.mapToTrackUI.mapList(tracks))
        binding.favoriteTracksList.adapter?.notifyDataSetChanged()
        binding.favoriteTracksList.isVisible = true
        binding.placeholderGroup.isVisible = false
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment = FavoriteTracksFragment()
        private const val CLICED_TRACK_DELAY = 300L
    }


}