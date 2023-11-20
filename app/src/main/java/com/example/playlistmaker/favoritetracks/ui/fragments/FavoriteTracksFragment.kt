package com.example.playlistmaker.favoritetracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TabFavoritesFragmentsBinding
import com.example.playlistmaker.favoritetracks.ui.models.FavoriteTracksScreenState
import com.example.playlistmaker.favoritetracks.ui.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: TabFavoritesFragmentsBinding? = null
    private val binding get() = _binding!!
    private val favoriteTracksVM: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFavoritesFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteTracksVM.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksScreenState.FavoriteTracksLoadind ->{loading()}
                is FavoriteTracksScreenState.FavoriteTracksEmpty ->{showEmpty()}
                is FavoriteTracksScreenState.FavoriteTracksContent ->{}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loading(){
        binding.favoriteTracksList.isVisible = false
        binding.favoriteProgressBar.isVisible = true
        binding.placeholderGroup.isVisible = false
    }

    private fun showEmpty(){
        binding.favoriteTracksList.isVisible = false
        binding.favoriteProgressBar.isVisible = false
        binding.placeholderGroup.isVisible = true
    }

    private fun showContent(tracks: List<Track>){

    }

    companion object {
        fun newInstance(): FavoriteTracksFragment = FavoriteTracksFragment()
    }
}