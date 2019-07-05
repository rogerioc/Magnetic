package libs.com.rogerio.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log

class AudioClip(ctx: Context, resID: Int) : Sounds(ctx) {
    private var mPlayer: MediaPlayer? = null
    private val name: String

    private var mPlaying = false
    private var mLoop = false

    private val tag = "AudioClip"

    fun ismPlaying(): Boolean {
        return mPlaying
    }

    init {
        name = ctx.resources.getResourceName(resID)

        volume()

        mPlayer = MediaPlayer.create(ctx, resID)
        mPlayer!!.setVolume(leftVolume, rightVolume)

        mPlayer!!.setOnCompletionListener { mp ->
            mPlaying = false
            if (mLoop) {
                Log.i(tag, "AudioClip loop $name")
                mp.start()
            }
        }
    }

    @Synchronized
    fun play() {
        if (mPlaying) {
            return
        }
        volume()

        if (mPlayer != null) {
            mPlaying = true
            mPlayer!!.setVolume(leftVolume, rightVolume)

            mPlayer!!.start()
        }
    }

    @Synchronized
    fun stop() {
        try {
            mLoop = false
            if (mPlaying) {
                mPlaying = false
                mPlayer!!.pause()
            }

        } catch (e: Exception) {
            Log.d(tag, "AduioClip::stop $name $e")
        }

    }

    @Synchronized
    fun loop() {
        mLoop = true
        mPlaying = true
        mPlayer!!.start()

    }

    fun release() {
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
        }
    }
}
