package com.example.babyhelper.saund

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import java.util.Random
import kotlin.math.sqrt

class Pink(private var sampleRate: Int) {
    private var isPlaying = false
    private lateinit var audioTrack: AudioTrack
    private lateinit var buffer: ShortArray
    private var noiseThread: Thread? = null
    private val random = Random()
    private var b0: Double = 0.0
    private var b1: Double = 0.0
    private var b2: Double = 0.0
    private var b3: Double = 0.0
    private var b4: Double = 0.0
    private var b5: Double = 0.0
    private var b6: Double = 0.0

    fun play() {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize,
            AudioTrack.MODE_STREAM
        )

        buffer = ShortArray(minBufferSize)

        audioTrack.play()

        noiseThread = Thread {
            while (isPlaying) {
                for (i in buffer.indices) {
                    buffer[i] = generatePinkNoise().toShort()
                }
                if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    audioTrack.write(buffer, 0, buffer.size)
                }
            }
        }
        noiseThread?.start()

        isPlaying = true
    }

    private fun generatePinkNoise(): Int {
        val white = random.nextGaussian() * sqrt(2.0)

        b0 = 0.99886 * b0 + white * 0.0555179
        b1 = 0.99332 * b1 + white * 0.0750759
        b2 = 0.96900 * b2 + white * 0.1538520
        b3 = 0.86650 * b3 + white * 0.3104856
        b4 = 0.55000 * b4 + white * 0.5329522
        b5 = -0.7616 * b5 - white * 0.0168980
        val pink = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362
        b6 = white * 0.115926

        return (pink * 0.11 * Short.MAX_VALUE).toInt()
    }

    fun stop() {
        isPlaying = false
        noiseThread?.join()  // Wait for the thread to finish
        audioTrack.stop()
        audioTrack.release()
    }

    fun setSampleRate(sampleRate: Int) {
        this.sampleRate = sampleRate
        if (isPlaying) {
            stop()
            play()
        }
    }

    fun isPlaying() = isPlaying
}