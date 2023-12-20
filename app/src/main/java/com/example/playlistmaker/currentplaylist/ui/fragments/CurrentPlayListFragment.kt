package com.example.playlistmaker.currentplaylist.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.currentplaylist.ui.CurrentPlayListAdapter
import com.example.playlistmaker.currentplaylist.ui.models.CurrentPlayListScreenState
import com.example.playlistmaker.currentplaylist.ui.viewmodel.CurrentPlayListViewModel
import com.example.playlistmaker.databinding.CurrentPlaylistFragmentBinding
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.playlist.ui.fragments.EditPlayListFragment
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class CurrentPlayListFragment : Fragment() {
    private var _binding: CurrentPlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private var adapter: CurrentPlayListAdapter? = null

    private lateinit var trackClickDebounce: (TrackUI) -> Unit

    private val currentPlayListVM: CurrentPlayListViewModel by viewModel<CurrentPlayListViewModel> {
        parametersOf(arguments?.getInt(CURRENT_PLAYLIST_ID) ?: "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackListBottomSheetBehavior =
            BottomSheetBehavior.from(binding.currentPlaylistBottomSheet)
        trackListBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val settingBottomSheetBehaivor =
            BottomSheetBehavior.from(binding.currentPlayListSettingBottomSheet)

        settingBottomSheetBehaivor.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        settingBottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
        trackClickDebounce = debounce<TrackUI>(CLICKDEBOUNCE, lifecycleScope, false) {
            findNavController().navigate(
                R.id.action_currentPlayListFragment2_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(currentPlayListVM.json.toJson(it))
            )
        }
        currentPlayListVM.getCurrentPlayListById()

        currentPlayListVM.getPlayListImmutableState().observe(viewLifecycleOwner) { currentAlbum ->
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_maker_album"
            )
            val file = File(filePath, currentAlbum.playListCover)
            Glide
                .with(this)
                .load(file.toUri())
                .placeholder(R.drawable.placeholder_current_playlist)
                .transform(CenterCrop())
                .into(binding.currentPlaylistCover)

            with(binding) {
                currentPlaylistName.text = currentAlbum.playListName
                quantity.text = currentAlbum.counterTrack
                if (currentAlbum.playListDescription != "") {
                    currentPlaylistDescr.isVisible = true
                    currentPlaylistDescr.text = currentAlbum.playListDescription
                } else {
                    currentPlaylistDescr.isVisible = false
                }
            }

        }
        adapter = CurrentPlayListAdapter(::onClick, ::onLongClick)

        binding.albumsList.adapter = adapter
        binding.albumsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        currentPlayListVM.getPlayListMutableState().observe(viewLifecycleOwner) { result ->
            when (result) {
                is CurrentPlayListScreenState.Empty -> {
                    showEmpty(result.time)
                }

                is CurrentPlayListScreenState.Content -> {
                    showContent(currentPlayListVM.mapper.mapList(result.data), result.time)
                }
            }
        }

        binding.playlistSharing.setOnClickListener {
            binding.overlay.isVisible = true
            if (adapter?.currentList?.isEmpty() == true) {
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                    .setMessage(R.string.no_tracks_for_sharing)
                    .setCancelable(false)
                    .setPositiveButton(R.string.close) { dialog, which ->
                        binding.overlay.isVisible = false
                    }.show()
            } else {
                currentPlayListVM.sharingPlayList()
            }
        }

        binding.playlistSetting.setOnClickListener {
            settingBottomSheetBehaivor.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.sharing.setOnClickListener {
            currentPlayListVM.sharingPlayList()
            settingBottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.deletePlaylist.setOnClickListener {
            settingBottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setMessage("${requireContext().getString(R.string.playlist_del_alert_dialog_message)} «${binding.currentPlaylistName.text}»?")
                .setCancelable(false)
                .setNegativeButton(R.string.no) { dialog, which ->
                    binding.overlay.isVisible = false
                }
                .setPositiveButton(R.string.yes) { doalog, which ->
                    currentPlayListVM.deletePlayList()
                    binding.overlay.isVisible = false
                    findNavController().navigateUp()
                }.show()
        }

        binding.editInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_currentPlayListFragment2_to_editPlayListFragment,
                EditPlayListFragment.createArgs(
                    currentPlayListVM.getPlayListId()
                )
            )
        }

        binding.backCurrentPlaylist.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onLongClick(trackUI: TrackUI) {
        binding.overlay.isVisible = true
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(R.string.track_delete_dialog_title)
            .setCancelable(false)
            .setNegativeButton(R.string.no) { dialog, which ->
                binding.overlay.isVisible = false
            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                binding.overlay.isVisible = false
                currentPlayListVM.deleteTrack(trackUI.toTrack())
            }
        dialog.show()
    }

    private fun onClick(trackUI: TrackUI) {
        trackClickDebounce(trackUI)
    }

    private fun showEmpty(time: String) {
        with(binding) {
            albumsList.isVisible = false
            playlistIsEmpty.isVisible = true
            summaryTime.text = time
            adapter?.submitList(listOf())
        }
    }

    private fun showContent(listTracks: List<TrackUI>, time: String) {
        with(binding) {
            adapter?.submitList(listTracks)
            albumsList.isVisible = true
            playlistIsEmpty.isVisible = false
            summaryTime.text = time
        }
    }

    companion object {
        private const val CURRENT_PLAYLIST_ID = "current_playlist_id"
        private const val CLICKDEBOUNCE = 300L
        fun createArgs(currentPlayListId: Int): Bundle =
            bundleOf(CURRENT_PLAYLIST_ID to currentPlayListId)
    }
}