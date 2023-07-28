package com.example.squeezemybrain

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.squeezemybrain.navigation.NavDestinations
import com.example.squeezemybrain.ui.theme.SqueezeMyBrainTheme
import com.example.squeezemybrain.ui_.QuestionViewModel
import com.example.squeezemybrain.ui_.category_screen.categoryList
import com.example.squeezemybrain.ui_.category_screen.goScreen
import com.example.squeezemybrain.ui_.question_screen.questionScreen
import com.example.squeezemybrain.ui_.quiz_complete_screen.quizCompletedScreen
import com.example.squeezemybrain.ui_.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SqueezeMyBrainTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                com.example.squeezemybrain.splashScreen(navController)
            }
        }
    }
}

@Composable
fun splashScreen(navController: NavHostController) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxSize()
    ) {

        var rotationValue by remember{
            mutableStateOf(50)
        }
        val imgRotate by produceState(initialValue = 0f){
            var rotationTimes = 200
            while (rotationValue > 0){
                if (value >= 360)
                    value = 0f
                delay(1)
                value = value + rotationValue

                rotationTimes--
                if (rotationTimes == 0){
                    rotationValue -= 20
                    rotationTimes = 15
                }
            }
            value = 0f
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(painter = painterResource(id = R.drawable.brain),
                contentDescription = "brain",
                modifier = Modifier
                    .rotate(imgRotate)
                    .size(150.dp))

            Text(text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall
                    .copy(
                        fontStyle = FontStyle.Italic
                    )
            )
        }
        if (rotationValue <= 0){
            mainStartingScreen(navController)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainStartingScreen(navController: NavHostController) {

    val scope = rememberCoroutineScope()
    val questionViewModel: QuestionViewModel = viewModel()

    // whether to show topBar or progressIndicator
    // if the question screen is visible then show the progressIndicator or else topBar
    var questionProgress by remember {
        mutableStateOf(0f)
    }

    var animateProgress = animateFloatAsState(
        targetValue = questionProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
        .value

    Scaffold(
        topBar = {

            if (questionProgress > 0) {
                Log.e("Dhaval", "LINEAR PROGRESS", )

                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    progress = animateProgress,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    modifier = Modifier,
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        },
    ) { it ->
        navigation(
            navController = navController,
            modifier = Modifier.padding(it),
            scope = scope,
            viewModel = questionViewModel,
            onProgress = {progress : Int ->
                if (progress == 0)
                    questionProgress = 0f
                else
                    questionProgress += (Constant.QUESTION_LIMIT.toFloat() / 100f)

                Log.e("Dhaval", "QUE PROGRESS : ${questionProgress}", )
            })
    }
}


@Composable
fun navigation(
    navController: NavHostController,
    modifier: Modifier,
    scope: CoroutineScope,
    viewModel: QuestionViewModel,
    onProgress: (Int) -> Unit
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = NavDestinations.CATEGORY_SCREEN.name
    ) {

        composable(route = NavDestinations.CATEGORY_SCREEN.name)
        {

            Log.e("Dhaval", "navigation: CATEGORY PROGRESS CALL", )
            onProgress.invoke(0)
            categoryList(
                scope = scope,
                viewmodel = viewModel,
                modifier = modifier,
                onCategoryClicked = { id ->
                    Log.e("Dhaval", "navigation: ${id}")
                    navController.navigate(route = "${NavDestinations.GO_SCREEN.name}/${id}"){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = "${NavDestinations.GO_SCREEN.name}/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType }
            )
        )
        { it ->
            val categoryId = it.arguments?.getInt("categoryId")
            Log.e("Dhaval", "navigation: categoryId : ${categoryId}")

            Log.e("Dhaval", "navigation: GO SCREEN PROGRESS CALL", )
            onProgress.invoke(0)
            LaunchedEffect(categoryId) {
                scope.launch {
                    categoryId?.let {
                        viewModel.getCategoryQuestionCount(categoryId)
                    }
                }
            }

            viewModel.deleteAllQuestions()

            goScreen(
                modifier = modifier,
                categoryQuestionCount = viewModel._categoryCountList.component1(),
                onDifficultyLevelClicked = { difficultyLevel, id ->
//                      onDifficultyLevelClicked(id, difficultyLevel)

                    Log.e("Dhaval", "navigation: id : ${id} -- difficulty : ${difficultyLevel}")
                    scope.launch {
                        viewModel.getQuestions(
                            categoryId1 = id,
                            difficulty1 = difficultyLevel,
                            questionType = "multiple"/*, token = "dljdgl4546765ffgdgf"*/
                        )

                        Log.e("Dhaval", "navigation: CALLING FROM GO SCREEN")
                    }
                    navController.navigate("${NavDestinations.QUESTION_SCREEN.name}/$id/$difficultyLevel"){
                        launchSingleTop = true
                        navController.popBackStack()
                    }
                })
        }



        composable(route = "${NavDestinations.QUESTION_SCREEN.name}/{category_id}/{difficulty_level}",
            arguments = listOf(
                navArgument("category_id") { type = NavType.IntType },
                navArgument("difficulty_level") { type = NavType.StringType }
            ))
        {
            Log.e("Dhaval", "navigation: QUESTION SCREEN CALLING ")
            val categoryId: Int = it.arguments!!.getInt("category_id", 0)
            val difficultyLevel: String = it.arguments!!.getString("difficulty_level", "")


            LaunchedEffect(Unit) {
                viewModel.setCategoryAndDifficulty(categoryId, difficultyLevel)
            }

            questionScreen(
                viewModel = viewModel, modifier = modifier,
                onProgress = {onProgress(1)},
                onRedirectToCompleteScreen = { result, categoryId, difficulty ->
                    Log.e("Dhaval", "navigation: result : ${result}", )
                    navController.navigate("${NavDestinations.COMPLETED_SCREEN.name}/$result/${categoryId}/${difficulty}"){
                        launchSingleTop = true
                        navController.popBackStack()

                    }
                }
            )

        }


        composable(route = "${NavDestinations.COMPLETED_SCREEN.name}/{result}/{categoryId}/{difficulty}",
            arguments = listOf(
                navArgument("result"){ type = NavType.IntType },
                navArgument("categoryId"){ type = NavType.IntType },
                navArgument("difficulty"){ type = NavType.StringType },
            )
        ) {
            Log.e("Dhaval", "navigation: COMPLETE SCREEN PROGRESS CALL", )

            Log.e("Dhaval", "navigation: COMPLETE SCREEN")
            val result = it.arguments?.getInt("result", 0)!!
            val categoryId: Int = it.arguments!!.getInt("categoryId", 0)
            val difficultyLevel: String = it.arguments!!.getString("difficulty", "")

            onProgress.invoke(0)
            quizCompletedScreen(
                modifier = modifier,
                result = result,
                onShowAnswers = {
                    navController.navigate("${NavDestinations.QUESTION_SCREEN.name}/$categoryId/$difficultyLevel"){
                        launchSingleTop = true
                    }
                    Log.e("Dhaval", "navigation: ON SHOW ANSWER CALLED", )
                },
                onTryAgain = {
                    // show questions again but clear the DB so it will be fresh start
                    viewModel.deleteAllQuestions()
                    navController.navigate("${NavDestinations.GO_SCREEN.name}/$categoryId"){
                        launchSingleTop = true
                        navController.popBackStack()
                    }
                },
                onClose = {
                    viewModel.deleteAllQuestions()
                    navController.navigate(NavDestinations.CATEGORY_SCREEN.name){
                        launchSingleTop = true
                        navController.popBackStack()
                    }
                })
        }
    }

    DisposableEffect(currentRoute) {
        onDispose {
            navBackStackEntry?.arguments?.clear()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SqueezeMyBrainTheme {
//        mainStartingScreen()
        splashScreen(navController = rememberNavController())
    }
}