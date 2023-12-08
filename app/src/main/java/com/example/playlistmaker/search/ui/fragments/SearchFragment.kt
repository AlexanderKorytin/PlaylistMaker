package com.example.playlistmaker.search.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.search.ui.FindAdapter
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var textSearch: String = ""
    private val searchVM: SearchViewModel by viewModel<SearchViewModel>()
    private lateinit var clickedTrackDebounce: (TrackUI) -> Unit
    private var findAdapter: FindAdapter? = null
    private var historyAdapter: FindAdapter? = null
    private var flagNavigateToPlayer = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickedTrackDebounce = debounce<TrackUI>(
            CLICED_TRACK_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            searchVM.savedTrack(track.toTrack())
            val clickedTrack = searchVM.json.toJson(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(clickedTrack)
            )
        }
        // Задаем адаптеры
        findAdapter = FindAdapter {
            flagNavigateToPlayer = true
            clickedTrackDebounce(it)
        }
        historyAdapter = FindAdapter {
            flagNavigateToPlayer = true
            clickedTrackDebounce(it)
        }
        this.binding.tracksList.layoutManager = LinearLayoutManager(requireContext())
        this.binding.tracksList.adapter = findAdapter

        this.binding.historyTracksList.layoutManager = LinearLayoutManager(requireContext())
        this.binding.historyTracksList.adapter = historyAdapter
        this.binding.menuFindSearchEditText.setText(textSearch)

// ------------- подписки----------------------------
        searchVM.getcurrentSearchViewScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is SearchScreenState.Start -> {
                    showStart(findAdapter!!)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Hictory -> {
                    showSearchHistory(true, findAdapter!!, historyAdapter!!, it.tracks)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.EmptyHistory -> {
                    showSearchHistory(false, findAdapter!!, historyAdapter!!, it.tracks)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Empty -> {
                    showEmpty(findAdapter!!)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.IsLoading -> {
                    showLoading(findAdapter!!)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Error -> {
                    showError(findAdapter!!)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Content -> {
                    showContent(findAdapter!!, it.tracks as ArrayList<TrackUI>)
                    showClearIcon(it.isVisibility)
                }
            }
        }


        this.binding.placeholderButton.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            searchVM.getMusic(textSearch)
        }

        this.binding.clearIcon.setOnClickListener {
            this.binding.menuFindSearchEditText.setText("")
        }

        this.binding.menuFindSearchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (textSearch.isNullOrEmpty()) searchVM.showSearchHistory(hasFocus)
        }

        this.binding.clearSearchHistory.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
            searchVM.clearSearchHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var flag =
                    (this@SearchFragment.binding.menuFindSearchEditText.hasFocus() && s?.isEmpty() == true)
                textSearch = s.toString()
                if (s?.isEmpty() == true) {
                    searchVM.showSearchHistory(flag)
                }
                searchVM.searchDebounce(textSearch)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        this.binding.menuFindSearchEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onPause() {
        super.onPause()
        searchVM.stop()
    }

    private fun showStart(adapter: FindAdapter) {
        with(this.binding) {
            clearIcon.isClickable = true
            searchHistoryListView.isVisible = false
            progressBar.isVisible = false
            placeholderFindViewGroup.isVisible = false
            placeholderButton.isVisible = false
            tracksList.isVisible = true
        }

        adapter.submitList(ArrayList())
    }

    private fun showContent(adapter: FindAdapter, tracks: ArrayList<TrackUI>) {
        if (textSearch.isNotEmpty()) {
            with(this.binding) {
                searchHistoryListView.isVisible = false
                clearIcon.isClickable = true
                progressBar.visibility = View.GONE
                placeholderFindViewGroup.visibility = View.GONE
                placeholderButton.visibility = View.GONE
                tracksList.visibility = View.VISIBLE
            }
            adapter.submitList(tracks)
        }
    }

    private fun showLoading(adapter: FindAdapter) {
        with(this.binding) {
            clearIcon.isClickable = false
            searchHistoryListView.isVisible = false
            progressBar.isVisible = true
            placeholderFindViewGroup.isVisible = false
            placeholderButton.isVisible = false
            tracksList.isVisible = true
        }
        adapter.submitList(ArrayList())
    }

    private fun showEmpty(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            with(this.binding) {
                searchHistoryListView.isVisible = false
                clearIcon.isClickable = true
                progressBar.visibility = View.GONE
                placeholderFindViewGroup.visibility = View.VISIBLE
                placeholderButton.visibility = View.GONE
                tracksList.visibility = View.GONE
                placeholderFindText.text = getString(R.string.placeholder_nothing_found_text)
                placeholderFindTint.setImageDrawable(requireContext().getDrawable(R.drawable.nothing_found))
            }
            adapter.submitList(ArrayList())
        }
    }

    private fun showError(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            with(this.binding) {
                searchHistoryListView.isVisible = false
                clearIcon.isClickable = true
                progressBar.visibility = View.GONE
                placeholderFindViewGroup.visibility = View.VISIBLE
                placeholderButton.visibility = View.VISIBLE
                tracksList.visibility = View.GONE
                placeholderFindText.text =
                    getString(R.string.placeholder_communication_problems_text)
                placeholderFindTint.setImageDrawable(requireContext().getDrawable(R.drawable.communication_problem))
            }
            adapter.submitList(ArrayList())
        }
    }

    private fun showClearIcon(value: Boolean) {
        this.binding.clearIcon.isVisible = value
    }

    fun showSearchHistory(
        flag: Boolean,
        findAdapter: FindAdapter,
        historyAdapter: FindAdapter,
        list: List<TrackUI>
    ) {
        if (flag) {
            with(this.binding) {
                progressBar.visibility = View.GONE
                searchHistoryListView.visibility = View.VISIBLE
                tracksList.visibility = View.GONE
                placeholderFindViewGroup.visibility = View.GONE
                placeholderButton.visibility = View.GONE
            }
            findAdapter.submitList(ArrayList())
            historyAdapter.submitList(list)
        } else {
            with(this.binding) {
                progressBar.visibility = View.GONE
                searchHistoryListView.visibility = View.GONE
                tracksList.visibility = View.GONE
                placeholderFindViewGroup.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (flagNavigateToPlayer) {
            this.binding.menuFindSearchEditText.setText(textSearch)
            binding.menuFindSearchEditText.selectAll()
            searchVM.searchDebounce(textSearch)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tracksList.adapter = null
        binding.historyTracksList.adapter = null
        findAdapter = null
        historyAdapter = null
        _binding = null

    }

    companion object {
        private const val CLICED_TRACK_DELAY = 300L
    }
}
//---------------------------------------------------
