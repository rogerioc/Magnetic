package rogerio.magneticGame.com;

import android.app.Activity;
import android.os.Bundle;

public class MagneticGame extends Activity {
    /** Called when the activity is first created. */
	Screen mScreen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreen = new Screen(this);
        setContentView(mScreen);
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mScreen.onDestroy();
	}
    
    
}