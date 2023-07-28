package com.example.squeezemybrain.ui_

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.squeezemybrain.data.network.ApiService
import com.example.squeezemybrain.data.QuestionRepository
import com.example.squeezemybrain.data.network.RetrofitHelper
import com.example.squeezemybrain.data.network.model.SessionToken
import com.example.squeezemybrain.data.local.QuizDatabase
import com.example.squeezemybrain.data.local.entity.QuestionEntity
import com.example.squeezemybrain.data.local.entity.TestEntity
import com.example.squeezemybrain.data.network.model.Category
import com.example.squeezemybrain.data.network.model.CategoryQuestionCount
import com.example.squeezemybrain.data.network.model.CategoryQuestionCountX
import com.example.squeezemybrain.data.network.model.Questions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var apiService: ApiService
    private lateinit var repository: QuestionRepository
    private lateinit var database: QuizDatabase
    private lateinit var sessionToken: SessionToken
    companion object {
        private var categoryId: Int = 0
        private var difficulty: String = ""
    }

    private var categoryCountList = mutableStateOf<CategoryQuestionCount>(
        CategoryQuestionCount(
            0,
            CategoryQuestionCountX(0, 0, 0, 0)
        )
    )

    val _categoryCountList: MutableState<CategoryQuestionCount>
        get() = categoryCountList



    init {
        database = QuizDatabase.getDatabase(application)
        apiService = RetrofitHelper.getRetrofit().create(ApiService::class.java)
        repository = QuestionRepository(apiService, database)
        getSessionToken()
    }

    val categoryUi: StateFlow<List<TestEntity>> =
        repository.getAllCategories()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )


    fun getSessionToken() {
        viewModelScope.launch {
            sessionToken = repository.getSessionToken()
        }
    }

    suspend fun getCategory() {
        return repository.getCategory()
    }

    suspend fun getCategoryQuestionCount(categoryId: Int) {
        categoryCountList.value = repository.getCategoryCount(categoryId)
    }

    suspend fun getQuestions(
        categoryId1: Int,
        difficulty1: String,
        questionType: String,
    ) {
        categoryId = categoryId1
        difficulty = difficulty1
        repository.getQuestionsFromApi(
            categoryId = categoryId,
            difficulty = difficulty,
            questionType = questionType ,
            token = sessionToken.token
        )
    }

    // Category DB functions
     fun updateCategory(testModel: TestEntity) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.updateCategory(testModel)
         }
    }

    val _categoryId = MutableStateFlow(0)
    val _difficulty = MutableStateFlow("")

    val allQuestions = combine(_categoryId, _difficulty) { categoryId, difficulty ->
        Pair(categoryId, difficulty)
    }.flatMapLatest { (categoryId, difficulty) ->
        repository.getAllQuestionsFromDB(categoryId, difficulty)
    }.asLiveData()

    fun setCategoryAndDifficulty(categoryId: Int, difficulty: String) {
        _categoryId.value = categoryId
        _difficulty.value = difficulty
    }

    fun updateQuestion(questions: List<QuestionEntity>){
        viewModelScope.launch {
            repository.updateQuestion(questions)
        }
    }

    fun deleteAllQuestions(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllQuestions()
        }
    }
}