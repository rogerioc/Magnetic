package libs.com.rogerio.utils

/*
 * Autor: Rogerio C. Santos - rogerio@rogeriocs.com.br
 * http://www.rogeriocs.com.br
 */

interface AccelerometerListener {

    fun onAccelerationChanged(x: Float, y: Float, z: Float)

    fun onShake(force: Float)


}
