package libs.com.rogerio.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class SpriteSimpleInfoData {
	public int w;
	public int h;
	
	public int x;
	public int y;
	public double speed;
	public boolean kill;
	public float rotate;
	public double deltaX;
	public double deltaY;
	//Trata image
	public int frame;
	public long time=0;
	private Bitmap bmp=null;
	
	private Rect mRect;
	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
		w = getW();
		h=getH();
	}

	public int getH() {
		if(bmp!=null)
			return bmp.getHeight();
		return h;
	}
	
	public int getW() {
		if(bmp!=null)
			return bmp.getWidth();
		return w;
	}
	
	public Rect getRect() {
		return new Rect(x, y, x+getW(), y+getH());
	}
	
}