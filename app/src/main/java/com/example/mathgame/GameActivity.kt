package com.example.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    lateinit var textScore: TextView
    lateinit var textLife: TextView
    lateinit var textTime: TextView

    lateinit var textQuestion: TextView
    lateinit var editTextAnswer: EditText

    private lateinit var buttonOk:Button
    lateinit var buttonNext: Button

    var expectedResult: Int = 0
    lateinit var countDownTimer: CountDownTimer;
    private val timeLeftMillis: Long = 30000
    private val timeIntervalMillis: Long = 1000
    private val maxLifeTime: Int = 3
    private var wrongAnswerCount: Int = 0

    lateinit var operationType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        textScore = findViewById(R.id.textViewScore)
        textLife = findViewById(R.id.textViewLife)
        textTime = findViewById(R.id.textViewTime)
        editTextAnswer = findViewById(R.id.editTextAnswer)
        textQuestion = findViewById(R.id.textViewQuestion)

        buttonOk = findViewById(R.id.okButton)
        buttonNext = findViewById(R.id.nextButton)

        operationType = intent.getStringExtra("operationType").toString()
        updateSupportTitle()

        generateQuestion()
        startGameTimer()
        buttonOk.setOnClickListener {
            val inputResultStr = editTextAnswer.text.toString()
            if(inputResultStr == "") {
                Toast.makeText(this, "Please write answer or proceed to next question", Toast.LENGTH_LONG).show()
            }
            else {
                val inputResult = inputResultStr.toInt()

                if(expectedResult == inputResult) {
                    textScore.text = (textScore.text.toString().toInt() + 1).toString()
                    textQuestion.text = "Correct answer!"
                }
                else {
                    wrongAnswerCount += 1
                    textLife.text = (maxLifeTime - wrongAnswerCount).toString()
                    textQuestion.text = "Wrong answer!\nAnswer: $expectedResult"
                }
                pauseTimer()
            }
        }

        buttonNext.setOnClickListener {
            if(wrongAnswerCount == 3) {
                Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG)
                val intent = Intent(this@GameActivity, GameEndActivity::class.java)
                intent.putExtra("score", textScore.text.toString())
                startActivity(intent)
                finish()
            }
            else {
                processNewQuestion()
            }
        }

    }

    private fun processNewQuestion() {
        generateQuestion()
        pauseTimer()
        startGameTimer()
    }

    private fun generateRandomNumber(): Int {
        val minValue = 1
        val maxValue = 50
        return Random.nextInt(minValue, maxValue + 1)
    }

    private fun generateQuestion() {
        val num1 = generateRandomNumber()
        val num2 = generateRandomNumber()
        when (operationType) {
            "add" -> {
                expectedResult = num1 + num2
                textQuestion.text = "$num1 + $num2"
            }
            "sub" -> {
                expectedResult = num1 - num2
                textQuestion.text = "$num1 - $num2"
            }
            "mul" -> {
                expectedResult = num1 * num2
                textQuestion.text = "$num1 * $num2"
            }
        }
        editTextAnswer.text.clear()
    }

    private fun startGameTimer() {
        countDownTimer = object: CountDownTimer(timeLeftMillis, timeIntervalMillis){

            override fun onTick(finishedTime: Long) {
                textTime.text = (finishedTime / 1000).toString()
            }

            override fun onFinish() {
                wrongAnswerCount += 1
                textLife.text = (maxLifeTime - wrongAnswerCount).toString()
                textTime.text = (timeLeftMillis / 1000).toString()
                textQuestion.text = "Time over!\nAnswer: $expectedResult"
            }
        }
        countDownTimer.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
    }

    private fun updateSupportTitle() {
        when (operationType) {
            "add" -> supportActionBar?.title = "Addition"
            "sub" -> supportActionBar?.title = "Substation"
            "mul" -> supportActionBar?.title = "Multiplication"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pauseTimer()
    }
}