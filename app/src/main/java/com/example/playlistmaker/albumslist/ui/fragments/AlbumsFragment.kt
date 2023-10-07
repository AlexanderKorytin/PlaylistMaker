package com.example.playlistmaker.albumslist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TabAlbumsFragmentContenerBinding
import com.example.playlistmaker.albumslist.ui.viewmodel.AlbumsListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment() {
    private var _binding: TabAlbumsFragmentContenerBinding? = null
    private val binding get() = _binding!!
    private val albumListVM: AlbumsListViewModel by viewModel<AlbumsListViewModel>()

    companion object{
        fun newInstance(): AlbumsFragment = AlbumsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TabAlbumsFragmentContenerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPlaceholder()
    }

    private fun showPlaceholder(){
        childFragmentManager.commit {
            add<AlbumsFragmentPlaceholder>(R.id.tabAlbumsFragmentsContener)
        }
    }
}