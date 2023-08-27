package com.example.playlistmaker.presentetion.ui.find

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.domain.impl.ClearButtonSetViewVisibilityUseCase
import com.example.playlistmaker.domain.impl.SearchHistoryInteractor
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentetion.getTrackListFromJson
import com.example.playlistmaker.presentetion.setOnClickListenerWithViber
import com.example.playlistmaker.presentetion.ui.mediaPlayer.MediaActivity
import com.google.gson.Gson
import java.io.IOException

const val SEARCH_HISTORY_TRACK_LIST = "Search history list"
const val TRACK_HISTORY_SHAREDPREFERENCES = "Track history"

class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICED_TRACK_DELAY = 1000L
    }

    private lateinit var bindingFindActivity: ActivityFindBinding
    private var isClickTrackAllowed = true
    private var textSearch = ""
    private var textSearchLast = ""
    private var trackList: ArrayList<Track>? = ArrayList()
    private var tracklistInterator: List<Track>? = emptyList()
    private val handlerMain: Handler = Handler(Looper.getMainLooper())

    private val networkClient = RetrofitNetworkClient()
    private val tracksRepository = TracksRepositoryImpl(networkClient)
    private val trackInteractor = TracksInteractorImpl(tracksRepository)
    private val setVisibilityClearButton = ClearButtonSetViewVisibilityUseCase()

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
//---------------------------------------------------

        val searchHistorySharedPreferences =
            getSharedPreferences(
                TRACK_HISTORY_SHAREDPREFERENCES,
                MODE_PRIVATE
            )
        val searchHistoryInteractor = SearchHistoryInteractor(searchHistorySharedPreferences)
        // Задаем адаптеры
        val findAdapter = FindAdapter {
            clickedTrack(it, searchHistoryInteractor)
        }
        val historyAdapter = FindAdapter {
            clickedTrack(it, searchHistoryInteractor)
        }
        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == SEARCH_HISTORY_TRACK_LIST) {
                    searchHistoryInteractor.setTrackList(
                        getTrackListFromJson(
                            searchHistorySharedPreferences.getString(
                                SEARCH_HISTORY_TRACK_LIST,
                                null
                            )
                        )
                    )
                    setVisibilityViewsForShowSearchHistory(
                        bindingFindActivity.menuFindSearchEditText.text.isEmpty() && searchHistoryInteractor.getTracksList()
                            .isNotEmpty(),
                        findAdapter, historyAdapter, searchHistoryInteractor
                    )
                }
            }
        searchHistorySharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        findAdapter.trackList = (trackList as ArrayList<Track>)
        historyAdapter.trackList = searchHistoryInteractor.getTracksList()
//---------------------------------------------------
        bindingFindActivity.tracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.tracksList.adapter = findAdapter

        bindingFindActivity.historyTracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.historyTracksList.adapter = historyAdapter
//---------------------------------------------------
        bindingFindActivity.menuFindSearchEditText.setText(textSearch)
// слушатель кнопки "обновить"
        bindingFindActivity.placeholderButton.setOnClickListenerWithViber {
            getMusic(textSearchLast, findAdapter)
        }
//---------------------------------------------------
        bindingFindActivity.clearIcon.setOnClickListener {
            bindingFindActivity.menuFindSearchEditText.setText("")
            trackList?.clear()
            findAdapter.notifyDataSetChanged()
            setVisibilityViewsForShowSearchHistory(
                searchHistoryInteractor.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter, searchHistoryInteractor,
            )
        }
//---------------------------------------------------
        bindingFindActivity.backFind.setOnClickListenerWithViber {
            finish()
        }
//---------------------------------------------------
        bindingFindActivity.menuFindSearchEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryInteractor.setTrackList(
                getTrackListFromJson(
                    searchHistorySharedPreferences.getString(SEARCH_HISTORY_TRACK_LIST, null)
                )
            )
            setVisibilityViewsForShowSearchHistory(
                hasFocus
                        && bindingFindActivity.menuFindSearchEditText.text.isEmpty()
                        && searchHistoryInteractor.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter, searchHistoryInteractor
            )
        }
//---------------------------------------------------
        bindingFindActivity.clearSearchHistory.setOnClickListenerWithViber {
            searchHistoryInteractor.clear()
            setVisibilityViewsForShowSearchHistory(
                searchHistoryInteractor.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter, searchHistoryInteractor,
            )
        }
