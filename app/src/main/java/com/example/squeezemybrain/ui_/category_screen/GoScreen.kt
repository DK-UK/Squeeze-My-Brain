package com.example.squeezemybrain.ui_.category_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.squeezemybrain.R
import com.example.squeezemybrain.data.network.model.CategoryQuestionCount
import com.example.squeezemybrain.data.network.model.CategoryQuestionCountX
import com.example.squeezemybrain.ui.theme.SqueezeMyBrainTheme
import com.example.squeezemybrain.ui_.QuestionViewModel
import com.example.squeezemybrain.ui_.utils.Constant

@Composable
fun goScreen(
    categoryQuestionCount : CategoryQuestionCount,
    modifier : Modifier = Modifier,
    onDifficultyLevelClicked: (String, Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.inversePrimary,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),

        ){
            Text(text = "Hey Welcome to ${stringResource(id = R.string.app_name)} and find out how much you can squeeze it yey!!",
                style = MaterialTheme.typography.titleLarge
                    .copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )

            Text(text = "Instructions : ",
                modifier = Modifier.padding(horizontal = 5.dp,
                    vertical = 10.dp),
                style = MaterialTheme.typography.titleLarge
                    .copy(fontWeight = FontWeight.Bold))

            listOf("1. There are ${Constant.QUESTION_LIMIT} number of questions and all are mandatory.",
                "2. You will be given ${Constant.TIME_LIMIT_IN_SECS} of time to complete the test.",
                "3. Choose the difficulty level according to your intelligence.",
                "4. You need to score ${Constant.QUESTION_LIMIT / 2} out of ${Constant.QUESTION_LIMIT} questions to pass.").forEach {
                showInstructions(instruction = it)
            }

            Text(text = "Please choose your difficulty level",
                style = MaterialTheme.typography.bodyLarge
                    .copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(10.dp))

            questionDifficulty(categoryQuestion = categoryQuestionCount, onDifficultyLevelClicked = onDifficultyLevelClicked)
        }
    }
}

@Composable
fun showInstructions(
    instruction : String
) {
    Text(text = instruction,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 10.dp,
            vertical = 2.dp))
}

@Composable
fun questionDifficulty(
    categoryQuestion: CategoryQuestionCount,
    onDifficultyLevelClicked: (String, Int) -> Unit
) {

    Column() {
        Surface(
            shadowElevation = 7.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .clickable {
                    onDifficultyLevelClicked("easy", categoryQuestion.category_id)
                },
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Easy",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = categoryQuestion.category_question_count.total_easy_question_count.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Surface(
            shadowElevation = 7.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onDifficultyLevelClicked("medium", categoryQuestion.category_id)
                },
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Medium",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = categoryQuestion.category_question_count.total_medium_question_count.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Surface(
            shadowElevation = 7.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onDifficultyLevelClicked("hard", categoryQuestion.category_id)
                },
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Hard",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = categoryQuestion.category_question_count.total_hard_question_count.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun goScreenPrev() {
    SqueezeMyBrainTheme {

        goScreen(CategoryQuestionCount(1, CategoryQuestionCountX(100,50, 80, 230)), onDifficultyLevelClicked =  {it1, it2->

        })
    }
}