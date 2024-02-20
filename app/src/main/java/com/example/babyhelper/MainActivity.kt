package com.example.babyhelper

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.babyhelper.saund.Pink
import com.example.babyhelper.saund.White

class MainActivity : AppCompatActivity() {

    private var noiseGenerator: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<ImageView>(R.id.playIcon)
        button.setOnClickListener {
            when (noiseGenerator) {
                is White -> {
                    val whiteNoise = noiseGenerator as White
                    if (whiteNoise.isPlaying()) {
                        whiteNoise.stop()
                    } else {
                        whiteNoise.play()
                    }
                }
                is Pink -> {
                    val pinkNoise = noiseGenerator as Pink
                    if (pinkNoise.isPlaying()) {
                        pinkNoise.stop()
                    } else {
                        pinkNoise.play()
                    }
                }
            }
        }

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (noiseGenerator) {
                    is White -> (noiseGenerator as White).setSampleRate(progress)
                    is Pink -> (noiseGenerator as Pink).setSampleRate(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Создание списка для Spinner
        val noises = arrayOf("White", "Pink")

        // Создание адаптера для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, noises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = findViewById<Spinner>(R.id.noiseSpinner)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val noise = parent.getItemAtPosition(position).toString()
                when (noise) {
                    "White" -> {
                        noiseGenerator = White(44100)
                    }
                    "Pink" -> {
                        noiseGenerator = Pink(44100)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //  обработать случай, когда ничего не выбрано
            }
        }
    }
}