package com.emranhss.stopwatch1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var setButton: Button
    private lateinit var stopButton: Button

    private lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alarm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        datePicker = findViewById(R.id.datePicker)
        timePicker = findViewById(R.id.timePicker)
        setButton = findViewById(R.id.setAlarmButton)
        stopButton = findViewById(R.id.stopAlarmButton)


        // Set Alarm
        // Set Alarm
        setButton.setOnClickListener {
            val hour: Int
            val minute: Int
            if (Build.VERSION.SDK_INT >= 23) {
                hour = timePicker.hour
                minute = timePicker.minute
            } else {
                hour = timePicker.currentHour
                minute = timePicker.currentMinute
            }

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, datePicker.year)
            calendar.set(Calendar.MONTH, datePicker.month)
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(this, "Selected date/time is in the past!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            Toast.makeText(
                this,
                "Alarm set for ${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year} $hour:$minute",
                Toast.LENGTH_SHORT
            ).show()
        }



//        setButton.setOnClickListener {
//            val hour: Int
//            val minute: Int
//            if (Build.VERSION.SDK_INT >= 23) {
//                hour = timePicker.hour
//                minute = timePicker.minute
//            } else {
//                hour = timePicker.currentHour
//                minute = timePicker.currentMinute
//            }
//
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, hour)
//            calendar.set(Calendar.MINUTE, minute)
//            calendar.set(Calendar.SECOND, 0)
//            if (calendar.timeInMillis <= System.currentTimeMillis()) {
//                calendar.add(Calendar.DAY_OF_MONTH, 1) // next day if time passed
//            }
//
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            val intent = Intent(this, AlarmReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(
//                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//
//            Toast.makeText(this, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
//        }

        // Stop Alarm (Stop Ringtone)
        stopButton.setOnClickListener {
            if (AlarmReceiver.player != null && AlarmReceiver.player!!.isPlaying) {
                AlarmReceiver.player!!.stop()
                AlarmReceiver.player!!.release()
                AlarmReceiver.player = null
                Toast.makeText(this, "Alarm stopped!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No alarm is ringing!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
