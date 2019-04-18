package libs.com.rogerio.utils

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class ObjetosImg(var img: Drawable) {
    var position: Vector2
    var objH: Int = 0
        protected set
    var objW: Int = 0
    var vel = 0f
    var isGood: Boolean = false

    init {
        objH = img.intrinsicHeight
        objW = img.intrinsicWidth
        position = Vector2()
    }

    fun draw(canvas: Canvas) {
        img.setBounds(position.x.toInt(), position.y.toInt(),
                position.x.toInt() + img.intrinsicWidth, position.y.toInt() + img.intrinsicHeight)
        img.draw(canvas)
        //invalidate(position.getX(), position.getY(),
        //position.getX()+objW,position.getY()+objH);
    }

    fun moveUp(mov: Int) {
        position.y = position.y - mov
    }

    fun moveDown(mov: Int) {
        position.y = position.y + mov
    }

    fun collide(obj: ObjetosImg): Boolean {
        return Physics.collide(this, obj)
        /*int wX = position.getX()+objW;
		int h  = position.getY()+objH;

		if(((obj.position.getX()>=position.getX() && obj.position.getX()<wX)
					|| ((obj.position.getX()+obj.objW)>=position.getX() && (obj.position.getX()+obj.objW)<wX))
					&& ((obj.position.getY()>=position.getY()&&obj.position.getY()<=h)
							|| (obj.position.getY()+obj.objH>=position.getY()&&obj.position.getY()<=h))) {
			return true;
		}
		return false;*/
    }

}
