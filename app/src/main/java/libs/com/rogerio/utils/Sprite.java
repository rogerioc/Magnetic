package libs.com.rogerio.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
 
public class Sprite {
 
    private Bitmap mAnimation;
   
    public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	private Rect mSRectangle;
    private int mFPS;
    private int mNoOfFrames;
    private int mCurrentFrame;
    private long mFrameTimer;
    private int mSpriteHeight;
    private int mSpriteWidth;
    private Vector2 position;
    private float mRotate;

	public float getRotate() {
		return mRotate;
	}

	public void setRotate(float rotate) {
		this.mRotate = rotate;
	}

	public int getHeight() {
		return mSpriteHeight;
	}

	public int getWidth() {
		return mSpriteWidth;
	}

	public Sprite() {
        mSRectangle = new Rect(0,0,0,0);
        mFrameTimer =0;
        mCurrentFrame =0;
        
    }
 
    public void init(Bitmap theBitmap, int Height, int Width, int theFPS, int theFrameCount) throws Exception {
    	if(theBitmap==null) {
    		throw new Exception("Null Bitmap.");
    	}
        mAnimation = theBitmap;
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        mSRectangle.top = 0;
        mSRectangle.bottom = mSpriteHeight;
        mSRectangle.left = 0;
        mSRectangle.right = mSpriteWidth;
        mFPS = 1000 /theFPS;
        mNoOfFrames = theFrameCount;
        mRotate=0;
    }
 
    public void Update(long GameTime) {
        if(GameTime > mFrameTimer + mFPS ) {
            mFrameTimer = GameTime;
            mCurrentFrame +=1;
 
            if(mCurrentFrame >= mNoOfFrames) {
                mCurrentFrame = 0;
            }
        }
 
        mSRectangle.left = mCurrentFrame * mSpriteWidth;
        mSRectangle.right = mSRectangle.left + mSpriteWidth;
    }
 
    public void draw(Canvas canvas) {
    	
    
        Matrix matrix = new Matrix();
        // rotate the Bitmap
        matrix.postRotate(mRotate);
        
        Bitmap resizedBitmap = Bitmap.createBitmap(mAnimation, mSRectangle.left,mSRectangle.top ,
        		mSpriteWidth, mSpriteHeight, matrix, true);
      
        
        //Rect dest = new Rect((int)position.getX(),(int)position.getY(),(int) position.getX()+ mSpriteWidth,
        		//(int)position.getY()+ mSpriteHeight);
        canvas.drawBitmap(resizedBitmap,position.getX(),position.getY(),null);
        //canvas.drawBitmap(resizedBitmap, mSRectangle, dest, null);
    }
 
   
}