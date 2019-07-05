package rogerio.magneticGame.com

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class Main : Activity() {
    lateinit var mContext: Context
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.main)
        val txtInicar = findViewById<View>(R.id.btIniciar) as Button
        txtInicar.setOnClickListener {
            val intent = Intent(mContext, MagneticGame::class.java)
            startActivity(intent)
        }

        val imgAbout = findViewById<View>(R.id.imgInfo) as ImageButton
        imgAbout.setOnClickListener {
            val intent = Intent(mContext, About::class.java)
            startActivity(intent)
        }
    }
}
