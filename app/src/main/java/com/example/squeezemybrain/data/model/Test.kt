package com.example.squeezemybrain.data.model

/*
    External data layer representation of Test
 */
data class Test(
    val id : Int,
    val category_id : Int,
    val category_name : String,
    val difficulty : String,
    val test_taken : Int,
    val test_passed : Int
)
