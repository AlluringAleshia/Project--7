package com.example.createyourownapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonRecyclerView: RecyclerView
    private var pokemonList = mutableListOf<Pair<String, String>>()

    // We need a reference to the adapter that we can access later.
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Find the RecyclerView in our layout.
        pokemonRecyclerView = findViewById(R.id.pokemon_recycler_view)

        // 2. THIS IS THE FIX (PART 1):
        // Create the adapter immediately with the empty list and attach it.
        adapter = PokemonAdapter(pokemonList)
        pokemonRecyclerView.adapter = adapter

        // 3. Set up how the list will be displayed (linearly).
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)

        // 4. Now, fetch the data that will fill the list.
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon?limit=151"

        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("PokemonAPI", "Response successful!")

                try {
                    // Before adding the new data, clear the old data.
                    pokemonList.clear()

                    val resultsArray = json.jsonObject.getJSONArray("results")

                    // Loop through the results and add each Pokémon to our list.
                    for (i in 0 until resultsArray.length()) {
                        val pokemonObject = resultsArray.getJSONObject(i)
                        val name = pokemonObject.getString("name")
                        val url = pokemonObject.getString("url")
                        pokemonList.add(Pair(name, url))
                    }

                    // 5. THIS IS THE FIX (PART 2):
                    // Do NOT create a new adapter. Just tell the existing one to update.
                    // This will refresh the RecyclerView with the new data.
                    adapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e("PokemonAPI", "Error parsing JSON", e)
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("PokemonAPI", "Request failed: $errorResponse")
            }
        })
    }
}
