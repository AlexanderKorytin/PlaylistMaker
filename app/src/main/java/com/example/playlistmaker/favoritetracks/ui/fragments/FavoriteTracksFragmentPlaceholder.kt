package com.example.playlistmaker.favoritetracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyListFavoritTrackBinding

class FavoriteTracksFragmentPlaceholder(): Fragment() {
    private var _binding: EmptyListFavoritTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EmptyListFavoritTrackBinding.inflate(inflater, container, false)
        return binding.root
    }
}