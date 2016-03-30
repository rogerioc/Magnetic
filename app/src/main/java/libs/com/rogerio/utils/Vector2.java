package libs.com.rogerio.utils;

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */
public class Vector2 {
	private float x,y;
	
	static public  Vector2 Zero(){
		return new Vector2();
	}
	
	public Vector2(float x,float y){
		this.x=x;
		this.y=y;
	}
	public Vector2(){
		this.x=0;
		this.y=0;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	 public Vector2 normal(Vector2 pos){
		 Vector2 v1 = new Vector2();
		 double len;
		    /* Encontra vetor v1 */
		    v1.x = pos.x - x;
		    v1.y = pos.y - y; 		    
		    len = Math.sqrt(v1.x*v1.x + v1.y*v1.y);
		    v1.x /= len;
		    v1.y /= len;
		    return v1;
		   
	 }
	
	 public float coefAng( Vector2 pos){
		 Vector2 v1 = new Vector2();
		 v1.x = pos.x - x;
		 v1.y = pos.y - y; 	
		 return v1.y/v1.x;
	 }

}
