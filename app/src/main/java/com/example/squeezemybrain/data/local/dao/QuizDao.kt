package com.example.squeezemybrain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.squeezemybrain.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert
    public suspend fun insertQuestions(questionModel: List<QuestionEntity>)

    @Update
    public suspend fun updateQuestion(questions: List<QuestionEntity>)

    @Query("select count(id) from questions where category=:categoryId")
    suspend fun getQuestionCount(categoryId: Int) : Int

    @Query("delete from questions")
    suspend fun deleteQuestionsFromDB()

    @Query("select * from questions where category=:categoryId and difficulty=:difficulty")
    fun getQuestions(categoryId : Int, difficulty : String) : Flow<List<QuestionEntity>>
}