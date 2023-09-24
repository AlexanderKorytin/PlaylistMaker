package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.util.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val CLICED_TRACK_DELAY = 1000L
    }

    private lateinit var bindingFindActivity: ActivityFindBinding
    private var isClickTrackAllowed = true
    private var textSearch: String = ""
    private var trackList: ArrayList<TrackUI>? = ArrayList()
    private val searchVM: SearchViewModel by viewModel<SearchViewModel>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, textSearch)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textSearch = savedInstanceState?.getString(SEARCH_QUERY, "") ?: ""
        bindingFindActivity = ActivityFindBinding.inflate(layoutInflater)
        val viewFind = bindingFindActivity.root
        setContentView(viewFind)
        // Задаем адаптеры
        val findAdapter = FindAdapter {
            clickedTrack(it)
        }
        val historyAdapter = FindAdapter {
            clickedTrack(it)
        }
        bindingFindActivity.tracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.tracksList.adapter = findAdapter

        bindingFindActivity.historyTracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.historyTracksList.adapter = historyAdapter
        bindingFindActivity.menuFindSearchEditText.setText(textSearch)

        findAdapter.trackList = (trackList as ArrayList<TrackUI>)
        historyAdapter.trackList =
            searchVM.mapToTrackUI.mapList(searchVM.getSearchHstoryTracks())
// ------------- подписки----------------------------
        searchVM.getcurrentSearchViewScreenState().observe(this) {
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


        bindingFindActivity.placeholderButton.setOnClickListenerWithViber {
            searchVM.getMusic(textSearch)
        }

        bindingFindActivity.clearIcon.setOnClickListener {
            bindingFindActivity.menuFindSearchEditText.setText("")
        }

        bindingFindActivity.backFind.setOnClickListenerWithViber {
            finish()
        }

        bindingFindActivity.menuFindSearchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (textSearch.isNullOrEmpty()) searchVM.showSearchHistory(hasFocus)
        }

        bindingFindActivity.clearSearchHistory.setOnClickListenerWithViber {
            searchVM.clearSearchHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var flag =
                    (bindingFindActivity.menuFindSearchEditText.hasFocus() && s?.isEmpty() == true)
                textSearch = s.toString()
                if (s?.isEmpty() == true) {
                    searchVM.showSearchHistory(flag)
                }
                searchVM.searchDebounce(textSearch)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        bindingFindActivity.menuFindSearchEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun showStart(adapter: FindAdapter) {
        bindingFindActivity.clearIcon.isClickable = false
        bindingFindActivity.searchHistoryListView.isVisible = false
        bindingFindActivity.progressBar.isVisible = false
        bindingFindActivity.clearIcon.isClickable = false
        bindingFindActivity.placeholderFindViewGroup.isVisible = false
        bindingFindActivity.placeholderButton.isVisible = false
        bindingFindActivity.tracksList.isVisible = true
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()

    }

    private fun showContent(adapter: FindAdapter, tracks: ArrayList<TrackUI>) {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.searchHistoryListView.isVisible = false
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.VISIBLE
            adapter.trackList = tracks
            adapter.notifyDataSetChanged()
        }
    }

    private fun showLoading(adapter: FindAdapter) {
        bindingFindActivity.clearIcon.isClickable = false
        bindingFindActivity.searchHistoryListView.isVisible = false
        bindingFindActivity.progressBar.isVisible = true
        bindingFindActivity.clearIcon.isClickable = false
        bindingFindActivity.placeholderFindViewGroup.isVisible = false
        bindingFindActivity.placeholderButton.isVisible = false
        bindingFindActivity.tracksList.isVisible = true
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.searchHistoryListView.isVisible = false
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.VISIBLE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindText.text =
                getString(R.string.placeholder_nothing_found_text)
            bindingFindActivity.placeholderFindTint.setImageDrawable(getDrawable(R.drawable.nothing_found))
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun showError(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.searchHistoryListView.isVisible = false
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.VISIBLE
            bindingFindActivity.placeholderButton.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindText.text =
                getString(R.string.placeholder_communication_problems_text)
            bindingFindActivity.placeholderFindTint.setImageDrawable(getDrawable(R.drawable.communication_problem))
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun showClearIcon(value: Boolean) {
        bindingFindActivity.clearIcon.isVisible = value
    }


    private fun clickedTrack(track: TrackUI) {
        if (trackClickedDebounce()) {
            searchVM.savedTrack(track.toTrack())
            val mediaIntent = Intent(this, MediaActivity::class.java)
            mediaIntent.putExtra("clickedTrack", searchVM.json.toJson(track))
            startActivity(mediaIntent)
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
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            findAdapter.trackList.clear()
            findAdapter.notifyDataSetChanged()
            historyAdapter.trackList = list as ArrayList<TrackUI>
            historyAdapter.notifyDataSetChanged()
        } else {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
        }
    }
}
//---------------------------------------------------
