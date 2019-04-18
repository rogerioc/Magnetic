package libs.com.rogerio.utils


import android.graphics.Canvas
import android.graphics.drawable.Drawable

open class ImgAnimation {

    lateinit var obj: List<ObjetosImg>
    var max: Int = 0
    var ind = 0
    var position: Vector2 = Vector2()
    lateinit var img: Drawable
    lateinit var imgO: ObjetosImg
    var isFinish: Boolean = false

    constructor(objs: List<ObjetosImg>) {
        obj = objs
        max = obj.size
        imgO = obj[0]
        img = imgO.img
        isFinish = false
    }

    constructor() : super() {}

    open fun getImg(): ObjetosImg {
        return imgO
    }

    open fun draw(canvas: Canvas) {

        img.setBounds(position.x.toInt(), position.y.toInt(),
                position.x.toInt() + img.intrinsicWidth, position.y.toInt() + img.intrinsicHeight)
        img.draw(canvas)

    }

    protected fun getObj() {
        getNewImg()

    }

    fun getNewImg() {
        if (ind + 1 == max) isFinish = true
        ind = (ind + 1) % max
        imgO = obj[ind]
        imgO.position = position
        img = imgO.img
    }

    fun moveDown(mov: Int) {
        position.y = position.y + mov
        ind = (ind + 1) % max
        //getObj();

    }

}