package com.example.mathgamenew

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
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
    private val startTimeInMillis : Long = 30000
    private var timeLeftInMillis : Long = startTimeInMillis

    private var correctAnswer: Int = 0
    private var userScore: Int = 0
    var userLife: Int = 3
    private var number1 :Int = 0
    private var number2 = 0


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

                if (inputText.toInt() == correctAnswer) {
                    userScore += 10
                    textQuestion.text = "Correct"
                    textScore.text = userScore.toString()
                    buttonOk.isEnabled = false
                    pauseTimer()
                } else {
                    userLife--
                    textQuestion.text = "Wrong"
                    textLife.text = userLife.toString()
                    Handler(Looper.getMainLooper()).postDelayed({
                        textQuestion.text = "$number1 + $number2"
                    },1500)
                    checkGameOver()
                }
            }
        }

        buttonNext.setOnClickListener {
            pauseTimer()
            resetTimes()

            editTextAnswer.setText("")
            buttonOk.isEnabled = true
            checkGameOver()
            gameContinue()

        }
    }

    fun gameContinue() {
        number1 = Random.nextInt(1, 100)
        number2 = Random.nextInt(1, 100)
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
                checkGameOver()

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

    private fun checkGameOver() {
        if (userLife <= 0) {
            Toast.makeText(applicationContext, "Game over", Toast.LENGTH_LONG).show()
            val intent = Intent(this@GameActivity, ResultActivity::class.java)
            intent.putExtra("score", userScore)
            startActivity(intent)
            finish()
        }
    }
}