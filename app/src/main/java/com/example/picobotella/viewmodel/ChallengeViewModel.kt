package com.example.picobotella.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.picobotella.database.Challenge
import com.example.picobotella.model.PokemonResponse
import com.example.picobotella.repository.ChallengeRepo
import com.example.picobotella.webservice.RetrofitClient
import kotlinx.coroutines.launch

class ChallengeViewModel (application: Application)  : AndroidViewModel(application) {
    private val repository = ChallengeRepo(application)

    // El Fragment observa esta lista
    val allRetos: LiveData<List<Challenge>> = repository.allChallenge

    // LiveData privado y público para exponer el resultado de la API al Fragment
    private val _randomPokemon = MutableLiveData<PokemonResponse?>()
    val randomPokemon: LiveData<PokemonResponse?> get() = _randomPokemon

    // LiveData para manejar estados de error de red de forma limpia
    private val _networkError = MutableLiveData<String?>()
    val networkError: LiveData<String?> get() = _networkError

    // No se congela la pantalla mientras escribe en la BD
    fun insert(descripcion: String) {
        viewModelScope.launch {
            repository.insert(Challenge(description = descripcion))
        }
    }

    fun update(reto: Challenge) {
        viewModelScope.launch { repository.update(reto) }
    }

    fun delete(reto: Challenge) {
        viewModelScope.launch { repository.delete(reto) }
    }

    fun fetchRandomPokemon() {
        val randomId = (1..1010).random().toString()

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getPokemonDetails(randomId)
                if (response.isSuccessful) {
                    _randomPokemon.postValue(response.body())
                    _networkError.postValue(null)
                } else {
                    _networkError.postValue("Error: ${response.code()}")
                    _randomPokemon.postValue(null)
                }
            } catch (e: Exception) {
                _networkError.postValue(e.localizedMessage ?: "Error de red")
                _randomPokemon.postValue(null)
            }
        }
    }

    fun clearPokemonState() {
        _randomPokemon.value = null
        _networkError.value = null
    }
}