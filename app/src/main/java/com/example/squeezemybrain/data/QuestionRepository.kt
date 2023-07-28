package com.example.squeezemybrain.data

import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.squeezemybrain.data.local.QuizDatabase
import com.example.squeezemybrain.data.local.entity.QuestionEntity
import com.example.squeezemybrain.data.local.entity.TestEntity
import com.example.squeezemybrain.data.network.model.Category
import com.example.squeezemybrain.data.network.model.CategoryQuestionCount
import com.example.squeezemybrain.data.network.ApiService
import com.example.squeezemybrain.data.network.model.SessionToken
import com.example.squeezemybrain.ui_.utils.Utility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class QuestionRepository(
    val apiService: ApiService,
    val database: QuizDatabase
) {

    suspend fun getSessionToken(): SessionToken {
        return apiService.getSessionToken()
    }

    //region Category Functions
    suspend fun getCategory() {

        if (database.testDao().getCategoryCount() <= 0) {
            val categories: Category = apiService.getCategory()
            val mappedCategories: List<TestEntity> = categories.trivia_categories.map {
                TestEntity(
                    category_id = it.id,
                    category_name = it.name,
                    difficulty = "",
                    test_taken = 0,
                    test_passed = 0
                )
            }
            insertCategories(mappedCategories)
        }
    }

    suspend fun getCategoryCount(categoryId: Int): CategoryQuestionCount {
        return apiService.getCategoryCount(categoryId)
    }

    suspend fun insertCategories(categories: List<TestEntity>) {
        return database.testDao().insertCategories(categories)
    }

    suspend fun updateCategory(testModel: TestEntity) {
        val category = getCategoryViaId(testModel.category_id)
        Log.e("Dhaval", "REPOSITORY category : ${category.category_id}", )
        Log.e("Dhaval", "TESTMODEL category : ${testModel.category_id}", )

        database.testDao().updateCategory(
            category_id = testModel.category_id,
            difficulty = testModel.difficulty,
            test_taken = (category.test_taken + testModel.test_taken),
            test_passed = (category.test_passed + testModel.test_passed)
        )
    }

    fun getAllCategories(): kotlinx.coroutines.flow.Flow<List<TestEntity>> {
        return database.testDao().getAllCategories()
    }

    suspend fun getCategoryViaId(categoryId : Int) : TestEntity{
        return database.testDao().getCategoryViaId(categoryId)
    }

    //endregion

    //region Question Functions
    suspend fun getQuestionsFromApi(
        categoryId: Int,
        difficulty: String,
        questionType: String,
        token : String
    ) {
        val questionCount = database.quizDao().getQuestionCount(categoryId)

        if (questionCount <= 0) {
            val questions = apiService.getQuestions(
                categoryId = categoryId,
                difficulty = difficulty,
                questionType = questionType,
                token = token
            ).results.map {
                val listOfAnswers: MutableList<String> = mutableListOf<String>().apply {
                    this.addAll(it.incorrect_answers)
                    this.add((0..it.incorrect_answers.size).random(), it.correct_answer)
                }

                QuestionEntity(
                    question = HtmlCompat.fromHtml(it.question,HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                    correct_answer = it.correct_answer,
                    answers = Utility.convertListToString(listOfAnswers),
                    category = categoryId,
                    difficulty = it.difficulty,
                    user_selected_ans = ""
                )
            }
            insertQuestions(questions)
        }
    }

    suspend fun insertQuestions(questions : List<QuestionEntity>){
        database.quizDao().insertQuestions(questions)
    }

    fun getAllQuestionsFromDB(categoryId : Int, difficulty : String) : Flow<List<QuestionEntity>>{
        return database.quizDao().getQuestions(categoryId, difficulty)
    }

    suspend fun updateQuestion(questions: List<QuestionEntity>){
        database.quizDao().updateQuestion(questions)
    }

    suspend fun deleteAllQuestions(){
        database.quizDao().deleteQuestionsFromDB()
    }
    //endregion
}