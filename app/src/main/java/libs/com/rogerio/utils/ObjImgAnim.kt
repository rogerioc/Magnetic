package libs.com.rogerio.utils

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */


import android.graphics.Canvas


class ObjImgAnim(objs: List<ObjetosImg>) : ImgAnimation(objs) {

    override fun getImg(): ObjetosImg {
        if (imgO == null)
            super.getObj()
        return super.getImg()
    }

    override fun draw(canvas: Canvas) {
        super.getObj()
        super.draw(canvas)
    }

}
