package rogerio.magneticGame.com;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import  libs.com.rogerio.utils.*;
import rogerio.magneticGame.*;

public class Screen extends View implements AccelerometerListener, Runnable{
	
	float iniX,posicaoY;
	float iniY,posicaoX;
	int larguraDoObj;
	int alturaDoObj;
	private int larguraTela;
	private int alturaTela;
	private Bitmap mSpaceShip;
	private Boolean ini=true;
	private Bitmap mBullet;
	private List<SpriteSimpleInfoData> mBullets;
	private boolean mPlay;
	
	private String tag = "Screen";
	private SpriteSimpleInfoData mSpSpaceShip;
	
	private List<SpriteSimpleInfoData> lstMeteors;
	private List<SpriteSimpleInfoData> lstExplodes;
	private Bitmap mMeteor= null;
	private int mMetorsLength  = 10;
	private int mNivel=0;
	private ArrayList<Integer> idExplodes; 
	
	private Context mContext;
	private Paint mPaint;
	//Dados Painel
	private int score = 0;
	
	private GameState m_State;
	private int count;
	private long m_iniTime;
	public Screen(Context context) {
		super(context);
		mContext = context;
		AccelerometerManager.setContextAcc(context,AccType.Magnetic);
		onAccStart();
	
		mSpaceShip = getImage(R.drawable.nav);
		mBullet =  getImage(R.drawable.laser);
		mMeteor =  getImage(R.drawable.asteroid01);
		
		alturaDoObj = mSpaceShip.getHeight();
		larguraDoObj= mSpaceShip.getWidth();
		mBullets = new ArrayList<SpriteSimpleInfoData>();
		idExplodes = new ArrayList<Integer>();
		
		idExplodes.add(R.drawable.explode1);
		idExplodes.add(R.drawable.explode2);
		idExplodes.add(R.drawable.explode3);
		idExplodes.add(R.drawable.explode4);
		lstExplodes = new ArrayList<SpriteSimpleInfoData>();
		posicaoX=posicaoY=240;
		mPlay = true;
		mSpSpaceShip = new SpriteSimpleInfoData();
		lstMeteors= new ArrayList<SpriteSimpleInfoData>();
		mPaint = new Paint();
		mPaint.setARGB(255, 255, 255, 255);
		m_State = GameState.Game;
		count=5;
		setFocusable(true);
		
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	private Bitmap getImage(int id){
		return BitmapFactory.decodeResource(mContext.getResources(),id);
	}
	
	protected void onAccStart() {	        
        if (AccelerometerManager.isSupported()) {
            AccelerometerManager.startListening(this);
        } else {
        	Toast.makeText(mContext, "Your device no has Accelerometer.", Toast.LENGTH_LONG).show();
        	try{
        		((Activity)(mContext)).finish();
        	}catch (Exception e) {
        		Log.d(tag, "Erro acc:" + e.getMessage());

        	}
        
        }
    }
	
	public void onDestroy() {
		mPlay=false;
		if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
	}
	
	 
	@Override
	public void draw(Canvas canvas) {
		
		super.draw(canvas);
		switch (m_State) {
		case Game:
			game(canvas);			
			break;

		
		}
		
		synchronized (lstExplodes) {
        	for (Iterator<SpriteSimpleInfoData> expl = lstExplodes.iterator(); expl.hasNext();) {
        		SpriteSimpleInfoData ex = expl.next();
        		if(ex.frame<idExplodes.size()){
        			canvas.drawBitmap(getImage(idExplodes.get(ex.frame)),ex.x,ex.y,null);
        			if(ex.time>1) {
        				ex.frame++;
        				ex.time=0;
        			}else {
        				ex.time++;
        			}
        		} else {
        			expl.remove();
        		}
        	}
		}
		 painel(canvas);
	}
	protected void game(Canvas canvas) {
		Matrix matrix = new Matrix();
        // rotate the Bitmap
        if(!mSpSpaceShip.kill) {
	        float rotate=(float)Math.atan2(posicaoX,posicaoY);
	        
			mSpSpaceShip.rotate=rotate;		//
			//canvas.drawText("Valores: "+pos, x, y, null);
			//Log.i(tag, "Rotate: "+ posicaoX+ " "+posicaoY);
	        matrix.postRotate((float)Math.toDegrees(rotate));
	        
	        Bitmap resizedBitmap = Bitmap.createBitmap(mSpaceShip,0,0 ,
	        		mSpaceShip.getWidth(), mSpaceShip.getHeight(), matrix, true);
	        
	        canvas.drawBitmap(resizedBitmap, mSpSpaceShip.x,mSpSpaceShip.y, null);
        
	        synchronized (mBullets) {        	 
	        	for (Iterator<SpriteSimpleInfoData> met = mBullets.iterator(); met.hasNext();) {
	        		SpriteSimpleInfoData m = met.next();
	        		matrix = new Matrix();
	        		float r=m.rotate-  3.141516f / 2 ;
	        		matrix.postRotate((float)Math.toDegrees(r));
	        		resizedBitmap = Bitmap.createBitmap(mBullet,0,0 ,
	        				mBullet.getWidth(), mBullet.getHeight(), matrix, true);
					canvas.drawBitmap(resizedBitmap, m.x,m.y,null);
				}
			}
        }
        synchronized (lstMeteors) {
        	for (Iterator<SpriteSimpleInfoData> met = lstMeteors.iterator(); met.hasNext();) {
        		SpriteSimpleInfoData m = met.next();
        		if(!m.kill) {
        			canvas.drawBitmap(mMeteor,m.x,m.y,null);
        		}
        	}
		}
        
        
       
	}
	
	
	private void painel(Canvas canvas) {
		// Display status and messages.
		float fontSize = mPaint.getTextSize();

		canvas.drawText("Score: " + score, fontSize, mPaint.getTextSize(),
				mPaint);
		//canvas.drawText("Ships: " + shipsLeft, fontSize, height - fontSize,
				//mPaint);
		canvas.drawText(""+count, getWidth()/2 -  mPaint.getTextSize()/2,getHeight()/2 - mPaint.getTextSize()/2,
				mPaint);
		//String str = "High: " + highScore;

	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		if(ini){
			iniX=x;
			iniY=y;
			ini=false;
		}
		posicaoX=x;
		posicaoY=y;
		//Log.i(tag, "Novo: "+ x+ " "+y+ " "+z);
		invalidate();
	}

	 protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        // TODO Auto-generated method stub
	        super.onSizeChanged(w, h, oldw, oldh);
	       
	        larguraTela = w;
	        alturaTela = h;
	        posicaoX = (larguraTela / 2)-(larguraDoObj/2);
	        posicaoY = (alturaTela / 2)-(alturaDoObj/2);	
	        mSpSpaceShip.x =(int) posicaoX;
	        mSpSpaceShip.y = (int) posicaoY;
	        mSpSpaceShip.h = mSpaceShip.getHeight();
	        mSpSpaceShip.w = mSpaceShip.getWidth();
	        generateMeteorsList();
	        m_iniTime = SystemClock.currentThreadTimeMillis();
	        Thread th = new Thread(this);
			th.start();
	 }

