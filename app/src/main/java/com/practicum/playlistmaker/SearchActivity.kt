package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView



import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create


class SearchActivity : AppCompatActivity() {

    private var searchQuery = ""
    private lateinit var searchEditText: EditText
    private val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com").build()


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
        val clearImageView = findViewById<ImageView>(R.id.clear)

        val errorIcon = findViewById<ImageView>(R.id.errorIcon)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)

        val updateButton = findViewById<MaterialButton>(R.id.updateButton)
        val iTunesApi = retrofit.create<ITunesApi>()
        val tracksList  = mutableListOf<Track>()
        val searchAdapter = SearchAdapter(tracksList)

        val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        searchRecyclerView.adapter = searchAdapter


        // set blank or restored text in search field
        searchEditText.setText(searchQuery)
        Log.d("Search", "onCreate searchQuery: $searchQuery")

        // clear search field
        clearImageView.setOnClickListener() { view ->
            searchEditText.setText("")
            tracksList.clear()
            searchAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // close SearchActivity
        backImageView.setNavigationOnClickListener {
            finish()
        }

        // show or hide clear clear button for text search
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    clearImageView.isVisible = false
                } else {
                    clearImageView.isVisible = true
                }
                searchQuery = searchEditText.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }

        searchEditText.addTextChangedListener(textWatcher)

        var lastSearch = ""

        fun makeResponse(text: String){
            iTunesApi.search(text).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        searchRecyclerView.isVisible = true

                        val responseBody = response.body()?.string()
                        val gson = Gson()
                        val searchResult = gson.fromJson(responseBody, SearchResult::class.java)
                        Log.d("RESPONSE", responseBody.toString())
                        if(searchResult.resultCount == 0){
                            errorIcon.setImageResource(R.drawable.no_search_results)
                            errorMessage.text = getString(R.string.no_results)
                            errorIcon.isVisible = true
                            errorMessage.isVisible = true
                        } else {
                            errorIcon.visibility = View.GONE
                            errorMessage.visibility = View.GONE
                        }
                        tracksList.clear()
                        tracksList.addAll(searchResult.tracks)
                        Log.d("TRACKS", searchResult.tracks.toString())
                        searchAdapter.notifyDataSetChanged()

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("RESPONSE", t.toString())
                    searchRecyclerView.visibility = View.GONE
                    errorIcon.setImageResource(R.drawable.no_internet)
                    errorIcon.isVisible = true
                    errorMessage.text = getString(R.string.no_internet_check_connection)
                    errorMessage.isVisible = true
                    updateButton.isVisible = true
                    lastSearch = searchEditText.text.toString()

                }
            }
            )
        }

        updateButton.setOnClickListener(){ view ->
            makeResponse(lastSearch)
            searchEditText.setText(lastSearch)
            updateButton.visibility = View.GONE
            errorIcon.visibility = View.GONE
            errorMessage.visibility = View.GONE
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeResponse(searchEditText.text.toString())
                true
            }
            false
        }
    }

}