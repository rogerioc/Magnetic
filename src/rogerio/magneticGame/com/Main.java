package rogerio.magneticGame.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	Context mContext;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mContext = this;
	        setContentView(R.layout.main);
	        Button txtInicar = (Button) findViewById(R.id.btIniciar);
	        txtInicar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, MagneticGame.class);
					startActivity(intent);
					
				}
			});
	        
	        ImageButton imgAbout = (ImageButton) findViewById(R.id.imgInfo);
	        imgAbout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, About.class);
					startActivity(intent);
					
				}
			});
	    }
}
