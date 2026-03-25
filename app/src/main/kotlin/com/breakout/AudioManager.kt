package com.breakout

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.sin

enum class SoundEvent {
    PADDLE_HIT,
    WALL_HIT,
    BRICK_HIT,
    BALL_LOST,
    WIN
}

class AudioManager(context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val sounds = mutableMapOf<SoundEvent, Int>()
    @Volatile var isMuted: Boolean = false
        private set

    private val cacheDir = context.cacheDir

    init {
        // Write WAV files on a background thread to avoid main-thread disk I/O
        Thread {
            SoundEvent.entries.forEach { event ->
                val pcm = generatePcm(event)
                val wavFile = writeTempWav(pcm, event.name)
                val soundId = soundPool.load(wavFile.absolutePath, 1)
                synchronized(sounds) { sounds[event] = soundId }
            }
        }.apply { isDaemon = true; start() }
    }

    fun play(event: SoundEvent) {
        if (isMuted) return
        val soundId = synchronized(sounds) { sounds[event] } ?: return
        if (soundId > 0) soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    fun toggleMute() {
        isMuted = !isMuted
    }

    fun release() {
        soundPool.release()
    }

    private fun generatePcm(event: SoundEvent): ShortArray {
        return when (event) {
            SoundEvent.PADDLE_HIT -> generateSine(180f, 0.080f)
            SoundEvent.WALL_HIT   -> generateSine(440f, 0.060f)
            SoundEvent.BRICK_HIT  -> generateSineDecay(600f, 0.050f)
            SoundEvent.BALL_LOST  -> generateSine(90f, 0.120f)
            SoundEvent.WIN        -> generateRisingSweep(523f, 1047f, 0.400f)
        }
    }

    private fun generateSine(freqHz: Float, durationSec: Float): ShortArray {
        val sampleCount = (Constants.AUDIO_SAMPLE_RATE * durationSec).toInt()
        return ShortArray(sampleCount) { i ->
            val t = i.toDouble() / Constants.AUDIO_SAMPLE_RATE
            (Short.MAX_VALUE * sin(2.0 * PI * freqHz * t)).toInt().toShort()
        }
    }

    private fun generateSineDecay(freqHz: Float, durationSec: Float): ShortArray {
        val sampleCount = (Constants.AUDIO_SAMPLE_RATE * durationSec).toInt()
        return ShortArray(sampleCount) { i ->
            val t = i.toDouble() / Constants.AUDIO_SAMPLE_RATE
            val envelope = 1.0 - (t / durationSec)
            (Short.MAX_VALUE * sin(2.0 * PI * freqHz * t) * envelope).toInt().toShort()
        }
    }

    private fun generateRisingSweep(startHz: Float, endHz: Float, durationSec: Float): ShortArray {
        val sampleCount = (Constants.AUDIO_SAMPLE_RATE * durationSec).toInt()
        return ShortArray(sampleCount) { i ->
            val t = i.toDouble() / Constants.AUDIO_SAMPLE_RATE
            val freq = startHz + (endHz - startHz) * (t / durationSec)
            (Short.MAX_VALUE * sin(2.0 * PI * freq * t)).toInt().toShort()
        }
    }

    private fun writeTempWav(pcm: ShortArray, name: String): File {
        val file = File(cacheDir, "sound_$name.wav")
        val dataSize = pcm.size * 2
        val totalSize = 44 + dataSize

        FileOutputStream(file).use { fos ->
            val buf = ByteBuffer.allocate(totalSize).order(ByteOrder.LITTLE_ENDIAN)
            buf.put("RIFF".toByteArray())
            buf.putInt(totalSize - 8)
            buf.put("WAVE".toByteArray())
            buf.put("fmt ".toByteArray())
            buf.putInt(16)
            buf.putShort(1)
            buf.putShort(1)
            buf.putInt(Constants.AUDIO_SAMPLE_RATE)
            buf.putInt(Constants.AUDIO_SAMPLE_RATE * 2)
            buf.putShort(2)
            buf.putShort(16)
            buf.put("data".toByteArray())
            buf.putInt(dataSize)
            for (sample in pcm) buf.putShort(sample)
            fos.write(buf.array())
        }
        return file
    }
}
