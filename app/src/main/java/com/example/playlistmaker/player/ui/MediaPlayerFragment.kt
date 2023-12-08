package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.models.ClickedTrackGson
import com.example.playlistmaker.player.ui.viewmodel.MediaPlayerViewModel
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.models.PlayListsScreenState
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MediaPlayerFragment : Fragment() {

    private var receivedTrack: String? = null
    private var _binding: ActivityMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.playlistsBottomSheet) }
    private var bottomSheetAdapter: PlayListsPlayerAdapter? = null
    private lateinit var clickedPlayListDebounce: (PlayList) -> Unit

    private val playerVM: MediaPlayerViewModel by viewModel<MediaPlayerViewModel> {
        parametersOf(ClickedTrackGson(arguments?.getString("clickedTrack") ?: ""))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMediaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val radiusIconTrackDp = 8.0f
        val radiusIconTrackPx = dpToPx(radiusIconTrackDp, requireContext())

        bottomSheetAdapter = PlayListsPlayerAdapter { onClickPlayList(it) }
        binding.playlistsRecyclerPlayer.adapter = bottomSheetAdapter

        clickedPlayListDebounce = debounce<PlayList>(
            CLICKED_PLAYLIST_DELAY,
            lifecycleScope,
            false
        ) {
            onClickPlayList(it)
        }
        playerVM.getBootomSheetState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsScreenState.PlayListsContent -> {
                    showBottomSheetContent(it.data)
                }

                is PlayListsScreenState.Empty -> {
                    showBottomSheetEmpty()
                }
            }
        }
        receivedTrack = savedInstanceState?.getString(CLICKED_TRACK, "")
            ?: arguments?.getString("clickedTrack")
        playerVM.getPlayerScreenState().observe(viewLifecycleOwner) { currentPlayerState ->
            when (currentPlayerState.playerState) {
                PlayerState.STATE_DEFAULT -> {
                    with(binding) {
                        playPause.isEnabled = false
                        timerMedia.text = currentPlayerState.currentTime
                    }
                }

                PlayerState.STATE_PREPARED -> {
                    with(binding) {
                        playPause.isEnabled = true
                        playPause.setImageDrawable(requireContext().getDrawable(R.drawable.play_button))
                        timerMedia.text = currentPlayerState.currentTime
                    }
                }

                PlayerState.STATE_PLAYING -> {
                    with(binding) {
                        playPause.setImageDrawable(requireContext().getDrawable(R.drawable.pause_button))
                        timerMedia.text = currentPlayerState.currentTime
                    }

                }

                else -> {
                    with(binding) {
                        timerMedia.text = currentPlayerState.currentTime
                        playPause.setImageDrawable(requireContext().getDrawable(R.drawable.play_button))
                    }
                }

            }
        }

        playerVM.getCurrentTrack().observe(viewLifecycleOwner) { track ->

            if (track.previewUrl == "") {
                binding.playPause.isEnabled = false
                Toast.makeText(
                    requireContext(),
                    getString(R.string.No_media_for_playing),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else binding.playPause.isEnabled = true

            Glide
                .with(this)
                .load(track.coverArtWork)
                .placeholder(R.drawable.placeholder_media_image)
                .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
                .into(binding.trackImageMedia)
            with(binding) {
                addFavorite.isClickable = true
                addCollection.isClickable = true
                trackNameMedia.text = track.trackName
                trackArtistMedia.text = track.artistName
                yearMediaMean.text = track.year
                countryMediaMean.text = track.country
                genreMediaMean.text = track.primaryGenreName
                timeMediaMean.text = track.trackTime
            }

            if (track.collectionName != "") {
                with(binding) {
                    albumMediaMean.isVisible = true
                    albumMedia.isVisible = true
                    albumMediaMean.text = track.collectionName
                }
            } else {
                with(binding) {
                    albumMediaMean.isVisible = false
                    albumMedia.isVisible = false
                }

            }
        }

        binding.addFavorite.setOnClickListener {
            playerVM.changedSingInFavorite()
        }

        playerVM.getCurrentSingFavorite().observe(viewLifecycleOwner) { sing ->
            if (sing) binding.addFavorite.setImageDrawable(requireContext().getDrawable(R.drawable.in_favorite_true))
            else binding.addFavorite.setImageDrawable(requireContext().getDrawable(R.drawable.in_favorite_false))
        }

        //загружаем анимации
        inAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fadein)
        outAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fadeout)
        //добавляем слушателя на анимацию
        outAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        inAnim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                playerVM.playbackControl()
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                        playerVM.getAllPlayLists()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = when (slideOffset) {
                    in 0F..0.4F -> 60F
                    in 0.4F..0.6F -> 70F
                    in 0.6F..1.0F -> 99F
                    else -> 50F
                }
            }

        })
        binding.addCollection.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        binding.backMedia.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.playPause.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
                    binding.playPause.startAnimation(outAnim)
                    return@setOnTouchListener false
                }

                MotionEvent.ACTION_UP -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
                    binding.playPause.startAnimation(inAnim)
                    return@setOnTouchListener false
                }

                else -> {
                    return@setOnTouchListener true
                }
            }
        }

        binding.createAlbumPlayer.setOnClickListener {
            findNavController().navigate(R.id.action_mediaPlayerFragment_to_albumsCreatorFragment)
        }
    }

    private fun showBottomSheetContent(list: List<PlayList>) {
        bottomSheetAdapter?.submitList(list)
    }

    private fun showBottomSheetEmpty() {

    }

    private fun onClickPlayList(playList: PlayList) {

    }


    override fun onResume() {
        super.onResume()
        playerVM.getAllPlayLists()
    }

    override fun onPause() {
        super.onPause()
        playerVM.pausePlayer()
    }

    companion object {
        private val CLICKED_TRACK = "CLICKED_TRACK"
        private const val CURRENTTRACK = "clickedTrack"
        private const val CLICKED_PLAYLIST_DELAY = 300L
        fun createArgs(clickedTrack: String?): Bundle = bundleOf(CURRENTTRACK to clickedTrack)
    }
}
