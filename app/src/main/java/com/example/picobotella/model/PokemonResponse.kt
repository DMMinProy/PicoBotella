package com.example.picobotella.model

import com.google.gson.annotations.SerializedName

data class PokedexResponse(
    @SerializedName("pokemon")
    val pokemon: List<PokemonResponse>
)

data class PokemonResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val img: String
)