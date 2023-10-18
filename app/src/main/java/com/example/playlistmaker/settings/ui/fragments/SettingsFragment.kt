package com.example.playlistmaker.settings.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.app.setVibe
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private val SettinsVM: SettingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettinsVM.getCurrentTheme().observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it.isNight
        }

        binding.backSetting.setOnClickListenerWithViber {
            findNavController().navigateUp()
        }

        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            requireContext().setVibe()
            SettinsVM.updateNightTheme(checked)
        }

        binding.shareApp.setOnClickListenerWithViber {
            SettinsVM.shareApp()
        }

        binding.writeToSupport.setOnClickListenerWithViber {
            SettinsVM.writeToSupport()
        }

        binding.userAgreement.setOnClickListenerWithViber {
            SettinsVM.seeUserAgreement()
        }

    }
}
