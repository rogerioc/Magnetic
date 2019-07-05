package libs.com.rogerio.utils

import android.graphics.Paint


object GamesFX {
    /**
     * Sorteia uma nova cor para uma estrela
     *
     * @return
     */
    fun newColor(): Paint {
        val r = (Math.random() * 255).toInt()
        val g = (Math.random() * 255).toInt()
        val b = (Math.random() * 255).toInt()
        val p = Paint()
        p.setARGB(255, r, g, b)
        return p
    }

}
