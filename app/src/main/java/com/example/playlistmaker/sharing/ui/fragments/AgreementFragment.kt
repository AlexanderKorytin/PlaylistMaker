package com.example.playlistmaker.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.AgreementFragmentBinding

class AgreementFragment : Fragment() {
    private var _binding: AgreementFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AgreementFragmentBinding.inflate(inflater, container, false)
        binding.agreementWeb.loadUrl(requireArguments().getString(AGREEMENT) ?: "")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backAgreement.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val AGREEMENT = "agreements"
        fun createArgs(url: String): Bundle = bundleOf(AGREEMENT to url)
    }
}