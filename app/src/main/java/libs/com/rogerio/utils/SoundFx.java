package libs.com.rogerio.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import android.util.Log;

public class SoundFx extends Sounds
{

	private String tag="SoundFx";	
	SoundPool soundPool;
	private int soundID;
	private String name;
	private MediaPlayer mPlayer;

	public SoundFx(Context ctx, int resID) {
		super(ctx);
		
		name = ctx.getResources().getResourceName(resID);	
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		
		soundID = soundPool.load(ctx,resID, 1);	
		
	}
	
	public void play () {
		volume();
		if(soundPool.play(soundID, leftVolume, rightVolume, 1, 0, 1f)==0) {
			Log.e(tag, "Erro estranho");
		}
		return;
		
	}
	
	public void stop() {
		try {			
			soundPool.stop(soundID);
			
		} catch (Exception e) {
			Log.d(tag,"AduioClip::stop " + name + " " + e.toString());
		}
	}
		
	
}
