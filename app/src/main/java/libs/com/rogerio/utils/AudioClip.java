package libs.com.rogerio.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class AudioClip extends Sounds 
{
	private MediaPlayer mPlayer;
	private String name;
	
	private boolean mPlaying = false;
	private boolean mLoop = false;
	
	private String tag="AudioClip";		
	
	public boolean ismPlaying() {
		return mPlaying;
	}

	public AudioClip(Context ctx, int resID) {
		super(ctx);
		name = ctx.getResources().getResourceName(resID);	
		
		volume();
		
		mPlayer = MediaPlayer.create(ctx, resID);
		mPlayer.setVolume(leftVolume, rightVolume);
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				mPlaying = false;
				if ( mLoop) {
					Log.i(tag,"AudioClip loop " + name);
					mp.start();
				}
			}
			
		});
	}

	public synchronized void play () {		
		if ( mPlaying) {			
			return;
		}
		volume();
		
		if (mPlayer != null ) {
			mPlaying = true;
			mPlayer.setVolume(leftVolume, rightVolume);
			
			mPlayer.start();
		}
	}
	
	public synchronized void stop() {
		try {
			mLoop = false;
			if ( mPlaying ) { 
				mPlaying = false;
				mPlayer.pause();
			}
			
		} catch (Exception e) {
			Log.d(tag,"AduioClip::stop " + name + " " + e.toString());
		}
	}
	
	public synchronized void loop () {
		mLoop = true;
		mPlaying = true;
		mPlayer.start();		
		
	}
	
	public void release () {
		if (mPlayer != null) { 
			mPlayer.release();
			mPlayer = null;
		}
	}
}
