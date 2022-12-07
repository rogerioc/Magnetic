package rogerio.magneticGame.com.core

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import libs.com.rogerio.utils.*
import rogerio.magneticGame.com.GameState
import rogerio.magneticGame.com.R
import java.util.ArrayList
import kotlin.math.roundToInt

class MagneticCore(
    private val mContext: Context
    ): Runnable {
    companion object {
        const val fireThreshold = 4
        const val meteorThreshold = 2
    }

    private var firstMeteorSpeed = 0.0
    private var larguraTela: Int = 0
    private var alturaTela: Int = 0
    private var fireSpeed = 6.0
    private var meteorSpeed = 6.0
    internal var iniX: Float = 0.toFloat()
    internal var posicaoY: Float = 0.toFloat()
    internal var iniY: Float = 0.toFloat()
    internal var posicaoX: Float = 0.toFloat()
    internal var larguraDoObj: Int = 0
    internal var alturaDoObj: Int = 0
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
    private val mMetorsLength = fireThreshold
    private val mNivel = 0
    private val idExplodes: ArrayList<Int>
    private val mPaint: Paint
    //Dados Painel
    private var score = 0

    private var m_State: GameState? = null
    private var count: Int = 0
    private var m_iniTime: Long = 0

    internal var handleGame: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            play()
        }
    }

    init {

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
        mPaint.textSize = 100.0f
        m_State = GameState.Game
        count = 5
    }


    /**
     *
     * @param id
     * @return
     */
    private fun getImage(id: Int): Bitmap {
        return BitmapFactory.decodeResource(mContext.resources, id)
    }


    fun onDestroy() {
        mPlay = false
        if (AccelerometerManager.isListening) {
            AccelerometerManager.stopListening()
        }
    }

    fun draw(canvas: Canvas) {
        when (m_State) {
            GameState.Game -> game(canvas)
        }
        drawExplodes(canvas)
        painel(canvas)
    }

    private fun drawExplodes(canvas: Canvas) {
        synchronized(lstExplodes) {
            val expl = lstExplodes.iterator()
            while (expl.hasNext()) {
                val ex = expl.next()
                if (ex.frame < idExplodes.size) {
                    canvas.drawBitmap(
                        getImage(idExplodes[ex.frame]),
                        ex.x.toFloat(),
                        ex.y.toFloat(),
                        null
                    )
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

        canvas.drawText("Score: $score", 10.0f, 10.0f+mPaint.textSize,
            mPaint)
        //canvas.drawText("Ships: " + shipsLeft, fontSize, height - fontSize,
        //mPaint);
//        canvas.drawText("" + count, larguraTela / 2 - mPaint.textSize / 2, alturaTela / 2 - mPaint.textSize / 2,
//            mPaint)
        //String str = "High: " + highScore;

    }


    fun onAccelerationChanged(x: Float, y: Float, z: Float) {
        if (ini!!) {
            iniX = x
            iniY = y
            ini = false
        }
        posicaoX = x
        posicaoY = y
        //Log.i(tag, "Novo: "+ x+ " "+y+ " "+z);

    }

    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        larguraTela = w
        alturaTela = h
        posicaoX = (larguraTela / 2 - larguraDoObj / 2).toFloat()
        posicaoY = (alturaTela / 2 - alturaDoObj / 2).toFloat()
        mSpSpaceShip.x = posicaoX.toInt()
        mSpSpaceShip.y = posicaoY.toInt()
        mSpSpaceShip.h = mSpaceShip.height
        mSpSpaceShip.w = mSpaceShip.width
        fireSpeed = (larguraTela/alturaTela).toDouble() * fireThreshold
        meteorSpeed = (larguraTela/alturaTela).toDouble() * meteorThreshold
        firstMeteorSpeed = meteorSpeed
        m_iniTime = SystemClock.currentThreadTimeMillis()
        generateMeteorsList()
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
        pts[fireThreshold] = pts[0]
        pts[11] = pts[1]
        return pts
    }


    private fun fireKey() {
        val spBullet = SpriteSimpleInfoData()
        spBullet.x = mSpSpaceShip.x + mSpSpaceShip.w / 2
        spBullet.y = mSpSpaceShip.y + mSpSpaceShip.h / 2
        spBullet.speed = fireSpeed
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

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            fireKey()
        }
        return true
    }

    fun play() {
        when (m_State) {
            GameState.Game -> {
                if (lstMeteors.size == 0) {
                    meteorSpeed *= 2
                    generateMeteorsList()
                }
                moveBullets()
                updateMeteors()
            }

            GameState.KillShip -> if (lstExplodes.size == 0) {
                m_State = GameState.Game
                mSpSpaceShip.kill = false
                meteorSpeed = firstMeteorSpeed
                score = 0
                generateNewMeteorsPostions()
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
    }

    private fun moveBullets() {
        synchronized(mBullets) {
            val bullets = mBullets.iterator()
            while (bullets.hasNext()) {
                val bullet = bullets.next()

                if (bullet.kill)
                    bullets.remove()

                if (bullet.x > larguraTela + bullet.w || bullet.x < 0 || bullet.y > alturaTela || bullet.y < 0) {
                    bullet.kill = true
                } else {
                    bullet.x += (bullet.deltaX * bullet.speed).toInt()
                    bullet.y -= (bullet.deltaY * bullet.speed).toInt()
                }
                val met = lstMeteors.iterator()
                while (met.hasNext()) {
                    val m = met.next()
                    /*Log.d(tag,"Bullet " + b.x+ " "+b.y);
					Log.d(tag,"MEtero " + m.x+ " "+m.y);*/
                    if (Physics.collide(bullet, m)) {
                        m.kill = true
                        bullet.kill = true
                        score++
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

                if (m.x < -larguraTela / 2 || m.x > larguraTela + m.w || m.y > alturaTela + m.h || m.y < -alturaTela / 2) {
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
                    m_State = GameState.KillShip
                    geraExplode(mSpSpaceShip.x, mSpSpaceShip.y)
                }

                //Log.i(tag, "Meteor "+ m.x + " "+m.y);
            }
        }
    }

    /**
     *
     */
    private fun generateNewMeteorsPostions() {
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
            m.speed = generateVelocity()
            /*if (i % 4 == 0) {
				m.speed += mNivel * 2;
			}*/

            m.kill = false
            lstMeteors.add(m)
        }
    }

    private fun generateVelocity(): Double {
        if (Math.random() > 0.5 ) {
            return meteorSpeed / 1.5
        }
        return meteorSpeed
    }

    /**
     * Gera a posi��o inicial na tela para um meteoro
     * @param m
     */
    protected fun meteorPositions(m: SpriteSimpleInfoData) {
        if (Math.random() < 0.5) {
            m.x = -larguraTela / 2
            if (Math.random() < 0.5)
                m.x = larguraTela
            m.y = (Math.random() * alturaTela).toInt()
        } else {
            m.x = (Math.random() * larguraTela).toInt()
            m.y = -alturaTela / 2
            if (Math.random() < 0.5)
                m.y = alturaTela
        }
    }


    override fun run() {
        while (mPlay) {
            handleGame.sendEmptyMessage(0)

            try {
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                // FIXME Auto-generated catch block
                e.printStackTrace()
            }

        }

    }


}