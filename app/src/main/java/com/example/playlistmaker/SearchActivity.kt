package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)

        settingsBackButton.setOnClickListener {
            finish()
        }

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val clearButton: ImageView = findViewById(R.id.clearButton)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(KEY_SEARCH_TEXT)
            searchEditText.setText(searchText)
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (charSequence.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                searchText = charSequence?.toString()
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.setText("")
            searchEditText.clearFocus()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_SEARCH_TEXT)
    }

    companion object {
        private const val KEY_SEARCH_TEXT = "searchText"
    }
}
