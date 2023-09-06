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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Util.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityFindBinding
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.mappers.MapToTrackUI
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.presentation.ui.mediaPlayer.MediaActivity
import com.example.playlistmaker.Util.setOnClickListenerWithViber
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.google.gson.Gson


class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICED_TRACK_DELAY = 1000L
    }

    private lateinit var bindingFindActivity: ActivityFindBinding
    private var isClickTrackAllowed = true
    private var textSearch = ""
    private var trackList: ArrayList<TrackUI>? = ArrayList()
    private var tracklistInterator: List<TrackUI>? = emptyList()
    private val handlerMain: Handler = Handler(Looper.getMainLooper())
    private lateinit var searchVM: SearchViewModel
    private val searchHistoryInteractorImpl by lazy { App().creator.provideGetSearchHistoryInteractor(this) }
    private val trackInteractor = App().creator.provideGetTrackInteractor()
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
//---------------------------------------------------
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
//---------------------------------------------------
        bindingFindActivity.tracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.tracksList.adapter = findAdapter

        bindingFindActivity.historyTracksList.layoutManager = LinearLayoutManager(this)
        bindingFindActivity.historyTracksList.adapter = historyAdapter
//---------------------------------------------------
        bindingFindActivity.menuFindSearchEditText.setText(textSearch)
// слушатель кнопки "обновить"
        bindingFindActivity.placeholderButton.setOnClickListenerWithViber {
            bindingFindActivity.placeholderButton.visibility = View.GONE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            bindingFindActivity.progressBar.visibility = View.VISIBLE
            getMusic(textSearch, findAdapter)
        }
//---------------------------------------------------
        bindingFindActivity.clearIcon.setOnClickListener {
            bindingFindActivity.menuFindSearchEditText.setText("")
            trackList?.clear()
            findAdapter.notifyDataSetChanged()
            setVisibilityViewsForShowSearchHistory(
                searchHistoryInteractorImpl.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter
            )
        }
//---------------------------------------------------
        bindingFindActivity.backFind.setOnClickListenerWithViber {
            finish()
        }
//---------------------------------------------------
        bindingFindActivity.menuFindSearchEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryInteractorImpl.setTrackList(
                   searchHistoryInteractorImpl.getTracksList()
            )
            setVisibilityViewsForShowSearchHistory(
                hasFocus
                        && bindingFindActivity.menuFindSearchEditText.text.isEmpty()
                        && searchHistoryInteractorImpl.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter
            )
        }
//---------------------------------------------------
        bindingFindActivity.clearSearchHistory.setOnClickListenerWithViber {
            searchHistoryInteractorImpl.clear()
            setVisibilityViewsForShowSearchHistory(
                searchHistoryInteractorImpl.getTracksList().isNotEmpty(),
                findAdapter, historyAdapter
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
                            && searchHistoryInteractorImpl.getTracksList().isNotEmpty(),
                    findAdapter, historyAdapter
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
            val result = trackInteractor.getMusic(text, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {

                        is ConsumerData.Data -> {
                            if (data.value.isNotEmpty()) {
                                tracklistInterator = MapToTrackUI().mapList(data.value)
                                trackList?.addAll(0, tracklistInterator as ArrayList<TrackUI>)
                                handlerMain.post { visibilitySearchingСomplet() }
                                adapter.trackList = trackList!!
                            } else {
                                handlerMain.post { setPlaceholderNothingFound(adapter) }
                            }
                        }

                        is ConsumerData.Error -> {
                            handlerMain.post { setPlaceholderCommunicationProblems(adapter) }
                        }

                    }
                }

            })
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


    private fun clickedTrack(track: TrackUI) {
        if (trackClickedDebounce()) {
            searchHistoryInteractorImpl.saved(track.toTrack())
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

    fun setVisibilityViewsForShowSearchHistory(
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
            bindingFindActivity.tracksList.visibility = View.VISIBLE
            bindingFindActivity.placeholderFindViewGroup.visibility = View.GONE
            historyAdapter.trackList =
                MapToTrackUI().mapList(searchHistoryInteractorImpl.getTracksList())
            historyAdapter.notifyDataSetChanged()
        }
    }
}
//---------------------------------------------------
