package com.example.squeezemybrain.data.network

import com.example.squeezemybrain.data.network.model.SessionToken
import com.example.squeezemybrain.data.model.*
import com.example.squeezemybrain.data.network.model.Category
import com.example.squeezemybrain.data.network.model.CategoryQuestionCount
import com.example.squeezemybrain.data.network.model.Questions
import com.example.squeezemybrain.ui_.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api_token.php")
    suspend fun getSessionToken(@Query("command") command : String = "request") : SessionToken

    @GET("api_category.php")
    suspend fun getCategory() : Category

    @GET("api_count.php")
    suspend fun getCategoryCount(@Query("category") categoryId : Int) : CategoryQuestionCount

    @GET("api.php")
    suspend fun getQuestions(@Query("amount") amount : Int = Constant.QUESTION_LIMIT,
                     @Query("category") categoryId: Int,
                     @Query("difficulty") difficulty : String,
                     @Query("type") questionType : String,
                     @Query("token") token : String) : Questions
}


/*

get session token
https://opentdb.com/api_token.php?command=request

* Get the quiz categories
https://opentdb.com/api_category.php

* Get the count of questions for particular category
https://opentdb.com/api_count.php?category=CATEGORY_ID_HERE


* Get questions
https://opentdb.com/api.php?amount=10&category=18&difficulty=easy&type=boolean

	amount - number of questions
	category - category of questions (id)
	difficulty - (easy, medium, hard)
	type - (true/false) --> boolean, multiple

 */