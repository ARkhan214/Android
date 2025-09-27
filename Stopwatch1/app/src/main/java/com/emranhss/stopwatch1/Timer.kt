package com.emranhss.stopwatch1

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

class Timer : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var etHour: EditText
    private lateinit var etMinute: EditText
    private lateinit var etSecond: EditText
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var totalTimeInMillis: Long = 0
    private var remainingTimeInMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind views
        tvTimer = findViewById(R.id.tvTimer)
        etHour = findViewById(R.id.etHour)
        etMinute = findViewById(R.id.etMinute)
        etSecond = findViewById(R.id.etSecond)
        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        // Button listeners
        btnStart.setOnClickListener { startInputTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startInputTimer() {
        if (!isRunning) {
            val h = etHour.text.toString().toLongOrNull() ?: 0
            val m = etMinute.text.toString().toLongOrNull() ?: 0
            val s = etSecond.text.toString().toLongOrNull() ?: 0

            totalTimeInMillis = (h * 3600 + m * 60 + s) * 1000
            if (totalTimeInMillis <= 0) {
                Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show()
                return
            }
            remainingTimeInMillis = totalTimeInMillis
            startTimer()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                updateTimerText()
            }
            override fun onFinish() {
                isRunning = false
                tvTimer.text = "00:00:00"
            }
        }.start()
        isRunning = true
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        remainingTimeInMillis = totalTimeInMillis
        updateTimerText()
        isRunning = false
    }

    private fun updateTimerText() {
        val hours = (remainingTimeInMillis / 1000) / 3600
        val minutes = ((remainingTimeInMillis / 1000) % 3600) / 60
        val seconds = (remainingTimeInMillis / 1000) % 60
        tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
