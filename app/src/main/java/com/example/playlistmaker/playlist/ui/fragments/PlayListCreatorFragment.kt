package com.example.playlistmaker.playlist.ui.fragments

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.databinding.AlbumCreatorFragmentBinding
import com.example.playlistmaker.playlist.domain.models.PlayList
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlayListCreatorFragment : Fragment() {
    private var _binding: AlbumCreatorFragmentBinding? = null
    private val binding get() = _binding!!
    private var coverUri: String? = null
    private val playListsVM by viewModel<PlayListsViewModel>()
    private val requester = PermissionRequester.instance()

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val radiusIconTrackDp = 8.0f
                val radiusIconTrackPx = dpToPx(radiusIconTrackDp, requireContext())
                Glide.with(requireContext())
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(radiusIconTrackPx))
                    .into(binding.listCover)
                saveImageToPrivateStorage(uri)
            }
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
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(requireContext().getString(R.string.create_playlist_dialog_title))
            .setMessage(requireContext().getString(R.string.create_playlist_dialog_message))
            .setNegativeButton(requireContext().getString(R.string.canсel)) { dialog, which ->

            }
            .setPositiveButton(requireContext().getString(R.string.сomplete)) { dialog, which ->
                findNavController().navigateUp()
            }

        val playListNameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                with(binding) {
                    if (s?.isNotEmpty()!!) {
                        namePlaylist.background =
                            requireContext().getDrawable(R.drawable.playlist_edittext_not_empty)
                        textInFrameHintName.isVisible = true
                        createPlaylist.isEnabled = true
                    } else {
                        namePlaylist.background =
                            requireContext().getDrawable(R.drawable.playlist_edittext_empty)
                        textInFrameHintName.isVisible = false
                        createPlaylist.isEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        binding.namePlaylist.addTextChangedListener(playListNameTextWatcher)

        val playListDescriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                with(binding) {
                    if (s?.isNotEmpty()!!) {
                        descriptionPlaylist.background =
                            requireContext().getDrawable(R.drawable.playlist_edittext_not_empty)
                        textInFrameHintDescription.isVisible = true
                    } else {
                        descriptionPlaylist.background =
                            requireContext().getDrawable(R.drawable.playlist_edittext_empty)
                        textInFrameHintDescription.isVisible = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        binding.descriptionPlaylist.addTextChangedListener(playListDescriptionTextWatcher)

        binding.listCover.setOnClickListener {
            lifecycleScope.launch {
                chekedPermission()
            }
        }
        binding.createPlaylist.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.namePlaylist.text} создан",
                Toast.LENGTH_LONG
            ).show()

            playListsVM.savePlayList(
                playList = PlayList(
                    playListName = binding.namePlaylist.text.toString(),
                    playListCover = coverUri ?: "",
                    playListDescription = binding.descriptionPlaylist.text.toString(),
                    tracksIds = ""
                )
            )
            findNavController().navigateUp()
        }

        binding.backNewList.setOnClickListener {
            if (binding.namePlaylist.text.isNotEmpty()
                || binding.descriptionPlaylist.text.isNotEmpty()
                || coverUri != null
            ) dialog.show() else findNavController().navigateUp()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.namePlaylist.text.isNotEmpty()
                        || binding.descriptionPlaylist.text.isNotEmpty()
                        || coverUri != null
                    ) dialog.show() else findNavController().navigateUp()
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun chekedPermission() {
        requester.request(Manifest.permission.CAMERA).collect { result ->
            when (result) {
                is PermissionResult.Granted -> {
                    withContext(Dispatchers.IO) {
                        pickMedia
                            .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }// Пользователь дал разрешение, можно продолжать работу
                is PermissionResult.Denied.NeedsRationale -> {}// Необходимо показать разрешение
                is PermissionResult.Denied.DeniedPermanently -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.data = Uri.fromParts("package", requireContext().packageName, null)
                    requireContext().startActivity(intent)
                } // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
                is PermissionResult.Cancelled -> {
                    return@collect
                } // Запрос на разрешение отменён
            }
        }
    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_maker_album"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        coverUri = "${uri.lastPathSegment}.jpg"
        val file = File(filePath, coverUri ?: "")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}