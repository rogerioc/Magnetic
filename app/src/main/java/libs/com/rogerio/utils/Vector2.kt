package libs.com.rogerio.utils

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */
class Vector2 {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor() {
        this.x = 0f
        this.y = 0f
    }

    fun normal(pos: Vector2): Vector2 {
        val v1 = Vector2()
        val len: Double
        /* Encontra vetor v1 */
        v1.x = pos.x - x
        v1.y = pos.y - y
        len = Math.sqrt((v1.x * v1.x + v1.y * v1.y).toDouble())
        v1.x /= len.toFloat()
        v1.y /= len.toFloat()
        return v1

    }

    fun coefAng(pos: Vector2): Float {
        val v1 = Vector2()
        v1.x = pos.x - x
        v1.y = pos.y - y
        return v1.y / v1.x
    }

    companion object {

        fun Zero(): Vector2 {
            return Vector2()
        }
    }

}
