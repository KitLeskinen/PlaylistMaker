package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var searchQuery = ""

    private lateinit var searchEditText: EditText

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
        Log.d("Search", "onSaveInstanceState searchQuery: $searchQuery")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY).toString()
        searchEditText.setText(searchQuery)
        Log.d("Search", "onRestoreInstanceState searchQuery: $searchQuery")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backImageView = findViewById<MaterialToolbar>(R.id.back)

        searchEditText = findViewById(R.id.searchEditText)

        searchEditText.setText(searchQuery)
        Log.d("Search", "onCreate searchQuery: $searchQuery")
        val clearImageView = findViewById<ImageView>(R.id.clear)

        clearImageView.setOnClickListener {
            searchEditText.setText("")
        }

        backImageView.setNavigationOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    clearImageView.visibility = View.INVISIBLE
                } else {
                    clearImageView.visibility = View.VISIBLE
                }
                searchQuery = searchEditText.text.toString()
            }


            override fun afterTextChanged(p0: Editable?) {

            }

        }

        searchEditText.addTextChangedListener(textWatcher)


    }

}