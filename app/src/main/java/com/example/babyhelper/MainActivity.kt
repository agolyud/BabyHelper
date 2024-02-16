package com.example.babyhelper

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.babyhelper.saund.White

class MainActivity : AppCompatActivity() {

    private lateinit var whiteNoise: White

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        whiteNoise = White(44100)

        val button = findViewById<Button>(R.id.play_button)
        button.setOnClickListener {
            if (whiteNoise.isPlaying()) {
                whiteNoise.stop()
            } else {
                whiteNoise.play()
            }
        }

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                whiteNoise.setSampleRate(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}