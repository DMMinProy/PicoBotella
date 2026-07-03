package com.example.picobotella.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//Este es el model de retos se define una id y la descripcion
@Entity(tableName="challenge")
data class Challenge(
    @PrimaryKey(autoGenerate =true)
    val id:Int = 0,
    val description:String
)