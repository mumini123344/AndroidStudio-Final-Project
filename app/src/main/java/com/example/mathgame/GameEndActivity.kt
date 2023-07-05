package com.example.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GameEndActivity : AppCompatActivity() {
    lateinit var playAgain: Button
    lateinit var exit: Button
    lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_end)

        result = findViewById(R.id.textViewFinalScore)
        playAgain = findViewById(R.id.buttonPlayAgain)
        exit = findViewById(R.id.buttonExit)

        val score = intent.getStringExtra("score")
        result.text = score

        playAgain.setOnClickListener {
            val intent = Intent(this@GameEndActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // to close GameEnd activity
        }

        exit.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}