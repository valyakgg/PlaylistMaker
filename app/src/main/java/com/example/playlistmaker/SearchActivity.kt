package com.example.playlistmaker

import ApiResponse
import ITunesApiService
import Song
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private var trackList = mutableListOf<Track>()
    private var lastSearchQuery: String? = null

    private lateinit var apiService: ITunesApiService

    private lateinit var noResultsLayout: View
    private lateinit var errorLayout: View
    private lateinit var retryButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)
        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TrackAdapter(trackList)
        recyclerView.adapter = adapter

        noResultsLayout = findViewById(R.id.noResultsLayout)
        errorLayout = findViewById(R.id.errorLayout)
        retryButton = findViewById(R.id.retryButton)

        retryButton.setOnClickListener {
            lastSearchQuery?.let { query -> search(query) }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ITunesApiService::class.java)

        settingsBackButton.setOnClickListener {
            finish()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.setText("")
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)
            clearButton.visibility = View.GONE
            recyclerView.visibility = View.GONE
            noResultsLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
            trackList.clear()
            adapter.notifyDataSetChanged()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchEditText.text.toString())
                hideKeyboard(searchEditText)
                true
            } else {
                false
            }
        }

        if (savedInstanceState != null) {
            lastSearchQuery = savedInstanceState.getString(KEY_SEARCH_TEXT)
            searchEditText.setText(lastSearchQuery)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, lastSearchQuery)
    }

    private fun search(query: String) {
        if (query.isEmpty()) return

        lastSearchQuery = query

        recyclerView.visibility = View.GONE
        noResultsLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE

        apiService.search(query).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val songs = response.body()!!.results
                    if (songs.isEmpty()) {
                        showNoResults()
                    } else {
                        displaySearchResults(songs)
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showNoResults() {
        recyclerView.visibility = View.GONE
        noResultsLayout.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private fun showError() {
        recyclerView.visibility = View.GONE
        noResultsLayout.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
    }

    private fun displaySearchResults(songs: List<Song>) {
        noResultsLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        val updatedTrackList = songs.map { song ->
            Track(
                trackName = song.trackName,
                artistName = song.artistName,
                trackTime = formatTrackTime(song.trackTimeMillis),
                artworkUrl100 = song.artworkUrl100 ?: ""
            )
        }
        trackList.clear()
        trackList.addAll(updatedTrackList)
        adapter.notifyDataSetChanged()
    }

    private fun formatTrackTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val KEY_SEARCH_TEXT = "searchText"
    }
}
