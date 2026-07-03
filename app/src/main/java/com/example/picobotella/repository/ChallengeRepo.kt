package com.example.picobotella.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.picobotella.data.AppDatabase
import com.example.picobotella.model.Challenge

//Este es el puente entre la BD y el ViewModel
class ChallengeRepo(context: Context) {
    private val ChallengeDao = AppDatabase.getDatabase(context).challengeDao()

    val allChallenge: LiveData<List<Challenge>> = ChallengeDao.getAllChallenge()

    suspend fun insert(challenge: Challenge)=ChallengeDao.insertChallenge(challenge)
    suspend fun update(challenge: Challenge)=ChallengeDao.updateChallenge(challenge)
    suspend fun delete(challenge: Challenge)=ChallengeDao.deleteChallenge(challenge)
    suspend fun getAllChallengeForGame(): List<Challenge> = ChallengeDao.getAllChallengeForGame()
}