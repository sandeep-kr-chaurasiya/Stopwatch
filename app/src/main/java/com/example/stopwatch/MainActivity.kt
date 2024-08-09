package com.example.stopwatch

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var elapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.start.setOnClickListener {
            toggleChronometer()
        }

        binding.reset.setOnClickListener {
            resetChronometer()
        }
    }

    private fun toggleChronometer() {
        if (!isRunning) {
            binding.chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
            binding.chronometer.start()
            binding.start.text = "Stop"
            isRunning = true
        } else {
            elapsedTime = SystemClock.elapsedRealtime() - binding.chronometer.base
            binding.chronometer.stop()
            binding.start.text = "Start"
            isRunning = false
        }
    }

    private fun resetChronometer() {
        binding.chronometer.stop()
        elapsedTime = 0
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.start.text = "Start"
        isRunning = false
    }
}