package com.example.createyourownapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pair<String, String>>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    // This class holds the UI elements for a single list item (from pokemon_item.xml)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
    }

    // Creates a new view holder when the RecyclerView needs one
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return ViewHolder(view)
    }

    // Binds the data to the views for a specific item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, url) = pokemonList[position]

        // Extract the Pokémon ID from its URL to construct the image URL
        val pokemonId = url.split("/").dropLast(1).last()
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

        // Set the Pokémon's name
        holder.pokemonName.text = name.replaceFirstChar { it.uppercase() }

        // Use Glide to load the image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .centerCrop()
            .into(holder.pokemonImage)
    }

    // Returns the total number of items in the data list
    override fun getItemCount() = pokemonList.size
