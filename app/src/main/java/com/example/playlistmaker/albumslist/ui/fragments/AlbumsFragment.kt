package com.example.playlistmaker.albumslist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.albumslist.ui.viewmodel.AlbumsListViewModel
import com.example.playlistmaker.databinding.TabAlbumsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment() {
    private var _binding: TabAlbumsFragmentBinding? = null
    private val binding get() = _binding!!
    private val albumListVM: AlbumsListViewModel by viewModel<AlbumsListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabAlbumsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): AlbumsFragment = AlbumsFragment()
    }
}