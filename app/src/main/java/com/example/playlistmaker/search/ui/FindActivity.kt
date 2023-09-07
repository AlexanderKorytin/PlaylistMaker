package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.player.ui.mappers.MapToTrackUI
import com.example.playlistmaker.presentation.ui.mediaPlayer.MediaActivity
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModelFactory
import com.google.gson.Gson


class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val CLICED_TRACK_DELAY = 1000L
    }

    private lateinit var bindingFindActivity: ActivityFindBinding
    private var isClickTrackAllowed = true
    private var textSearch = ""
    private var trackList: ArrayList<TrackUI>? = ArrayList()
    private val handlerMain: Handler = Handler(Looper.getMainLooper())
    private lateinit var searchVM: SearchViewModel
    private val searchHistoryInteractorImpl by lazy {
        App().creator.provideGetSearchHistoryInteractor(
            this
        )
    }
    private val setVisibilityClearButton = App().creator.provideGetSetViewVisibilityUseCase()


    //---------------------------------------------------
    // запоминаем текст в EditText и восстанавливаем при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, textSearch)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(SEARCH_QUERY, "")
    }

    //---------------------------------------------------
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        findAdapter.trackList = (trackList as ArrayList<TrackUI>)
        historyAdapter.trackList =
            MapToTrackUI().mapList(searchHistoryInteractorImpl.getTracksList())
        bindingFindActivity.tracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.tracksList.adapter = findAdapter

        bindingFindActivity.historyTracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.historyTracksList.adapter = historyAdapter
        searchVM =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]
// ------------- подписки----------------------------
        searchVM.getcurrentSearchViewScreenState().observe(this) {
            when (it) {
                is SearchScreenState.Hictory -> {
                    showSearchHistory(true, findAdapter, historyAdapter)
                }

                is SearchScreenState.EmptyHistory -> {
                    showSearchHistory(false, findAdapter, historyAdapter)
                }

                is SearchScreenState.Empty -> {
                    showEmpty(findAdapter)
                }

                is SearchScreenState.isLoading -> {
                    showLoading(findAdapter)
                }

                is SearchScreenState.Error -> {
                    showError(findAdapter)
                }

                is SearchScreenState.Content -> {
                    showContent(findAdapter, it.tracks as ArrayList<TrackUI>)
                }
            }
        }

        bindingFindActivity.menuFindSearchEditText.setText(textSearch)

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
            searchVM.showSearchHistory(hasFocus)
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
                searchVM.showSearchHistory(flag)
                textSearch = s.toString()
                searchVM.searchDebounce(textSearch)
                bindingFindActivity.clearIcon.isVisible = setVisibilityClearButton.execute(s)
                bindingFindActivity.clearIcon.isClickable = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        bindingFindActivity.menuFindSearchEditText.addTextChangedListener(simpleTextWatcher)
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


    private fun clickedTrack(track: TrackUI) {
        if (trackClickedDebounce()) {
            searchHistoryInteractorImpl.saved(track.toTrack())
            val mediaIntent = Intent(this, MediaActivity::class.java)
            mediaIntent.putExtra("clickedTrack", Gson().toJson(track))
            startActivity(mediaIntent)
        }
    }

    private fun trackClickedDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            handlerMain.postDelayed({ isClickTrackAllowed = true }, CLICED_TRACK_DELAY)
        }
        return current
    }

    fun showSearchHistory(
        flag: Boolean,
        findAdapter: FindAdapter,
        historyAdapter: FindAdapter
    ) {
      if (flag) {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            findAdapter.trackList.clear()
            findAdapter.notifyDataSetChanged()
            historyAdapter.trackList =
                MapToTrackUI().mapList(searchHistoryInteractorImpl.getTracksList())
            historyAdapter.notifyDataSetChanged()
      } else {
          bindingFindActivity.progressBar.visibility = View.GONE
          bindingFindActivity.searchHistoryListView.visibility = View.GONE
          bindingFindActivity.tracksList.visibility = View.GONE
          bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
          historyAdapter.trackList =
              MapToTrackUI().mapList(searchHistoryInteractorImpl.getTracksList())
          historyAdapter.notifyDataSetChanged()
      }
    }
}
//---------------------------------------------------
