package com.example.environmentmonitor.data.sensor

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import javax.inject.Inject
import kotlin.math.log10

class AudioMeter @Inject constructor(
    private val context: Context
) {
    private var recorder: MediaRecorder? = null

    fun start() {
        try {
            recorder = MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                // zapis do "pustki", bo ważna jest tylko amplituda
                setOutputFile(File(context.cacheDir, "temp_audio.3gp").path)

                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            recorder = null
        }
    }

    fun getAmplitude(): Double {
        return recorder?.maxAmplitude?.toDouble() ?: 0.0
    }

    fun getDecibels(): Double {
        val amp = getAmplitude()
        return if (amp > 0) 20 * log10(amp) else 0.0
    }

    fun stop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}