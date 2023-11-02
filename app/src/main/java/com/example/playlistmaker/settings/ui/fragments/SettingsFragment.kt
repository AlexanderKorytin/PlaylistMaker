package com.example.playlistmaker.settings.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
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
            settinsVM.updateNightTheme(checked)
        }

        binding.shareApp.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            settinsVM.shareApp()
        }

        binding.writeToSupport.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            settinsVM.writeToSupport()
        }

        binding.userAgreement.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            findNavController().navigate(
                R.id.action_settingsFragment_to_agreementFragment,
                AgreementFragment.createArgs(link)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
