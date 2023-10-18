package com.example.playlistmaker.search.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.app.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.FindAdapter
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val CLICED_TRACK_DELAY = 1000L
    }

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var isClickTrackAllowed = true
    private var textSearch: String = ""
    private var trackList: ArrayList<TrackUI>? = ArrayList()
    private val searchVM: SearchViewModel by viewModel<SearchViewModel>()
    private  lateinit var navHostFragment:NavHostFragment

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
//        textSearch = savedInstanceState?.getString(SEARCH_QUERY, "") ?: ""
        // Задаем адаптеры
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.root_container) as NavHostFragment
        val findAdapter = FindAdapter {
            clickedTrack(it)
        }
        val historyAdapter = FindAdapter {
            clickedTrack(it)
        }
        this.binding.tracksList.layoutManager = LinearLayoutManager(requireContext())
        this.binding.tracksList.adapter = findAdapter

        this.binding.historyTracksList.layoutManager = LinearLayoutManager(requireContext())
        this.binding.historyTracksList.adapter = historyAdapter
        this.binding.menuFindSearchEditText.setText(textSearch)

        findAdapter.trackList = (trackList as ArrayList<TrackUI>)
        historyAdapter.trackList =
            searchVM.mapToTrackUI.mapList(searchVM.getSearchHstoryTracks())
// ------------- подписки----------------------------
        searchVM.getcurrentSearchViewScreenState().observe(viewLifecycleOwner) {
            when (it) {
                is SearchScreenState.Start -> {
                    showStart(findAdapter)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Hictory -> {
                    showSearchHistory(true, findAdapter, historyAdapter, it.tracks)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.EmptyHistory -> {
                    showSearchHistory(false, findAdapter, historyAdapter, it.tracks)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Empty -> {
                    showEmpty(findAdapter)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.IsLoading -> {
                    showLoading(findAdapter)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Error -> {
                    showError(findAdapter)
                    showClearIcon(it.isVisibility)
                }

                is SearchScreenState.Content -> {
                    showContent(findAdapter, it.tracks as ArrayList<TrackUI>)
                    showClearIcon(it.isVisibility)
                }
            }
        }


        this.binding.placeholderButton.setOnClickListenerWithViber {
            searchVM.getMusic(textSearch)
        }

        this.binding.clearIcon.setOnClickListener {
            this.binding.menuFindSearchEditText.setText("")
        }

        this.binding.backFind.setOnClickListenerWithViber {
            findNavController().navigateUp()
        }

        this.binding.menuFindSearchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (textSearch.isNullOrEmpty()) searchVM.showSearchHistory(hasFocus)
        }

        this.binding.clearSearchHistory.setOnClickListenerWithViber {
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

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(SEARCH_QUERY, textSearch)
//    }

    private fun showStart(adapter: FindAdapter) {
        this.binding.clearIcon.isClickable = false
        this.binding.searchHistoryListView.isVisible = false
        this.binding.progressBar.isVisible = false
        this.binding.clearIcon.isClickable = false
        this.binding.placeholderFindViewGroup.isVisible = false
        this.binding.placeholderButton.isVisible = false
        this.binding.tracksList.isVisible = true
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()

    }

    private fun showContent(adapter: FindAdapter, tracks: ArrayList<TrackUI>) {
        if (textSearch.isNotEmpty()) {
            this.binding.searchHistoryListView.isVisible = false
            this.binding.clearIcon.isClickable = true
            this.binding.progressBar.visibility = View.GONE
            this.binding.placeholderFindViewGroup.visibility = View.GONE
            this.binding.placeholderButton.visibility = View.GONE
            this.binding.tracksList.visibility = View.VISIBLE
            adapter.trackList = tracks
            adapter.notifyDataSetChanged()
        }
    }

    private fun showLoading(adapter: FindAdapter) {
        this.binding.clearIcon.isClickable = false
        this.binding.searchHistoryListView.isVisible = false
        this.binding.progressBar.isVisible = true
        this.binding.clearIcon.isClickable = false
        this.binding.placeholderFindViewGroup.isVisible = false
        this.binding.placeholderButton.isVisible = false
        this.binding.tracksList.isVisible = true
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            this.binding.searchHistoryListView.isVisible = false
            this.binding.clearIcon.isClickable = true
            this.binding.progressBar.visibility = View.GONE
            this.binding.placeholderFindViewGroup.visibility = View.VISIBLE
            this.binding.placeholderButton.visibility = View.GONE
            this.binding.tracksList.visibility = View.GONE
            this.binding.placeholderFindText.text =
                getString(R.string.placeholder_nothing_found_text)
            this.binding.placeholderFindTint.setImageDrawable(requireContext().getDrawable(R.drawable.nothing_found))
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun showError(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            this.binding.searchHistoryListView.isVisible = false
            this.binding.clearIcon.isClickable = true
            this.binding.progressBar.visibility = View.GONE
            this.binding.placeholderFindViewGroup.visibility = View.VISIBLE
            this.binding.placeholderButton.visibility = View.VISIBLE
            this.binding.tracksList.visibility = View.GONE
            this.binding.placeholderFindText.text =
                getString(R.string.placeholder_communication_problems_text)
            this.binding.placeholderFindTint.setImageDrawable(requireContext().getDrawable(R.drawable.communication_problem))
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun showClearIcon(value: Boolean) {
        this.binding.clearIcon.isVisible = value
    }


    private fun clickedTrack(track: TrackUI) {
        if (trackClickedDebounce()) {
            searchVM.savedTrack(track.toTrack())
            val navController = navHostFragment.navController
            navController.navigate(
                R.id.action_searchFragment_to_mediaActivity,
                MediaActivity.createArgs(searchVM.json.toJson(track))
            )
        }
    }

    private fun trackClickedDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            searchVM.handlerMain.postDelayed({ isClickTrackAllowed = true }, CLICED_TRACK_DELAY)
        }
        return current
    }

    fun showSearchHistory(
        flag: Boolean,
        findAdapter: FindAdapter,
        historyAdapter: FindAdapter,
        list: List<TrackUI>
    ) {
        if (flag) {
            this.binding.progressBar.visibility = View.GONE
            this.binding.searchHistoryListView.visibility = View.VISIBLE
            this.binding.tracksList.visibility = View.GONE
            this.binding.placeholderFindViewGroup.visibility = View.GONE
            this.binding.placeholderButton.visibility = View.GONE
            findAdapter.trackList.clear()
            findAdapter.notifyDataSetChanged()
            historyAdapter.trackList = list as ArrayList<TrackUI>
            historyAdapter.notifyDataSetChanged()
        } else {
            this.binding.progressBar.visibility = View.GONE
            this.binding.searchHistoryListView.visibility = View.GONE
            this.binding.tracksList.visibility = View.GONE
            this.binding.placeholderFindViewGroup.visibility = View.GONE
        }
    }
}
//---------------------------------------------------
