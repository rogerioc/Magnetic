package libs.com.rogerio.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect


/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */
object Physics {
    private val g = 0.9f

    fun gravity(obj: ObjetosImg) {
        if (obj.vel < 55.5)
            obj.vel += g
        obj.position.y = obj.position.y + obj.vel.toInt()
    }

    fun collide(obj1: ObjetosImg, obj2: ObjetosImg): Boolean {
        val wX = (obj1.position.x + obj1.objW).toInt()
        val h = (obj1.position.y + obj1.objH).toInt()
        val dimension = obj2.position.x >= obj1.position.x && obj2.position.x < wX || obj2.position.x + obj2.objW >= obj1.position.x && obj2.position.x + obj2.objW < wX

        return if (dimension && (obj2.position.y >= obj1.position.y && obj2.position.y <= h
                        || obj2.position.y + obj2.objH >= obj1.position.y && obj2.position.y <= h
                        || obj2.position.y >= obj1.position.y + obj1.objH)) {
            true
        } else false


    }

    /**
     * Verifica se a cor n�o � transparente
     * @param pixel
     * @return
     */
    private fun isFilled(pixel: Int): Boolean {
        return pixel != Color.TRANSPARENT
    }

    fun collisionPerPixel(obj1: SpriteSimpleInfoData, obj2: SpriteSimpleInfoData): Boolean {

        if (collide(obj1, obj2)) {
            val collisionBounds = getCollisionBounds(obj1.rect, obj2.rect)
            for (i in collisionBounds.left until collisionBounds.right) {
                for (j in collisionBounds.top until collisionBounds.bottom) {
                    val sprite1Pixel = getBitmapPixel(obj1, i, j)
                    val sprite2Pixel = getBitmapPixel(obj1, i, j)
                    if (isFilled(sprite1Pixel) && isFilled(sprite2Pixel)) {
                        return true
                    }
                }
            }

        }
        return false
    }

    private fun getBitmapPixel(sprite: SpriteSimpleInfoData, i: Int, j: Int): Int {
        return sprite.bmp!!.getPixel(i - sprite.x, j - sprite.y)
    }


    private fun getCollisionBounds(rect1: Rect, rect2: Rect): Rect {
        val left = Math.max(rect1.left, rect2.left)
        val top = Math.max(rect1.top, rect2.top)
        val right = Math.min(rect1.right, rect2.right)
        val bottom = Math.min(rect1.bottom, rect2.bottom)
        return Rect(left, top, right, bottom)
    }


    fun collide(obj1: SpriteSimpleInfoData, obj2: SpriteSimpleInfoData): Boolean {

        return if (obj1.rect.intersect(obj2.rect)) true else false


    }

    fun collide(obj1: Sprite, obj2: Sprite): Boolean {
        val w = (obj1.position!!.x + obj1.width).toInt()
        val h = (obj1.position!!.y + obj1.height).toInt()

        val dimension = obj2.position!!.x >= obj1.position!!.x && obj2.position!!.x < w || obj2.position!!.x + obj2.width >= obj1.position!!.x && obj2.position!!.x + obj2.width < w

        return if (dimension && (obj2.position!!.y >= obj1.position!!.y && obj2.position!!.y <= h || obj2.position!!.y + obj2.height >= obj1.position!!.y && obj2.position!!.y + obj2.height <= h)) {
            true
        } else false


    }

    fun collide(obj1: Sprite, obj2: Vector2): Boolean {
        val wX = (obj1.position!!.x + obj1.width).toInt()
        val h = (obj1.position!!.y + obj1.height).toInt()
        val dimension = obj2.x >= obj1.position!!.x && obj2.x < wX


        return if (dimension && obj2.y >= obj1.position!!.y && obj2.y <= h) {
            true
        } else false


    }

}
