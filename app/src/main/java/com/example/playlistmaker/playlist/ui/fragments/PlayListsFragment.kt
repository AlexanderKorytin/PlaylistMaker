package com.example.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListsViewModel
import com.example.playlistmaker.databinding.TabAlbumsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {
    private var _binding: TabAlbumsFragmentBinding? = null
    private val binding get() = _binding!!
    private val albumListVM: PlayListsViewModel by viewModel<PlayListsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabAlbumsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createAlbum.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_albumsCreatorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlayListsFragment = PlayListsFragment()
    }
}