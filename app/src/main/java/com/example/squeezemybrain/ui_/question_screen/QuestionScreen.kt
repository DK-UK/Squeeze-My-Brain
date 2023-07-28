package com.example.squeezemybrain.ui_.question_screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.HtmlCompat
import com.example.squeezemybrain.R
import com.example.squeezemybrain.data.local.entity.QuestionEntity
import com.example.squeezemybrain.data.local.entity.TestEntity
import com.example.squeezemybrain.ui.theme.SqueezeMyBrainTheme
import com.example.squeezemybrain.ui.theme.lightGreen
import com.example.squeezemybrain.ui.theme.lightRed
import com.example.squeezemybrain.ui_.QuestionViewModel
import com.example.squeezemybrain.ui_.utils.Utility
import kotlinx.coroutines.delay
import java.util.Random

private var questionLimit: Int = 0
private var questionsToBeSaveInDb : MutableList<QuestionEntity>? = null

@Composable
fun questionScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel,
    onProgress: (Int) -> Unit,
    onRedirectToCompleteScreen: (Int, Int, String) -> Unit
) {

    val questions: List<QuestionEntity> =
        viewModel.allQuestions.observeAsState(initial = emptyList()).value

    LaunchedEffect(Unit) {
        questionsToBeSaveInDb = mutableListOf()
    }

    var correctAnsCount: Int by remember {
        mutableStateOf(0)
    }

    Log.e("Dhaval", "questionScreen: correct Ans Count : ${correctAnsCount}")

    questions.let {
        if (it.isNotEmpty()) {
            questionLimit = questions.size

            var questionCount: Int by remember {
                mutableStateOf(1)
            }

            LaunchedEffect(key1 = questionCount) {
                onProgress.invoke(1)
                Log.e("Dhaval", "questionScreen: count : ${questionCount}")
            }

            val question = it.getOrNull(questionCount - 1)

            if (question != null) {
                // if the user come from complete screen via showAnswer
                // check if user already played the quiz or not

                Surface(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {

                    // if the user reaches to the end of the question
                    // then on the click of next or the timer over's action
                    // show the Quiz completed screen
                    // if possible show their answers along with correct answers also.

                    if (questionCount <= questionLimit) {
                        question(questionEntity = question,
                            questionNumber = questionCount,
                            nextQuestion = { selectedOption ->
                                // if answer checked, store to DB
                                if (selectedOption.trim().isNotEmpty()) {
//                                    viewModel.updateQuestion(
                                        questionsToBeSaveInDb!!.add(question.copy(
                                            user_selected_ans = selectedOption
                                        )
                                        )
//                                    )
                                }

                                // if user reaches to the end of the questions
                                // then redirect to complete screen
                                if (questionCount == it.size) {
                                    // redirect to complete screen

                                    viewModel.updateQuestion(questionsToBeSaveInDb!!)
                                    Log.e("Dhaval", "questionScreen: question : ${question}", )
                                    onRedirectToCompleteScreen.invoke(
                                        correctAnsCount,
                                        question.category,
                                        question.difficulty
                                    )

                                    // update category stats if test completed
                                    viewModel.updateCategory(
                                        TestEntity(
                                            category_id = question.category,
                                            difficulty = question.difficulty,
                                            test_taken = 1,
                                            test_passed = if (correctAnsCount >= 5) 1 else 0,
                                            category_name = ""
                                        )
                                    )
                                }



                                // increase question num with +1
                                questionCount = questionCount + 1
                            },
                            onCorrectAnsClicked = {
                                correctAnsCount += 1
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun question(
    modifier: Modifier = Modifier,
    questionEntity: QuestionEntity,
    questionNumber: Int,
    nextQuestion: (String) -> Unit,
    onCorrectAnsClicked: () -> Unit
) {

    val timer = produceState(initialValue = "1", key1 = questionNumber) {

        if (questionEntity.user_selected_ans == null || questionEntity.user_selected_ans.isEmpty()) {
            for (i in 90 downTo 0) {
                value = Utility.secondsToMinutesSeconds(i)
                delay(1000)
            }
        }
    }

    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {

        // question
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = questionNumber.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.inversePrimary)
                    .padding(10.dp)
            )

            Text(
                text = questionEntity.question,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = modifier
                    .padding(10.dp)
                    .weight(1f)
            )
        }

        // options
        val answers: List<String> = Utility.convertStringToList(questionEntity.answers.trim())

        Log.e(
            "Dhaval",
            "question: correct : ${questionEntity.correct_answer} answers : ${answers} : user_selected_ans : ${questionEntity.user_selected_ans}",
        )

        var selectedOption by remember {
            mutableStateOf(questionEntity.user_selected_ans)
        }

        var selected: (String) -> Unit = {
            selectedOption = it
            if (selectedOption == questionEntity.correct_answer) {
                onCorrectAnsClicked.invoke()
            }
        }

        LaunchedEffect(key1 = questionNumber) {
            if (questionEntity.user_selected_ans.isNotEmpty()) {
                selected.invoke(questionEntity.user_selected_ans)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            answers.forEach {
                var userSelected = questionEntity.user_selected_ans.trim()
                var COLOR = MaterialTheme.colorScheme.primaryContainer
                if (userSelected.isNotEmpty()) {
                    if (it == questionEntity.user_selected_ans) {
                        if (it == questionEntity.correct_answer) {
                            COLOR = lightGreen
                        } else {
                            COLOR = lightRed
                        }
                    } else if (it == questionEntity.correct_answer) {
                        COLOR = lightGreen
                    } else {
                        COLOR = MaterialTheme.colorScheme.primaryContainer
                    }
                }
                questionOption(
                    optionStr = it,
                    selectedOption = selectedOption,
                    bgColor = COLOR,
                    clickedOption = selected,
                    isClickable = userSelected.isEmpty()

                )
            }
        }


        // Timer and Next button
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timer
                if (questionEntity.user_selected_ans == null || questionEntity.user_selected_ans.isEmpty()) {
                    Text(
                        text = "${timer.value} left",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)
                    )
                }

                Button(
                    onClick = {
                        nextQuestion(selectedOption)
                    },
                    enabled = selectedOption.isNotEmpty(),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = if (questionNumber <= questionLimit) "Next" else "Submit")
                }
            }
        }

        if (timer.value == "00:00") {
            LaunchedEffect(timer.value) {
                nextQuestion.invoke(selectedOption)
            }
        }
    }
}

@Composable
fun questionOption(
    modifier: Modifier = Modifier,
    optionStr: String,
    selectedOption: String,
    bgColor: Color,
    clickedOption: (String) -> Unit,
    isClickable : Boolean
) {

    Log.e("Dhaval", "questionOption: color : ${bgColor}")
    Surface(
        color = bgColor,
        shadowElevation = 7.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .clickable(enabled = isClickable) {
                clickedOption(optionStr)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(enabled = isClickable, selected = (optionStr == selectedOption), onClick = {
                clickedOption(optionStr)
            })
            Text(
                text = HtmlCompat.fromHtml(optionStr, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun previewQuestionScreen() {
    SqueezeMyBrainTheme {

//        questionScreen(questions = Questions(1, emptyList()))
        /* question(
             questionEntity = QuestionEntity(id = 0, question = "who is the creator of C?",
                 answers = listOf("Dennis Ritchie", "Dhaval Keni", "alan walker", "dilip joshi").toString(),
                 correct_answer = "Dennis Ritchie",
                 user_selected_ans = "",
                 category = 9,
                 difficulty = "easy"
             ),
             questionNumber = 1,
             nextQuestion = {str->

             }, onCorrectAnsClicked = {}

         )*/
    }
}
