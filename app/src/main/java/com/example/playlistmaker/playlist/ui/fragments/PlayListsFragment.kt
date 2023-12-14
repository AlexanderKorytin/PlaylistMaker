package com.example.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.currentplaylist.ui.fragments.CurrentPlayListFragment
import com.example.playlistmaker.databinding.TabAlbumsFragmentBinding
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.PlayListAdapter
import com.example.playlistmaker.playlist.ui.models.PlayListsScreenState
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListsViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {
    private var _binding: TabAlbumsFragmentBinding? = null
    private val binding get() = _binding!!
    private val playListsVM: PlayListsViewModel by viewModel<PlayListsViewModel>()
    private var adapter: PlayListAdapter? = null

    private lateinit var playListClickDebounce: (PlayList) -> Unit

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
        playListClickDebounce = debounce<PlayList>(CLICKDEBOUNCE, lifecycleScope, false) {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_currentPlayListFragment2,
                CurrentPlayListFragment.createArgs(playListsVM.json.toJson(it))
            )
        }
        adapter = PlayListAdapter { playListClickDebounce(it) }
        binding.albumsList.adapter = adapter
        playListsVM.getAllPlayLists()
        playListsVM.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsScreenState.PlayListsContent -> showContent(it.data)
                is PlayListsScreenState.Empty -> showEmpty()
            }
        }
        binding.createAlbum.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_albumsCreatorFragment
            )
        }
    }

    override fun onResume() {
        super.onResume()
        playListsVM.getAllPlayLists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    private fun showContent(list: List<PlayList>) {
        binding.placeholderListAlbums.isVisible = false
        binding.placeholderText.isVisible = false
        binding.albumsList.isVisible = true
        adapter?.submitList(list)
    }

    private fun showEmpty() {
        binding.placeholderListAlbums.isVisible = true
        binding.placeholderText.isVisible = true
        binding.albumsList.isVisible = false
    }

    companion object {
        private const val CLICKDEBOUNCE = 300L
        fun newInstance(): PlayListsFragment = PlayListsFragment()
    }
}