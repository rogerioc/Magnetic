package libs.com.rogerio.utils;
/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */
import java.util.List;


import android.graphics.Canvas;


public class ObjImgAnim extends ImgAnimation {
	public ObjImgAnim(List<ObjetosImg> objs){
		super(objs);
	}

	@Override
	public ObjetosImg getImg() {
		if(imgO==null)
			super.getObj();
		return super.getImg();
	}

	@Override
	public void draw(Canvas canvas) {
		super.getObj();
		super.draw(canvas);
	}

}
