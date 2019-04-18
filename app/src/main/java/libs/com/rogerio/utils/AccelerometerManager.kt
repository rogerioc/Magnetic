package libs.com.rogerio.utils

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


object AccelerometerManager {
    /** Accuracy configuration  */
    private var threshold = 2
    private var interval = 1000

    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    // you could use an OrientationListener array instead
    // if you plans to use more than one listener
    private var listener: AccelerometerListener? = null

    /** indicates whether or not Accelerometer Sensor is supported  */
    private var supported: Boolean? = null
    /** indicates whether or not Accelerometer Sensor is running  */
    /**
     * Returns true if the manager is listening to orientation changes
     */
    var isListening = false
        private set
    private var context: Context? = null
    private var mAccType: AccType? = null

    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    val isSupported: Boolean
        get() {
            if (supported == null) {
                if (context != null) {
                    sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                    val sensors = sensorManager!!.getSensorList(
                            accType())
                    supported = sensors.size > 0
                } else {
                    supported = java.lang.Boolean.FALSE
                }
            }
            return supported!!
        }

    /**
     * The listener that listen to events from the accelerometer listener
     */
    private val sensorEventListener = object : SensorEventListener {

        private var now: Long = 0
        private var timeDiff: Long = 0
        private var lastUpdate: Long = 0
        private var lastShake: Long = 0

        private var x = 0f
        private var y = 0f
        private var z = 0f
        private var lastX = 0f
        private var lastY = 0f
        private var lastZ = 0f
        private var force = 0

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            // use the event timestamp as reference
            // so the manager precision won't depends
            // on the AccelerometerListener implementation
            // processing time
            now = event.timestamp

            x = event.values[0]
            y = event.values[1]
            z = event.values[2]

            // if not interesting in shake events
            // just remove the whole if then else bloc
            if (lastUpdate == 0L) {
                lastUpdate = now
                lastShake = now
                lastX = x
                lastY = y
                lastZ = z
            } else {
                timeDiff = now - lastUpdate
                if (timeDiff > 0) {
                    val `val` = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff
                    force = `val`.toInt()
                    if (force > threshold) {
                        if (now - lastShake >= interval) {
                            // trigger shake event
                            listener!!.onShake(force.toFloat())
                        }
                        lastShake = now
                    }
                    lastX = x
                    lastY = y
                    lastZ = z
                    lastUpdate = now
                }
            }
            // trigger change event
            listener!!.onAccelerationChanged(x, y, z)
        }
    }

    fun setContextAcc(con: Context, actype: AccType) {
        context = con
        mAccType = actype
    }

    /**
     * Unregisters listeners
     */
    fun stopListening() {
        isListening = false
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager!!.unregisterListener(sensorEventListener)
            }
        } catch (e: Exception) {
        }

    }

    private fun accType(): Int {
        when (mAccType) {
            AccType.Magnetic -> return Sensor.TYPE_MAGNETIC_FIELD
            else -> return Sensor.TYPE_ACCELEROMETER
        }

    }

    /**
     * Configure the listener for shaking
     * @param threshold
     * minimum acceleration variation for considering shaking
     * @param interval
     * minimum interval between to shake events
     */
    fun configure(threshold: Int, interval: Int) {
        AccelerometerManager.threshold = threshold
        AccelerometerManager.interval = interval
    }

    /**
     * Registers a listener and start listening
     * @param accelerometerListener
     * callback for accelerometer events
     */
    fun startListening(
            accelerometerListener: AccelerometerListener) {
        sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager!!.getSensorList(
                accType())
        if (sensors.size > 0) {
            sensor = sensors[0]
            isListening = sensorManager!!.registerListener(
                    sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME)
            listener = accelerometerListener
        }
    }

    /**
     * Configures threshold and interval
     * And registers a listener and start listening
     * @param accelerometerListener
     * callback for accelerometer events
     * @param threshold
     * minimum acceleration variation for considering shaking
     * @param interval
     * minimum interval between to shake events
     */
    fun startListening(
            accelerometerListener: AccelerometerListener,
            threshold: Int, interval: Int) {
        configure(threshold, interval)
        startListening(accelerometerListener)
    }
}


