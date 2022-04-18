package rogerio.magneticGame.com

import android.app.Activity
import android.os.Bundle

class MagneticGame : Activity() {
    /** Called when the activity is first created.  */
    lateinit var mScreen: Screen

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScreen = Screen(this)
        setContentView(mScreen)
    }

    public override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
        mScreen.onDestroy()
    }


}