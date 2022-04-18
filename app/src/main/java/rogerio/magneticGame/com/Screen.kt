package rogerio.magneticGame.com

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

import libs.com.rogerio.utils.*
import rogerio.magneticGame.com.core.MagneticCore


class Screen(private val mContext: Context) : View(mContext), AccelerometerListener {

    var coregame: MagneticCore?
    private val tag = "Screen"
    init {
        coregame = MagneticCore(mContext)
        isFocusable = true
        AccelerometerManager.setContextAcc(mContext, AccType.Magnetic)
        onAccStart()
    }

    fun onDestroy() {
        coregame?.onDestroy()

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        coregame?.draw(canvas)
    }

    override fun onAccelerationChanged(x: Float, y: Float, z: Float) {
        coregame?.onAccelerationChanged(x, y, z)
        invalidate()
    }

    protected fun onAccStart() {
        if (AccelerometerManager.isSupported) {
            AccelerometerManager.startListening(this)
        } else {
            Toast.makeText(mContext, "Your device no has Accelerometer.", Toast.LENGTH_LONG).show()
            try {
                (mContext as Activity).finish()
            } catch (e: Exception) {
                Log.d(tag, "Erro acc:" + e.message)

            }

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        coregame?.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onShake(force: Float) {
        // TODO Auto-generated method stub

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return coregame?.onTouchEvent(event) == true
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }

}
