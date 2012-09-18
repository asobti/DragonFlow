package me.notimplementedexception.dragonflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class AnswerActivity extends Activity{
	
	private TextView ansView;
	private Answer answer;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.answer_view);
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			Gson gson = new Gson();
			answer = gson.fromJson(extras.getString("answer"), Answer.class);
		}
		
		ansView  =(TextView)findViewById(R.id.answerDetail);
		ansView.setMovementMethod(new ScrollingMovementMethod());
		ansView.setText(answer.getBody());
	}
}
