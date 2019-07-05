package rogerio.magneticGame.com

import java.util.ArrayList

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

import libs.com.rogerio.utils.*
import rogerio.magneticGame.*

class Screen(private val mContext: Context) : View(mContext), AccelerometerListener, Runnable {

    internal var iniX: Float = 0.toFloat()
    internal var posicaoY: Float = 0.toFloat()
    internal var iniY: Float = 0.toFloat()
    internal var posicaoX: Float = 0.toFloat()
    internal var larguraDoObj: Int = 0
    internal var alturaDoObj: Int = 0
    private var larguraTela: Int = 0
    private var alturaTela: Int = 0
    private val mSpaceShip: Bitmap
    private var ini: Boolean? = true
    private val mBullet: Bitmap
    private val mBullets: MutableList<SpriteSimpleInfoData>
    private var mPlay: Boolean = false

    private val tag = "Screen"
    private val mSpSpaceShip: SpriteSimpleInfoData

    private val lstMeteors: MutableList<SpriteSimpleInfoData>
    private val lstExplodes: MutableList<SpriteSimpleInfoData>
    private var mMeteor: Bitmap? = null
    private val mMetorsLength = 10
    private val mNivel = 0
    private val idExplodes: ArrayList<Int>
    private val mPaint: Paint
    //Dados Painel
    private val score = 0

    private var m_State: GameState? = null
    private var count: Int = 0
    private var m_iniTime: Long = 0

