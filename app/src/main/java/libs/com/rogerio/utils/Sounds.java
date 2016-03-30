package libs.com.rogerio.utils;

import android.content.Context;
import android.media.AudioManager;

public class Sounds {
	protected float leftVolume;
	protected float rightVolume;
	AudioManager manager;	
	
	public Sounds(Context ctx) {
		manager =(AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	}

	protected void volume() {
		float curVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		leftVolume = curVolume/maxVolume;
		rightVolume = curVolume/maxVolume;
	}

}