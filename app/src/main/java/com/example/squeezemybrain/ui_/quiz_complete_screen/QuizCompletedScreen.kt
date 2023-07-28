package com.example.squeezemybrain.ui_.quiz_complete_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.squeezemybrain.ui.theme.lightGreen
import com.example.squeezemybrain.ui.theme.lightRed
import com.example.squeezemybrain.ui_.utils.Constant

@Composable
fun quizCompletedScreen(
    modifier : Modifier = Modifier,
    result : Int,
    onShowAnswers : () -> Unit,
    onTryAgain : () -> Unit,
    onClose : () -> Unit
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {


        val isPassed = result >= Constant.QUESTION_LIMIT / 2
        val value = if(isPassed) "Passed" else "Failed"
        val color = if(isPassed) lightGreen else lightRed

        Column(

        ){

                Text(text = value,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(100.dp)
                        .drawBehind {
                                    drawCircle(
                                        color = color,
                                        radius = this.size.maxDimension - 70
                                    )
                        }
                        .align(alignment = Alignment.CenterHorizontally)
                    ,
                    style = MaterialTheme.typography.displayMedium
                    )

            /* Icon(imageVector = Icons.Filled.Check,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = "Completed icon",
                modifier = Modifier
                    .padding(top = 150.dp)
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(color = Color.White)
                    .align(Alignment.CenterHorizontally)


            )*/

            Text(text = "Completed",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )

            Text(text = "Result : $result/${Constant.QUESTION_LIMIT}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            TextButton(onClick = onShowAnswers,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "show answers",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold)
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(0.1f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = onTryAgain
                ) {
                    Text(text = "Try again")
                }
                TextButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Preview (showSystemUi = true)
@Composable
fun quizCompletedScreen() {
    quizCompletedScreen(
        modifier = Modifier.padding(0.dp),
        result = 5,
        onShowAnswers = {},
        onTryAgain = {},
        onClose = {}
    )
}