package com.emranhss.stopwatch1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Watch : AppCompatActivity() {

    private lateinit var timeView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private lateinit var alarmButton: Button // New button

    private lateinit var timerButton: Button  // Timer button




    private var seconds = 0
    private var running = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_watch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views
        timeView = findViewById(R.id.timeView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)
        alarmButton = findViewById(R.id.alarmButton)
        timerButton = findViewById(R.id.timerButton)  // findViewById for timerButton

        // Button listeners
        startButton.setOnClickListener { running = true }
        stopButton.setOnClickListener { running = false }
        resetButton.setOnClickListener {
            running = false
            seconds = 0
            updateTime()
        }

        // Alarm button listener
        alarmButton.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }

        // Timer button listener → open TimerActivity
        timerButton.setOnClickListener {
            val intent = Intent(this, Timer::class.java)
            startActivity(intent)
        }

        // Start the timer
        runTimer()
    }

    private fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                if (running) {
                    seconds++
                    updateTime()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun updateTime() {
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60
        timeView.text = String.format("%d:%02d:%02d", hrs, mins, secs)
    }


}