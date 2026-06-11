package com.example.picobotella.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="challenge")
data class Challenge(
    @PrimaryKey(autoGenerate =true)
    val id:Int = 0,
    val description:String
)