    internal var handleGame: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            core()
        }
    }

    init {
        AccelerometerManager.setContextAcc(mContext, AccType.Magnetic)
        onAccStart()

        mSpaceShip = getImage(R.drawable.nav)
        mBullet = getImage(R.drawable.laser)
        mMeteor = getImage(R.drawable.asteroid01)

        alturaDoObj = mSpaceShip.height
        larguraDoObj = mSpaceShip.width
        mBullets = ArrayList()
        idExplodes = ArrayList()

        idExplodes.add(R.drawable.explode1)
        idExplodes.add(R.drawable.explode2)
        idExplodes.add(R.drawable.explode3)
        idExplodes.add(R.drawable.explode4)
        lstExplodes = ArrayList()
        posicaoY = 240f
        posicaoX = posicaoY
        mPlay = true
        mSpSpaceShip = SpriteSimpleInfoData()
        lstMeteors = ArrayList()
        mPaint = Paint()
        mPaint.setARGB(255, 255, 255, 255)
        m_State = GameState.Game
        count = 5
        isFocusable = true

    }

    /**
     *
     * @param id
     * @return
     */
    private fun getImage(id: Int): Bitmap {
        return BitmapFactory.decodeResource(mContext.resources, id)
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

    fun onDestroy() {
        mPlay = false
        if (AccelerometerManager.isListening) {
            AccelerometerManager.stopListening()
        }
    }


    override fun draw(canvas: Canvas) {

        super.draw(canvas)
        when (m_State) {
            GameState.Game -> game(canvas)
        }

        synchronized(lstExplodes) {
            val expl = lstExplodes.iterator()
            while (expl.hasNext()) {
                val ex = expl.next()
                if (ex.frame < idExplodes.size) {
                    canvas.drawBitmap(getImage(idExplodes[ex.frame]), ex.x.toFloat(), ex.y.toFloat(), null)
                    if (ex.time > 1) {
                        ex.frame++
                        ex.time = 0
                    } else {
                        ex.time++
                    }
                } else {
                    expl.remove()
                }
            }
        }
        painel(canvas)
    }

    protected fun game(canvas: Canvas) {
        var matrix = Matrix()
        // rotate the Bitmap
        if (!mSpSpaceShip.kill) {
            val rotate = Math.atan2(posicaoX.toDouble(), posicaoY.toDouble()).toFloat()

            mSpSpaceShip.rotate = rotate        //
            //canvas.drawText("Valores: "+pos, x, y, null);
            //Log.i(tag, "Rotate: "+ posicaoX+ " "+posicaoY);
            matrix.postRotate(Math.toDegrees(rotate.toDouble()).toFloat())

            var resizedBitmap = Bitmap.createBitmap(mSpaceShip, 0, 0,
                    mSpaceShip.width, mSpaceShip.height, matrix, true)

            canvas.drawBitmap(resizedBitmap, mSpSpaceShip.x.toFloat(), mSpSpaceShip.y.toFloat(), null)

            synchronized(mBullets) {
                val met = mBullets.iterator()
                while (met.hasNext()) {
                    val m = met.next()
                    matrix = Matrix()
                    val r = m.rotate - 3.141516f / 2
                    matrix.postRotate(Math.toDegrees(r.toDouble()).toFloat())
                    resizedBitmap = Bitmap.createBitmap(mBullet, 0, 0,
                            mBullet.width, mBullet.height, matrix, true)
                    canvas.drawBitmap(resizedBitmap, m.x.toFloat(), m.y.toFloat(), null)
                }
            }
        }
        synchronized(lstMeteors) {
            val met = lstMeteors.iterator()
            while (met.hasNext()) {
                val m = met.next()
                if (!m.kill) {
                    canvas.drawBitmap(mMeteor!!, m.x.toFloat(), m.y.toFloat(), null)
                }
            }
        }


    }


    private fun painel(canvas: Canvas) {
        // Display status and messages.
        val fontSize = mPaint.textSize

        canvas.drawText("Score: $score", fontSize, mPaint.textSize,
                mPaint)
        //canvas.drawText("Ships: " + shipsLeft, fontSize, height - fontSize,
        //mPaint);
        canvas.drawText("" + count, width / 2 - mPaint.textSize / 2, height / 2 - mPaint.textSize / 2,
                mPaint)
        //String str = "High: " + highScore;

    }

    override fun onAccelerationChanged(x: Float, y: Float, z: Float) {
        if (ini!!) {
            iniX = x
            iniY = y
            ini = false
        }
        posicaoX = x
        posicaoY = y
        //Log.i(tag, "Novo: "+ x+ " "+y+ " "+z);
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh)

        larguraTela = w
        alturaTela = h
        posicaoX = (larguraTela / 2 - larguraDoObj / 2).toFloat()
        posicaoY = (alturaTela / 2 - alturaDoObj / 2).toFloat()
        mSpSpaceShip.x = posicaoX.toInt()
        mSpSpaceShip.y = posicaoY.toInt()
        mSpSpaceShip.h = mSpaceShip.height
        mSpSpaceShip.w = mSpaceShip.width
        generateMeteorsList()
        m_iniTime = SystemClock.currentThreadTimeMillis()
        val th = Thread(this)
        th.start()
    }

    protected fun triang(x: Float, y: Float, tam: Int): FloatArray {
        val pts = FloatArray(3 * 4)
        pts[0] = x
        pts[1] = y
        pts[2] = pts[0] - tam / 2
        pts[3] = pts[1] + tam
        pts[4] = pts[2]
        pts[5] = pts[3]
        pts[6] = pts[0] + tam / 2
        pts[7] = pts[3]
        pts[8] = pts[6]
        pts[9] = pts[7]
        pts[10] = pts[0]
        pts[11] = pts[1]
        return pts
    }

    override fun onShake(force: Float) {
        // TODO Auto-generated method stub

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            fireKey()
        }
        return true
    }

    private fun fireKey() {
        val spBullet = SpriteSimpleInfoData()
        spBullet.x = mSpSpaceShip.x + mSpSpaceShip.w / 2
        spBullet.y = mSpSpaceShip.y + mSpSpaceShip.h / 2
        spBullet.speed = 6.0
        spBullet.rotate = mSpSpaceShip.rotate
        spBullet.deltaX = 2 * Math.sin(mSpSpaceShip.rotate.toDouble())
        spBullet.deltaY = 2 * Math.cos(mSpSpaceShip.rotate.toDouble())
        spBullet.h = mBullet.height
        spBullet.w = mBullet.width

        synchronized(mBullets) {
            mBullets.add(spBullet)
        }
        Log.i(tag, "Nova bala")
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    private fun core() {
        when (m_State) {
            GameState.Game -> {
                if (lstMeteors.size == 0) {
                    generateMeteorsList()
                }
                moveBullets()
                updateMeteors()
            }

            GameState.SpExplode -> if (lstExplodes.size == 0) {
                m_State = GameState.Game
                mSpSpaceShip.kill = false
                generateNewPostions()
            }
            GameState.Count -> {
                val time = SystemClock.currentThreadTimeMillis()
                //Log.i(tag, "Time:"+);
                if (time - m_iniTime >= 100) {
                    count--
                    m_iniTime = time
                }
                if (count == 0) {
                    count = 5
                }
            }
        }

        invalidate()
    }

    private fun moveBullets() {
        synchronized(mBullets) {
            val bullets = mBullets.iterator()
            while (bullets.hasNext()) {
                val b = bullets.next()

                if (b.kill)
                    bullets.remove()

                if (b.x > width + b.w || b.x < 0 || b.y > height || b.y < 0) {
                    b.kill = true
                } else {
                    b.x += (b.deltaX * b.speed).toInt()
                    b.y -= (b.deltaY * b.speed).toInt()
                }
                val met = lstMeteors.iterator()
                while (met.hasNext()) {
                    val m = met.next()
                    /*Log.d(tag,"Bullet " + b.x+ " "+b.y);
					Log.d(tag,"MEtero " + m.x+ " "+m.y);*/
                    if (Physics.collide(b, m)) {
                        m.kill = true
                        b.kill = true
                        Log.i(tag, "Colidiu")
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
    private fun geraExplode(x: Int, y: Int) {
        val explode = SpriteSimpleInfoData()
        explode.x = x
        explode.y = y
        explode.frame = 0
        synchronized(lstExplodes) {
            lstExplodes.add(explode)
        }

    }

    private fun updateMeteors() {
        synchronized(lstMeteors) {
            val mIter = lstMeteors.iterator()
            while (mIter.hasNext()) {
                val m = mIter.next()

                if (m.kill) {
                    mIter.remove()
                    geraExplode(m.x, m.y)
                }

                if (m.x < -width / 2 || m.x > width + m.w || m.y > height + m.h || m.y < -height / 2) {
                    m.kill = true
                } else {
                    m.x += (m.deltaX * m.speed).toInt()
                    m.y += (m.deltaY * m.speed).toInt()
                }
                //Colis�o com a nave
                if (Physics.collide(mSpSpaceShip, m)) {
                    m.kill = true
                    mSpSpaceShip.kill = true
                    Log.i(tag, "Colidiu nave")
                    m_State = GameState.SpExplode
                    geraExplode(mSpSpaceShip.x, mSpSpaceShip.y)
                }

                //Log.i(tag, "Meteor "+ m.x + " "+m.y);
            }
        }
    }

    /**
     *
     */
    private fun generateNewPostions() {
        synchronized(lstMeteors) {
            val mIter = lstMeteors.iterator()
            while (mIter.hasNext()) {
                val m = mIter.next()
                meteorPositions(m)

                //Log.i(tag, "Meteor "+ m.x + " "+m.y);
            }
        }
    }

    /***
     * Gera os meteoros
     */
    private fun generateMeteorsList() {

        for (i in 0 until mMetorsLength) {
            val m = SpriteSimpleInfoData()
            m.frame = 0
            meteorPositions(m)

            //Teste
            //m.x = mSpSpaceShip.x - mMeteor.getWidth();;
            //m.y = mSpSpaceShip.y;

            val v = Vector2(mSpSpaceShip.x.toFloat(), mSpSpaceShip.y.toFloat())
            var v2 = Vector2(m.x.toFloat(), m.y.toFloat())
            v2 = v2.normal(v)
            //Procurar o heroi
            //float r=(float)Math.atan2(v.getX(),v.getY()) + 3.141516f / 2 ;

            m.deltaX = v2.x.toDouble()
            m.deltaY = v2.y.toDouble()

            m.w = mMeteor!!.width
            m.h = mMeteor!!.height
            m.speed = 2.0
            /*if (i % 4 == 0) {
				m.speed += mNivel * 2;
			}*/

            m.kill = false
            lstMeteors.add(m)
        }
    }

    /**
     * Gera a posi��o inicial na tela para um meteoro
     * @param m
     */
    protected fun meteorPositions(m: SpriteSimpleInfoData) {
        if (Math.random() < 0.5) {
            m.x = -width / 2
            if (Math.random() < 0.5)
                m.x = width
            m.y = (Math.random() * height).toInt()
        } else {
            m.x = (Math.random() * width).toInt()
            m.y = -height / 2
            if (Math.random() < 0.5)
                m.y = height
        }
    }

    override fun run() {
        while (mPlay) {
            handleGame.sendEmptyMessage(0)

            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                // FIXME Auto-generated catch block
                e.printStackTrace()
            }

        }

    }

}
