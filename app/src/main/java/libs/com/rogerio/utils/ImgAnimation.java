package libs.com.rogerio.utils;



import java.util.List;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class ImgAnimation {

	protected List<ObjetosImg> obj;
	protected int max;
	int ind = 0;
	Vector2 position;
	Drawable img;
	ObjetosImg imgO;
	boolean finished;
	public boolean isFinish() {
		return finished;
	}

	public void setFinish(boolean finish) {
		this.finished = finish;
	}

	public ImgAnimation(List<ObjetosImg> objs){
		obj = objs;
		max = obj.size();
		imgO = obj.get(0);
		img = imgO.img;
		finished= false;
	}

	public ImgAnimation() {
		super();
	}

	public ObjetosImg getImg() {		
		return imgO;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {		
		this.position = position;
		
	}

	public void draw(Canvas canvas) {
		
		img.setBounds((int)position.getX(),(int)position.getY(),
				(int)position.getX()+img.getIntrinsicWidth(),(int)position.getY()+img.getIntrinsicHeight());
		img.draw(canvas);
		
	}

	protected void getObj() {		
		getNewImg();	
		
	}

	public void getNewImg() {
		if((ind+1)==max) finished=true;
		ind = (ind + 1) % max;	
		imgO = obj.get(ind);
		imgO.setPosition(position);
		img = imgO.img;	
	}

	public void moveDown(int mov) {
		position.setY(position.getY()+mov);
		ind = (ind + 1) % max;	
		//getObj();
		
	}

}