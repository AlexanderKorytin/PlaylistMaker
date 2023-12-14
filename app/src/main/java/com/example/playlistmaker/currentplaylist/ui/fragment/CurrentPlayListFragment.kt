package com.example.playlistmaker.currentplaylist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import com.example.playlistmaker.databinding.CurrentPlaylistFragmentBinding
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.search.ui.FindAdapter
import org.koin.core.parameter.parametersOf

class CurrentPlayListFragment(): Fragment() {
    private var _binding: CurrentPlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: FindAdapter? = null

    private val currentPlayListVM by ViewModel<CurrentPlayListViewModel>{
        parametersOf(arguments?.getString(CURRENTPLAYLIST)?:"")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val CURRENTPLAYLIST = "current_playlist"
        fun createArgs(currentPlayList: String?): Bundle = bundleOf(CurrentPlayListFragment.CURRENTPLAYLIST to currentPlayList)
    }
}