	protected float [] triang(float x,float y,int tam) {
		float[] pts = new float[3*4];
		pts[0] = x;
		pts[1] = y;
		pts[2] = pts[0]- tam/2;
		pts[3] = pts[1]+ tam;
		pts[4] = pts[2];
		pts[5] = pts[3];
		pts[6] = pts[0]+ tam/2;
		pts[7] = pts[3];
		pts[8] = pts[6];
		pts[9] = pts[7];
		pts[10] = pts[0];
		pts[11] = pts[1];
		return pts;
	}
	 
	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			fireKey();
		}
		return true;
	}

	private void fireKey() {
		SpriteSimpleInfoData spBullet = new SpriteSimpleInfoData();
		spBullet.x =  mSpSpaceShip.x + mSpSpaceShip.w/2;
		spBullet.y =  mSpSpaceShip.y + mSpSpaceShip.h/2;
		spBullet.speed = 6;
		spBullet.rotate = mSpSpaceShip.rotate;
		spBullet.deltaX = 2 * Math.sin(mSpSpaceShip.rotate);
		spBullet.deltaY = 2 * Math.cos(mSpSpaceShip.rotate);
		spBullet.h = mBullet.getHeight();
		spBullet.w = mBullet.getWidth();
		
		synchronized (mBullets) {
			mBullets.add(spBullet);
		}
		Log.i(tag , "Nova bala");
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	private void core() {
		switch (m_State) {
		case Game:
			if(lstMeteors.size()==0) {
				generateMeteorsList();
			}
			moveBullets();
			updateMeteors();
			break;

		case SpExplode:
			if(lstExplodes.size()==0) { 
				m_State = GameState.Game;
				mSpSpaceShip.kill= false;
				generateNewPostions();
			}
			break;
		case Count:
			long time = SystemClock.currentThreadTimeMillis();
			//Log.i(tag, "Time:"+);
			if((time-m_iniTime)>=100) {
				count--;
				m_iniTime = time;
			}
			if(count==0) {
				count = 5;
			}
			break;
		}
		
		invalidate();	
	}
	
	private void moveBullets() {
		synchronized (mBullets) {        	 
        	for (Iterator<SpriteSimpleInfoData> bullets = mBullets.iterator(); bullets.hasNext();) {
        		SpriteSimpleInfoData b = bullets.next();
				
				if (b.kill)
					bullets.remove();

				if (b.x > (getWidth() + b.w) || b.x<0 || b.y>(getHeight()) || b.y<0) {
					b.kill=true;
				} else {
					b.x += b.deltaX * b.speed;
					b.y -= b.deltaY * b.speed;
				}
				for(Iterator<SpriteSimpleInfoData> met = lstMeteors.iterator();met.hasNext();){
					SpriteSimpleInfoData m = met.next();
					/*Log.d(tag,"Bullet " + b.x+ " "+b.y);
					Log.d(tag,"MEtero " + m.x+ " "+m.y);*/
					if(Physics.collide(b, m)){
						m.kill=true;
						b.kill=true;
						Log.i(tag, "Colidiu");
					}
					
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	private void geraExplode(int x, int y) {
		SpriteSimpleInfoData explode=  new SpriteSimpleInfoData();
		explode.x=x;
		explode.y=y;
		explode.frame=0;
		synchronized (lstExplodes) {
			lstExplodes.add(explode);
		}
		
	}
	private void updateMeteors(){
		synchronized (lstMeteors) {
			for(Iterator<SpriteSimpleInfoData> mIter = lstMeteors.iterator();mIter.hasNext();){
				SpriteSimpleInfoData m= mIter.next();

				if (m.kill) {
					mIter.remove();
					geraExplode(m.x,m.y);
				}

				if ( (m.x < (-getWidth()/2) || m.x>(getWidth()+m.w)) || m.y>(getHeight()+m.h) || m.y<(-getHeight()/2)) {
					m.kill=true;
				} else {
					m.x += m.deltaX * m.speed;
					m.y += m.deltaY * m.speed;	
				}
				//Colis�o com a nave
				if(Physics.collide(mSpSpaceShip, m)){
					m.kill=true;
					mSpSpaceShip.kill=true;
					Log.i(tag, "Colidiu nave");
					m_State = GameState.SpExplode;
					geraExplode(mSpSpaceShip.x,mSpSpaceShip.y);
				}				
				
				//Log.i(tag, "Meteor "+ m.x + " "+m.y);
			}
		}
	}
	
	/**
	 * 
	 */
	private void generateNewPostions(){
		synchronized (lstMeteors) {
			for(Iterator<SpriteSimpleInfoData> mIter = lstMeteors.iterator();mIter.hasNext();){
				SpriteSimpleInfoData m= mIter.next();
				meteorPositions(m);
				
				//Log.i(tag, "Meteor "+ m.x + " "+m.y);
			}
		}
	}
	/***
	 * Gera os meteoros
	 */
	private void generateMeteorsList(){
		
		for(int i=0;i<mMetorsLength  ;i++){
			SpriteSimpleInfoData m = new SpriteSimpleInfoData();
			m.frame = 0;
			meteorPositions(m);
			
			//Teste
			//m.x = mSpSpaceShip.x - mMeteor.getWidth();; 
			//m.y = mSpSpaceShip.y;
			
			Vector2 v = new Vector2(mSpSpaceShip.x, mSpSpaceShip.y);
			Vector2 v2 = new Vector2(m.x, m.y);
			v2=v2.normal(v);
			//Procurar o heroi
			//float r=(float)Math.atan2(v.getX(),v.getY()) + 3.141516f / 2 ;
			
			m.deltaX = v2.getX();
			m.deltaY = v2.getY();
			
			m.w = mMeteor.getWidth();
			m.h = mMeteor.getHeight();
			m.speed = 2;
			/*if (i % 4 == 0) {
				m.speed += mNivel * 2;
			}*/

			m.kill = false;
			lstMeteors.add(m);
		}
	}
	
	/**
	 * Gera a posi��o inicial na tela para um meteoro
	 * @param m
	 */
	protected void meteorPositions(SpriteSimpleInfoData m) {
		if (Math.random() < 0.5) {
			m.x = -getWidth() / 2;
			if (Math.random() < 0.5)
				m.x = getWidth();
			m.y =  (int) (Math.random()* getHeight());
		} else {
			m.x =  (int) (Math.random() * getWidth());
			m.y = -getHeight() / 2;
			if (Math.random() < 0.5)
				m.y = getHeight();
		}
	}

	Handler handleGame = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			core();
		}	
	};
	
	@Override
	public void run() {
		while(mPlay) {
			handleGame.sendEmptyMessage(0);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
