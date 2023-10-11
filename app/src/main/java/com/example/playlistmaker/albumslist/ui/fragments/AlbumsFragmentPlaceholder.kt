package com.example.playlistmaker.albumslist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.EmptyListAlbumsBinding

class AlbumsFragmentPlaceholder(): Fragment() {
    private var _binding: EmptyListAlbumsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EmptyListAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }
}