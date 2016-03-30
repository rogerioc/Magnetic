package libs.com.rogerio.utils;

import android.graphics.Paint;


public class GamesFX {
	/**
	 * Sorteia uma nova cor para uma estrela
	 * 
	 * @return
	 */
	public static Paint newColor() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		Paint p = new Paint();
		p.setARGB(255, r, g, b);
		return p;
	}
	
}
