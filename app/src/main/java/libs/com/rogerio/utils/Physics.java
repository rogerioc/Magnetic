package libs.com.rogerio.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;


/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */
public class Physics {
	static private float g=0.9f;
	
	static public  void gravity(ObjetosImg obj){
		if(obj.vel<55.5)
			obj.vel += g;
		obj.position.setY(obj.position.getY()+(int)obj.vel);
	}
	static public boolean collide(ObjetosImg obj1,ObjetosImg obj2){
		int wX = (int) (obj1.position.getX()+obj1.getObjW());
		int h  = (int) (obj1.position.getY()+obj1.getObjH());
		boolean dimension = ((obj2.position.getX()>=obj1.position.getX() && obj2.position.getX()<wX)
				|| ((obj2.position.getX()+obj2. getObjW())>=obj1.position.getX() && (obj2.position.getX()+obj2.objW)<wX));
		
		if(dimension && ((obj2.position.getY()>=obj1.position.getY()&&obj2.position.getY()<=h)
					|| (obj2.position.getY()+obj2.getObjH()>=obj1.position.getY()&&obj2.position.getY()<=h)
					|| (obj2.position.getY()>=(obj1.position.getY()+obj1.getObjH())))){
			return true;
		}
		
		
		
		return false;
	}
	
	/**
	 * Verifica se a cor n�o � transparente
	 * @param pixel
	 * @return
	 */
	private static boolean isFilled(int pixel) {
	    return pixel != Color.TRANSPARENT;
	}
	
	static public boolean collisionPerPixel(SpriteSimpleInfoData obj1,SpriteSimpleInfoData obj2) {
		
		if(collide(obj1, obj2)) {		
			 Rect collisionBounds = getCollisionBounds(obj1.getRect(), obj2.getRect());
		        for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
		            for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
		                int sprite1Pixel = getBitmapPixel(obj1, i, j);
		                int sprite2Pixel = getBitmapPixel(obj1, i, j); 
		                if( isFilled(sprite1Pixel) && isFilled(sprite2Pixel)) {
		                    return true;
		                }
		            }
		        }

		}
		return false;
	}
	
	private static int getBitmapPixel(SpriteSimpleInfoData sprite, int i, int j) {
	    return sprite.getBmp().getPixel(i-(int)sprite.x, j-(int)sprite.y);
	}

	
	private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
	    int left = (int) Math.max(rect1.left, rect2.left);
	    int top = (int) Math.max(rect1.top, rect2.top);
	    int right = (int) Math.min(rect1.right, rect2.right);
	    int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
	    return new Rect(left, top, right, bottom);
	}

	
	static public boolean collide(SpriteSimpleInfoData obj1,SpriteSimpleInfoData obj2){		
		
		if(obj1.getRect().intersect(obj2.getRect())) 
			 return true;
		
		
		return false;
	}
	static public boolean collide(Sprite obj1,Sprite obj2){
		int w = (int) (obj1.getPosition().getX()+obj1.getWidth());
		int h  = (int) (obj1.getPosition().getY()+obj1.getHeight());
		
		boolean dimension = ((obj2.getPosition().getX()>=obj1.getPosition().getX() && obj2.getPosition().getX()<w)
				|| ((obj2.getPosition().getX()+obj2.getWidth())>=obj1.getPosition().getX() 
						&& (obj2.getPosition().getX()+obj2.getWidth())<w));
		
		if(dimension && ((obj2.getPosition().getY()>=obj1.getPosition().getY()&&obj2.getPosition().getY()<=h)
					|| (obj2.getPosition().getY()+obj2.getHeight()>=obj1.getPosition().getY() && obj2.getPosition().getY()+obj2.getHeight()<=h)
					)){
			return true;
		}
		
		
		
		return false;
	}
	static public boolean collide(Sprite obj1,Vector2 obj2){
		int wX = (int) (obj1.getPosition().getX()+obj1.getWidth());
		int h  = (int) (obj1.getPosition().getY()+obj1.getHeight());
		boolean dimension = (obj2.getX()>=obj1.getPosition().getX() && obj2.getX()<wX);
		
		
		if(dimension && (obj2.getY()>=obj1.getPosition().getY()&&obj2.getY()<=h)){
			return true;
		}
		
		
		
		return false;
		
	}
	
}
