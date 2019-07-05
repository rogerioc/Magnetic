package libs.com.rogerio.utils

import android.content.Context
import android.media.AudioManager

open class Sounds(ctx: Context) {
    protected var leftVolume: Float = 0.toFloat()
    protected var rightVolume: Float = 0.toFloat()
    internal var manager: AudioManager

    init {
        manager = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    protected fun volume() {
        val curVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        leftVolume = curVolume / maxVolume
        rightVolume = curVolume / maxVolume
    }

}