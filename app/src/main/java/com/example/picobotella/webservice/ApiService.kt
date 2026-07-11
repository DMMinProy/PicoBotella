package com.example.picobotella.webservice

import com.example.picobotella.model.PokedexResponse
import com.example.picobotella.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {

    @GET("Biuni/PokemonGO-Pokedex/master/pokedex.json")
    suspend fun getPokedex(
    ): Response<PokedexResponse>
}