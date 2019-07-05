package libs.com.rogerio.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool

import android.util.Log

class SoundFx(ctx: Context, resID: Int) : Sounds(ctx) {

    private val tag = "SoundFx"
    internal var soundPool: SoundPool
    private val soundID: Int
    private val name: String
    private val mPlayer: MediaPlayer? = null

    init {

        name = ctx.resources.getResourceName(resID)
        soundPool = SoundPool(4, AudioManager.STREAM_MUSIC, 0)

        soundID = soundPool.load(ctx, resID, 1)

    }

    fun play() {
        volume()
        if (soundPool.play(soundID, leftVolume, rightVolume, 1, 0, 1f) == 0) {
            Log.e(tag, "Erro estranho")
        }
        return

    }

    fun stop() {
        try {
            soundPool.stop(soundID)

        } catch (e: Exception) {
            Log.d(tag, "AduioClip::stop $name $e")
        }

    }


}
