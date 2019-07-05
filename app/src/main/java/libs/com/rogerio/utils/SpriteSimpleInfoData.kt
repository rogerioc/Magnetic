package libs.com.rogerio.utils

import android.graphics.Bitmap
import android.graphics.Rect

class SpriteSimpleInfoData {
    var w: Int = 0
    var h: Int = 0

    var x: Int = 0
    var y: Int = 0
    var speed: Double = 0.toDouble()
    var kill: Boolean = false
    var rotate: Float = 0.toFloat()
    var deltaX: Double = 0.toDouble()
    var deltaY: Double = 0.toDouble()
    //Trata image
    var frame: Int = 0
    var time: Long = 0
    var bmp: Bitmap? = null
        set(bmp) {
            field = bmp
            w = getWidth()
            h = getHeight()
        }

    private val mRect: Rect? = null

    val rect: Rect
        get() = Rect(x, y, x + getWidth(), y + getHeight())

    fun getHeight(): Int {
        return if (this.bmp != null) this.bmp!!.height else h
    }

    fun getWidth(): Int {
        return if (this.bmp != null) this.bmp!!.width else w
    }

}