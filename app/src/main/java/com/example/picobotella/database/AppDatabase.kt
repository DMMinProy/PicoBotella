package com.example.picobotella.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// le dice a Room qué tablas tiene esta base de datos
// entities = las tablas (por ahora solo reto)
// version = 1: si cambiala estructura despues, se aumenta este número
@Database(entities = [Challenge::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun challengeDao(): ChallengeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: porq solo existe 1 base de datos en toda la app
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pico_botella_bd"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}