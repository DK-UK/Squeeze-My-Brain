package com.example.squeezemybrain.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.squeezemybrain.data.model.Test

@Entity(tableName = "Test")
class TestEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val category_id : Int,
    val category_name : String,
    val difficulty : String,
    val test_taken : Int,
    val test_passed : Int
)

fun TestEntity.asExternalModel() = Test(
    id = this.id,
    category_id = this.category_id,
    category_name = this.category_name,
    difficulty = this.difficulty,
    test_taken = this.test_taken,
    test_passed = this.test_passed
)