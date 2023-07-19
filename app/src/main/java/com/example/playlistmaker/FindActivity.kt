package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY_TRACK_LIST = "Search history list"
const val TRACK_HISTORY_SHAREDPREFERENCES = "Track history"

class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val baseUrlFilms = " https://itunes.apple.com"
    }
    private lateinit var plaseholderFindViewGroup: LinearLayout
    private lateinit var searchHistoryListView: LinearLayout
    private lateinit var placeholderFindTint: ImageView
    private lateinit var placeholderFindText: TextView
    private lateinit var updateButton: Button
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var historyTrackList: RecyclerView
    private lateinit var clearSearchHistory: Button

    private var textSearch = ""
    private var textSearchLast = ""
    private var trackList: ArrayList<Track> = ArrayList()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrlFilms)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

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
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
//---------------------------------------------------
//---------------------------------------------------
        val back = findViewById<ImageView>(R.id.backFind)
        val searchEditText = findViewById<EditText>(R.id.menuFind_SearchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        plaseholderFindViewGroup = findViewById<LinearLayout>(R.id.placeholder_find_view_group)
        placeholderFindTint = findViewById<ImageView>(R.id.placeholder_find_tint)
        placeholderFindText = findViewById<TextView>(R.id.placeholder_find_text)
        updateButton = findViewById<Button>(R.id.placeholder_button)
        searchHistoryListView = findViewById(R.id.search_History_List_View)
        clearSearchHistory = findViewById(R.id.clearSearchHistory)

        val searchHistorySharedPreferences =
            getSharedPreferences(
                TRACK_HISTORY_SHAREDPREFERENCES,
                MODE_PRIVATE
            )
        val searchHistory = SearchHistory(searchHistorySharedPreferences)
        // Задаем адаптеры
        val findAdapter = FindAdapter {
            clickedTrack(it, searchHistory)
        }
        val historyAdapter = FindAdapter {
            clickedTrack(it, searchHistory)
        }
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {sharedPreferences, key ->
            if (key == SEARCH_HISTORY_TRACK_LIST) {
                searchHistory.searchHistoryList = getTrackList(
                    searchHistorySharedPreferences.getString(SEARCH_HISTORY_TRACK_LIST, null)
                )
                setVisibilityViewsForShowSearchHistory(
                    searchEditText.text.isEmpty() && searchHistory.searchHistoryList.isNotEmpty(),
                    findAdapter, historyAdapter, searchHistory
                )
            }}
        searchHistorySharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        findAdapter.trackList = trackList
        historyAdapter.trackList = searchHistory.searchHistoryList
//---------------------------------------------------
        trackRecyclerView = findViewById<RecyclerView>(R.id.tracksList)
        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = findAdapter
        historyTrackList = findViewById(R.id.historyTracksList)
        historyTrackList.layoutManager = LinearLayoutManager(this)
        historyTrackList.adapter = historyAdapter
//---------------------------------------------------
        searchEditText.setText(textSearch)
//---------------------------------------------------
// слушатель кнопки на клавиаруре
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchHistoryListView.isVisible = false
                trackRecyclerView.isVisible = true
                getMusic(textSearch, findAdapter)
                true
            }
            false
        }
// слушатель кнопки "обновить"
        updateButton.setOnClickListenerWithViber {
            getMusic(textSearchLast, findAdapter)
        }
//---------------------------------------------------
        clearButton.setOnClickListener {
            searchEditText.setText("")
            trackList.clear()
            findAdapter.notifyDataSetChanged()
            setVisibilitySearchСompleted()
        }
//---------------------------------------------------
        back.setOnClickListenerWithViber {
            finish()
        }
//---------------------------------------------------
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            searchHistory.searchHistoryList = getTrackList(
                searchHistorySharedPreferences.getString(SEARCH_HISTORY_TRACK_LIST, null)
            )
            setVisibilityViewsForShowSearchHistory(
                hasFocus
                        && searchEditText.text.isEmpty()
                        && searchHistory.searchHistoryList.isNotEmpty(),
                findAdapter, historyAdapter, searchHistory
            )
        }
//---------------------------------------------------
        clearSearchHistory.setOnClickListenerWithViber {
            searchHistory.clearHistory()
            setVisibilityViewsForShowSearchHistory(
                searchHistory.searchHistoryList.isNotEmpty(),
                findAdapter, historyAdapter, searchHistory,
            )
        }
//---------------------------------------------------
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setVisibilityViewsForShowSearchHistory(
                    searchEditText.hasFocus()
                            && s?.isEmpty() == true
                            && searchHistory.searchHistoryList.isNotEmpty(),
                    findAdapter, historyAdapter, searchHistory
                )
                textSearch = searchEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    //---------------------------------------------------
    private fun getMusic(text: String, adapter: FindAdapter) {
        iTunesService
            .searchTracks(text)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    when (response.code()) {

                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                setVisibilitySearchСompleted()
                                trackList.clear()
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                setPlaceholderNothingFound(adapter)
                            }
                        }

                        else -> {
                            setPlaceholderCommunicationProblems(adapter)
                            textSearchLast = textSearch
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    setPlaceholderCommunicationProblems(adapter)
                    textSearchLast = textSearch
                }

            })
    }

    //---------------------------------------------------
    private fun setVisibilitySearchСompleted() {
        plaseholderFindViewGroup.visibility = View.GONE
        updateButton.visibility = View.GONE
        trackRecyclerView.visibility = View.VISIBLE
    }

    private fun setPlaceholderNothingFound(adapter: FindAdapter) {
        plaseholderFindViewGroup.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        trackRecyclerView.visibility = View.GONE
        placeholderFindText.text = getString(R.string.placeholder_nothing_found_text)
        placeholderFindTint.setImageDrawable(getDrawable(R.drawable.nothing_found))
        trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun setPlaceholderCommunicationProblems(adapter: FindAdapter) {
        plaseholderFindViewGroup.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE
        trackRecyclerView.visibility = View.GONE
        placeholderFindText.text = getString(R.string.placeholder_communication_problems_text)
        placeholderFindTint.setImageDrawable(getDrawable(R.drawable.communication_problem))
        trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun setVisibilityViewsForShowSearchHistory(
        focus: Boolean,
        adapter: FindAdapter,
        historyAdapter: FindAdapter,
        searchHistory: SearchHistory
    ) {
        if (focus) {
            searchHistoryListView.visibility = View.VISIBLE
            trackRecyclerView.visibility = View.GONE
            plaseholderFindViewGroup.visibility = View.GONE
            updateButton.visibility = View.GONE
            adapter.trackList.clear()
            adapter.notifyDataSetChanged()
            historyAdapter.trackList = searchHistory.searchHistoryList
            historyAdapter.notifyDataSetChanged()
        } else {
            searchHistoryListView.visibility = View.GONE
            trackRecyclerView.visibility = View.VISIBLE
            historyAdapter.trackList = searchHistory.searchHistoryList
            historyAdapter.notifyDataSetChanged()
        }
    }
    private fun clickedTrack(track: Track, searchHistory: SearchHistory){
        searchHistory.savedTrack(track)
        val mediaIntent = Intent(this, MediaActivity::class.java)
        mediaIntent.putExtra("clickedTrack", Gson().toJson(track))
        startActivity(mediaIntent)
    }
}


//---------------------------------------------------

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}