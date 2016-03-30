package libs.com.rogerio.utils;
/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class ObjetosImg {
	protected Drawable img;
	public Vector2 position;
	protected int objH,objW;
	public float vel=0;
	private boolean good;
	
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public ObjetosImg(Drawable img){
		this.img=img;
		objH = img.getIntrinsicHeight();
		objW = img.getIntrinsicWidth();
		position = new Vector2();
	}
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Drawable getImg() {
		return img;
	}
	
	public void draw(Canvas canvas){
		img.setBounds((int)position.getX(),(int)position.getY(),
				(int)position.getX()+img.getIntrinsicWidth(),(int)position.getY()+img.getIntrinsicHeight());
		img.draw(canvas);
		//invalidate(position.getX(), position.getY(), 
				//position.getX()+objW,position.getY()+objH);
	}
	
	public void moveUp(int mov){
		position.setY(position.getY()-mov);
	}
	
	public void moveDown(int mov){
		position.setY(position.getY()+mov);
	}
	public int getObjH() {
		return objH;
	}
	public int getObjW() {
		return objW;
	}
	
	public boolean collide(ObjetosImg obj){
		return Physics.collide(this, obj);
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
