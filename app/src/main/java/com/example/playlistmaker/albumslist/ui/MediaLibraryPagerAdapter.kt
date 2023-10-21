package com.example.playlistmaker.albumslist.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.albumslist.ui.fragments.AlbumsFragment
import com.example.playlistmaker.favoritetracks.ui.fragments.FavoriteTracksFragment

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> AlbumsFragment.newInstance()
        }
    }

}