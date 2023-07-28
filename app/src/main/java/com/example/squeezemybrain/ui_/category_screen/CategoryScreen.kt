package com.example.squeezemybrain.ui_.category_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.squeezemybrain.data.local.entity.TestEntity
import com.example.squeezemybrain.ui_.QuestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun categoryList(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    viewmodel: QuestionViewModel,
    onCategoryClicked: (Int) -> Unit
) {

    LaunchedEffect(Unit) {
        scope.launch {
            viewmodel.getCategory()
        }
    }

    val categories = viewmodel.categoryUi.collectAsState(initial = emptyList()).value

    Log.e("Dhaval", "categoryList: categories : ${categories}", )

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Choose Category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(10.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp)
        ) {
            /*items(viewmodel._categoryList.component1().trivia_categories){
                categoryItem(categoryTitle = it.name,
                    onCategoryClicked = {
                        onCategoryClicked(it.id)
                    })
            }*/

            items(categories) {
                Log.e("Dhaval", "categoryList: name : ${it.category_name} -- taken : ${it.test_taken} -- passed : ${it.test_passed}", )
                categoryItem(category = it,
                    onCategoryClicked = {
                        onCategoryClicked(it.category_id)
                    })
            }
        }

    }
}

@Composable
fun categoryItem(
    category: TestEntity,
    onCategoryClicked: () -> Unit
) {
    Surface(
        shadowElevation = 7.dp,
        tonalElevation = 7.dp,
        shape = MaterialTheme.shapes.medium.copy(topStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable(onClick = onCategoryClicked)
            .clipToBounds()
            .padding(15.dp)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.category_name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(25.dp)
            )

            // Test Results
          /*  if (category.test_taken > 0) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.inversePrimary)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Taken : ${category.test_taken}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "Passed : ${category.test_passed}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }*/
        }
    }
}

@Preview
@Composable
fun categoryItem() {
    categoryItem(TestEntity(0, 1, "general", "easy",
        10, 5), {

    })
}