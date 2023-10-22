package com.example.playlistmaker.favoritetracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TabFavoritesFragmentsBinding
import com.example.playlistmaker.favoritetracks.ui.viewmodel.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: TabFavoritesFragmentsBinding? = null
    private val binding get() = _binding!!
    private val favoriteTracksVM: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    companion object {
        fun newInstance(): FavoriteTracksFragment = FavoriteTracksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFavoritesFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }


}