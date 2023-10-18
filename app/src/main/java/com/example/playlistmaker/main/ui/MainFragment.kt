package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.find.setOnClickListenerWithViber {
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        binding.media.setOnClickListenerWithViber {
            findNavController().navigate(R.id.action_mainFragment_to_mediaLibraryFragment)
        }

        binding.settings.setOnClickListenerWithViber {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
}