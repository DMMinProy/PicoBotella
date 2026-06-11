package com.example.picobotella.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.picobotella.database.Challenge
import com.example.picobotella.repository.ChallengeRepo
import kotlinx.coroutines.launch

class ChallengeViewModel (application: Application)  : AndroidViewModel(application) {
    private val repository = ChallengeRepo(application)

    // El Fragment observa esta lista
    val allRetos: LiveData<List<Challenge>> = repository.allChallenge

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
}