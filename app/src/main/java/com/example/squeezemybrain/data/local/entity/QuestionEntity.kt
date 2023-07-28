package com.example.squeezemybrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val question : String,
    val  answers : String, // listOf answers converted to string for storing in local DB
    val correct_answer : String,
    val user_selected_ans : String,
    val category : Int,
    val difficulty : String
)

