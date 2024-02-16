package com.example.babyhelper.saund

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import java.util.Random
import kotlin.math.sqrt

class White(private var sampleRate: Int) {
    private var isPlaying = false
    private lateinit var audioTrack: AudioTrack
    private lateinit var buffer: ShortArray
    private var noiseThread: Thread? = null

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
        val random = Random()

        audioTrack.play()

        noiseThread = Thread {
            while (isPlaying) {
                for (i in buffer.indices) {
                    buffer[i] =
                        (random.nextGaussian() * sqrt(2.0) * Short.MAX_VALUE).toInt().toShort()
                }
                if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    audioTrack.write(buffer, 0, buffer.size)
                }
            }
        }
        noiseThread?.start()

        isPlaying = true
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
