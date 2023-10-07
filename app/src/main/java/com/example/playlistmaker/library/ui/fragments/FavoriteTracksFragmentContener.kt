package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TabFavoritesFragmentsContenerBinding
import com.example.playlistmaker.library.ui.viewmodels.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragmentContener : Fragment() {
    private var _binding: TabFavoritesFragmentsContenerBinding? = null
    private val binding get() = _binding!!
    private val favoriteTracksVM: FavoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    companion object {
        fun newInstance(): FavoriteTracksFragmentContener = FavoriteTracksFragmentContener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFavoritesFragmentsContenerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPlaceholder()
    }

    private fun showPlaceholder() {
        childFragmentManager.commit {
            add<FavoriteTracksFragmentPlaceholder>(R.id.tabFavoriteTracksFragmentsContener)
        }
    }
}