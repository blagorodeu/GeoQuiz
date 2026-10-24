package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoquiz.ui.theme.GeoQuizTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoQuizTheme {
                QuizScreen()
            }
        }
    }
}

// Класс для хранения вопросов с метками true или false
private data class Question(
    val text: String,
    val answerTrue: Boolean
)

// Список вопросов с метками true или false
private val questionList = listOf(
    Question("Canberra is the capital of Australia.", true),
    Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
    Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
    Question("The source of the Nile River is in Egypt.", false),
    Question("The Amazon River is the longest river in the Americas.", true),
    Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
)

// Вывод количества правильных ответов
fun resultMessage(correct: Int, total: Int): String =
    "Правильных ответов: " + correct + " из " + total

@Composable
fun QuizScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }
    var correctCount by remember { mutableStateOf(0) }
    val currentQuestion = questionList[currentIndex]
    var answeredCurrent by remember { mutableStateOf(false) }

    // Показ всплывающей панели с выводом количества правильных ответов
    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    fun handleAnswer(userPressedTrue: Boolean) {
        if (answeredCurrent)
            return
        answeredCurrent = true
        val isCorrect = (userPressedTrue == currentQuestion.answerTrue)
        if (isCorrect)
        {
            correctCount++
            showMessage("Верно!")
        }
        else
        {
            showMessage("Неверно!")
        }
    }

    // Обработка перехода к следующему вопросу
    fun handleNext() {
        currentIndex = (currentIndex + 1) % questionList.size
        answeredCurrent = false
    }

    // Построение UI
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentQuestion.text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            Row(
                modifier = Modifier.padding(bottom = 24.dp)
                    .width(1000.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    onClick = { handleAnswer(true)},
                    shape = RectangleShape
                ) {
                    Text("TRUE", fontSize = 28.sp)
                }
                Button(
                    onClick = { handleAnswer(false)},
                    shape = RectangleShape
                ) {
                    Text("FALSE", fontSize = 28.sp)
                }
            }

            Button(onClick = { handleNext()},
                shape = RectangleShape,
                modifier = Modifier.padding(top=10.dp)
                    .align(Alignment.End)) {
                Text("NEXT", fontSize = 28.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizPreview() {
    GeoQuizTheme {
        QuizScreen()
    }
}