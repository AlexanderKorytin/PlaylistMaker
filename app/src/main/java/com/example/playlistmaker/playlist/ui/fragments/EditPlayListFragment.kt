package com.example.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.AlbumCreatorFragmentBinding
import com.example.playlistmaker.playlist.ui.viewmodel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlayListFragment : PlayListCreatorFragment() {

    override var _binding: AlbumCreatorFragmentBinding? = null
    override val binding get() = _binding!!


    override val playListsVM by viewModel<EditPlaylistViewModel> {
        parametersOf(
            arguments?.getInt(
                PLAYLIST_ID
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlbumCreatorFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylist.text = requireContext().getString(R.string.save)
        playListsVM.getPlayList()
        playListsVM.getCurrentScreenState().observe(viewLifecycleOwner) {
            with(binding) {
                namePlaylist.setText(it.playListName)
                descriptionPlaylist.setText(it.playListDescription)

                if (namePlaylist.text.isNotEmpty()) namePlaylist.background =
                    requireContext().getDrawable(R.drawable.playlist_edittext_not_empty)
                else namePlaylist.background =
                    requireContext().getDrawable(R.drawable.playlist_edittext_empty)

                if (descriptionPlaylist.text.isNotEmpty()) descriptionPlaylist.background =
                    requireContext().getDrawable(R.drawable.playlist_edittext_not_empty)
                else descriptionPlaylist.background =
                    requireContext().getDrawable(R.drawable.playlist_edittext_empty)


                val radiusIconTrackDp = 8.0f
                val radiusIconTrackPx = dpToPx(radiusIconTrackDp, requireContext())
                val filePath = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "playlist_maker_album"
                )
                val file = File(filePath, it.playListCover)
                Glide.with(requireContext())
                    .load(file.toUri())
                    .placeholder(R.drawable.new_list_rouded_rectangle)
                    .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
                    .into(binding.listCover)
            }
        }

        binding.createPlaylist.setOnClickListener {
            val album = playListsVM.getPlayListVM()
            album.playListName = binding.namePlaylist.text.toString()
            album.playListDescription = binding.descriptionPlaylist.text.toString()
            if (coverUri != null) album.playListCover = coverUri.toString()
            playListsVM.savePlayList(album)
            findNavController().navigateUp()
        }

        binding.backNewList.setOnClickListener {
            findNavController().navigateUp()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })
    }

    companion object {
        private const val PLAYLIST_ID = "current_playlist_id"
        fun createArgs(currentPlayListId: Int): Bundle =
            bundleOf(PLAYLIST_ID to currentPlayListId)
    }
}