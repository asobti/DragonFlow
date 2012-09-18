package me.notimplementedexception.dragonflow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	protected TextView txtAbout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about_view);
		
		txtAbout = (TextView)findViewById(R.id.txtAbout);
		txtAbout.setText("Alpha version of DragonFlow \nfor \nCS 338: GUI (Drexel University) \n by \nAyush Sobti (as948@drexel.edu)");
	}
}

