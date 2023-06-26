package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.drawable.Drawable
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val baseUrlFilms = " https://itunes.apple.com"
    }

    private lateinit var plaseholderFindViewGroup: LinearLayout
    private lateinit var placeholderFindTint: ImageView
    private lateinit var placeholderFindText: TextView
    private lateinit var updateButton: Button
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var tintPlaceholderNF: Drawable
    private lateinit var tintPlaceholderCP: Drawable
    private var textSearch = ""
    private var textSearchLast = ""
    private var trackList: ArrayList<Track> = ArrayList()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrlFilms)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val adapter = FindAdapter(trackList)

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
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        // проверяем включена ли темная тема и устанавливаем для заглушек
        // соответсвующие изображения из ресурсов

//---------------------------------------------------
        val back = findViewById<ImageView>(R.id.backFind)
        val searchEditText = findViewById<EditText>(R.id.menuFind_SearchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        plaseholderFindViewGroup = findViewById<LinearLayout>(R.id.placeholder_find_view_group)
        placeholderFindTint = findViewById<ImageView>(R.id.placeholder_find_tint)
        placeholderFindText = findViewById<TextView>(R.id.placeholder_find_text)
        updateButton = findViewById<Button>(R.id.placeholder_button)
//---------------------------------------------------
        trackRecyclerView = findViewById<RecyclerView>(R.id.tracksList)
        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = adapter
//---------------------------------------------------
        searchEditText.setText(textSearch)
//---------------------------------------------------
// слушатель кнопки на клавиаруре
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getMusic(textSearch)
                true
            }
            false
        }
// слушатель кнопки "обновить"
        updateButton.setOnClickListener {
            getMusic(textSearchLast)
        }
//---------------------------------------------------
        clearButton.setOnClickListener {
            searchEditText.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()

            plaseholderFindViewGroup.visibility = View.GONE
            updateButton.visibility = View.GONE
            trackRecyclerView.visibility = View.VISIBLE
        }
//---------------------------------------------------
        back.setOnClickListener {
            finish()
        }
//---------------------------------------------------
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = searchEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    //---------------------------------------------------
    private fun getMusic(text: String) {
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
                                setPlaceholderNothingFound()
                            }
                        }

                        else -> {
                            setPlaceholderCommunicationProblems()
                            textSearchLast = textSearch
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    setPlaceholderCommunicationProblems()
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

    private fun setPlaceholderNothingFound() {
        plaseholderFindViewGroup.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        trackRecyclerView.visibility = View.GONE
        placeholderFindText.text = getString(R.string.placeholder_nothing_found_text)
        placeholderFindTint.setImageDrawable(getDrawable(R.drawable.nothing_found))
    }

    private fun setPlaceholderCommunicationProblems() {
        plaseholderFindViewGroup.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE
        trackRecyclerView.visibility = View.GONE
        placeholderFindText.text = getString(R.string.placeholder_communication_problems_text)
        placeholderFindTint.setImageDrawable(getDrawable(R.drawable.communication_problem))
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