//---------------------------------------------------
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setVisibilityViewsForShowSearchHistory(
                    bindingFindActivity.menuFindSearchEditText.hasFocus()
                            && s?.isEmpty() == true
                            && searchHistoryInteractor.getTracksList().isNotEmpty(),
                    findAdapter, historyAdapter, searchHistoryInteractor
                )

                textSearch = bindingFindActivity.menuFindSearchEditText.text.toString()
                bindingFindActivity.clearIcon.isVisible = setVisibilityClearButton.execute(s)
                if (s?.isNotEmpty() == true) searchDebounce(findAdapter)
                else handlerMain.removeCallbacks { getMusic(textSearch, findAdapter) }
                bindingFindActivity.clearIcon.isClickable = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        bindingFindActivity.menuFindSearchEditText.addTextChangedListener(simpleTextWatcher)
    }

    //---------------------------------------------------
    // несколько криво но обрабатывать ошибки запроса в сеть по реализованной схеме (синхронно) обещают в след спринтах
    private fun getMusic(text: String, adapter: FindAdapter) {
        if (text.isNotEmpty()) {
            adapter.notifyDataSetChanged()
            val t = Thread {
                var flagIOException = false
                try {
                    tracklistInterator = trackInteractor.getMusic(text)
                } catch (e: IOException) {
                    handlerMain.post { setPlaceholderCommunicationProblems(adapter) }
                    textSearchLast = textSearch
                    flagIOException = true
                }
                if (tracklistInterator?.isEmpty() == true && !flagIOException) {
                    handlerMain.post { setPlaceholderNothingFound(adapter) }
                }
                if (tracklistInterator?.isNotEmpty() == true) {
                    trackList?.addAll(0, tracklistInterator as ArrayList<Track>)
                    handlerMain.post { visibilitySearchingСomplet() }
                    adapter.trackList = trackList!!
                }
            }
            t.start()
        }
    }

    //---------------------------------------------------
    private fun visibilitySearchingСomplet() {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.VISIBLE
        }
    }

    private fun setPlaceholderNothingFound(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.VISIBLE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindText.text =
                getString(R.string.placeholder_nothing_found_text)
            bindingFindActivity.placeholderFindTint.setImageDrawable(getDrawable(R.drawable.nothing_found))
            trackList?.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun setPlaceholderCommunicationProblems(adapter: FindAdapter) {
        if (textSearch.isNotEmpty()) {
            bindingFindActivity.clearIcon.isClickable = true
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.VISIBLE
            bindingFindActivity.placeholderButton.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindText.text =
                getString(R.string.placeholder_communication_problems_text)
            bindingFindActivity.placeholderFindTint.setImageDrawable(getDrawable(R.drawable.communication_problem))
            trackList?.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun setVisibilityViewsForShowSearchHistory(
        focus: Boolean,
        adapter: FindAdapter,
        historyAdapter: FindAdapter,
        searchHistoryInteractor: SearchHistoryInteractor
    ) {
        if (focus) {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.VISIBLE
            bindingFindActivity.tracksList.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.placeholderButton.visibility = View.GONE
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
            historyAdapter.trackList = searchHistoryInteractor.getTracksList()
            historyAdapter.notifyDataSetChanged()
        } else {
            bindingFindActivity.progressBar.visibility = View.GONE
            bindingFindActivity.searchHistoryListView.visibility = View.GONE
            bindingFindActivity.tracksList.visibility = View.VISIBLE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            historyAdapter.trackList = searchHistoryInteractor.getTracksList()
            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun clickedTrack(track: Track, searchHistory: SearchHistoryInteractor) {
        if (trackClickedDebounce()) {
            searchHistory.saved(track)
            val mediaIntent = Intent(this, MediaActivity::class.java)
            mediaIntent.putExtra("clickedTrack", Gson().toJson(track))
            startActivity(mediaIntent)
        }
    }

    // функция для планирования поиска музыки по введенному в поисковую чстроку тексту спустя
    // время SEARCH_DEBOUNCE_DELAY после окончания ввода текста
    //debounce пользовательского ввода
    private fun searchDebounce(findAdapter: FindAdapter) {
        bindingFindActivity.progressBar.visibility = View.VISIBLE
        bindingFindActivity.tracksList.visibility = View.GONE
        handlerMain.removeCallbacks { getMusic(textSearch, findAdapter) }
        handlerMain.postDelayed({ getMusic(textSearch, findAdapter) }, SEARCH_DEBOUNCE_DELAY)
    }

    private fun trackClickedDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            handlerMain.postDelayed({ isClickTrackAllowed = true }, CLICED_TRACK_DELAY)
        }
        return current
    }

}
//---------------------------------------------------
