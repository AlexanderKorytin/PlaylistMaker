package com.example.playlistmaker.library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.ui.fragments.AlbumsFragmentContener
import com.example.playlistmaker.library.ui.fragments.FavoriteTracksFragmentContener

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0 -> FavoriteTracksFragmentContener.newInstance()
            else -> AlbumsFragmentContener.newInstance()
       }
    }

}