package com.example.playlistmaker.settings.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.app.setVibe
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.sharing.ui.fragments.AgreementFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private val settinsVM: SettingsViewModel by viewModel<SettingsViewModel>()
    private var link = ""
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
        settinsVM.seeUserAgreement()
        settinsVM.getCurrentTheme().observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it.isNight
        }
        settinsVM.getAgreementLink().observe(viewLifecycleOwner) {
            link = it
        }
        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            requireContext().setVibe()
            settinsVM.updateNightTheme(checked)
        }

        binding.shareApp.setOnClickListenerWithViber {
            settinsVM.shareApp()
        }

        binding.writeToSupport.setOnClickListenerWithViber {
            settinsVM.writeToSupport()
        }

        binding.userAgreement.setOnClickListenerWithViber {
            findNavController().navigate(
                R.id.action_settingsFragment_to_agreementFragment,
                AgreementFragment.createArgs(link)
            )
        }

    }
}
