package com.example.picobotella.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("forms")
    val forms: List<PokemonForm>
)

data class PokemonForm(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)