package com.example.mathgamenew

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var textScore: TextView
    lateinit var textLife: TextView
    lateinit var textTime: TextView

    lateinit var textQuestion: TextView

    lateinit var editTextAnswer: EditText

    lateinit var buttonOk: Button
    lateinit var buttonNext: Button

    lateinit var timer : CountDownTimer
    private val startTimeInMillis : Long = 60000
    private var timeLeftInMillis : Long = startTimeInMillis

    var correctAnswer: Int = 0
    var userScore: Int = 0
    var userLife: Int = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        textScore = findViewById(R.id.currentScore)
        textLife = findViewById(R.id.currentLife)
        textTime = findViewById(R.id.timeCurent)
        textQuestion = findViewById(R.id.questionText)

        editTextAnswer = findViewById(R.id.answerEdit)
        buttonOk = findViewById(R.id.okButton)
        buttonNext = findViewById(R.id.nextButton)

        gameContinue()


        buttonOk.setOnClickListener {
            val inputText = editTextAnswer.text.toString()
            if (inputText.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.emptyInput),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                pauseTimer()
                if (inputText.toInt() == correctAnswer) {
                    userScore += 10
                    textQuestion.text = "Correct"
                    textScore.text = userScore.toString()
                } else {
                    userLife--
                    textQuestion.text = "Wrong"
                    textLife.text = userLife.toString()
                }
            }
        }

        buttonNext.setOnClickListener {
            pauseTimer()
            resetTimes()
            gameContinue()
            editTextAnswer.setText("")

        }
    }

    fun gameContinue() {
        val number1 = Random.nextInt(1, 100)
        val number2 = Random.nextInt(1, 100)
        correctAnswer = number1 + number2

        textQuestion.text = "$number1 + $number2"
        startTimer()
    }

    fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis,1000) {
            override fun onTick(millisUntilFinish: Long) {
                timeLeftInMillis = millisUntilFinish
                updateText()
            }

            override fun onFinish() {
                pauseTimer()
                resetTimes()
                updateText()
                userLife--
                textLife.text = userLife.toString()
                textQuestion.text = "Time is out"

            }

        }.start()
    }

    private fun resetTimes() {
        timeLeftInMillis = startTimeInMillis
        updateText()
    }

    private fun pauseTimer() {
        timer.cancel()
    }

    private fun updateText() {
        textTime.text = String.format(Locale.getDefault(),"%02d", timeLeftInMillis / 1000)
    }
}