package com.example.picobotella.database
import androidx.lifecycle.LiveData
import androidx.room.*

//Se definen las operaciones
@Dao
interface ChallengeDao {

    //trae retos y se actualiza automaticamente
    @Query("SELECT * FROM challenge ORDER BY id DESC")
    fun getAllChallenge(): LiveData<List<Challenge>>
    //para ejecutar un hilo secundario
    @Insert
    suspend fun insertChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)
    // Listo para la HU9 eliminar
    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    //Para la (Hu12) sin LiveData
    @Query("SELECT * FROM challenge")
    suspend fun getAllChallengeForGame(): List<Challenge>

}