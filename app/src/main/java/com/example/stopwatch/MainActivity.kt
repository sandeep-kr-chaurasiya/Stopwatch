package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var startTime: Long = 0
    private var passedTime: Long = 0
    private val lapTimes = mutableListOf<String>()
    private var lapCount = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.start.setOnClickListener {
            if (!isRunning) {
                startChronometer()
            } else {
                stopChronometer()
            }
        }
        binding.reset.setOnClickListener {
            resetChronometer()
        }
        binding.lap.setOnClickListener {
            addLap()
        }
    }
    private fun startChronometer() {
        startTime = SystemClock.elapsedRealtime() - passedTime
        handler.post(updateChronometer)
        binding.start.text = "Stop"
        isRunning = true
    }
    private fun stopChronometer() {
        passedTime = SystemClock.elapsedRealtime() - startTime
        handler.removeCallbacks(updateChronometer)
        binding.start.text = "Start"
        isRunning = false
    }
    private val updateChronometer = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedMillis = SystemClock.elapsedRealtime() - startTime
                binding.chronometer.text = formatTimeWithMillis(elapsedMillis)
                handler.postDelayed(this, 10) // Update every 10 milliseconds
            }
        }
    }

    private fun resetChronometer() {
        stopChronometer()
        passedTime = 0L
        binding.chronometer.text = formatTimeWithMillis(0L)
        lapTimes.clear()
        lapCount = 0
        binding.lapDisplay.text = ""
    }

    private fun formatTimeWithMillis(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        val milliseconds = (timeInMillis % 1000) / 10
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }

    private fun addLap() {
        if (isRunning) {
            lapCount++
            val lapTime = SystemClock.elapsedRealtime() - startTime
            val formattedTime = formatTimeWithMillis(lapTime)
            lapTimes.add("Lap $lapCount: $formattedTime")
            updateLapDisplay()
        }
    }

    private fun updateLapDisplay() {
        val lapsText = lapTimes.joinToString("\n")
        binding.lapDisplay.text = lapsText
    }
}