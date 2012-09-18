package me.notimplementedexception.dragonflow;

import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity {
	
	protected User user;
	protected TextView txtTitle;
	protected TextView joinDate;
	protected TextView reputation;
	protected TextView txtWebsite;
	protected Button btnQue;
	protected Button btnAns;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user_view);
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			Gson gson = new Gson();
			user = gson.fromJson(extras.getString("user"), User.class);
		}
		
		joinDate = (TextView)findViewById(R.id.userJoinDate);
		txtWebsite = (TextView)findViewById(R.id.userWebsite);
		
		reputation = (TextView)findViewById(R.id.userReputation);
		reputation.setText(String.valueOf(user.getReputation()));
		
		txtTitle = (TextView)findViewById(R.id.userTitle);
		txtTitle.setText(user.getName());
		
		btnQue = (Button)findViewById(R.id.userQueBtn);
		btnQue.setText("Questions by " + user.getName());
		
		btnAns = (Button)findViewById(R.id.userAnsBtn);
		btnAns.setText("Answers by " + user.getName());
		
		btnQue.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				QuestionFilter filter = new QuestionFilter(filterType.USER, String.valueOf(user.getId()));
				Gson gson = new Gson();
				Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
				i.putExtra("filter", gson.toJson(filter));
				startActivity(i);
			}
		});
		
		btnAns.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				QuestionFilter filter = new QuestionFilter(filterType.USER, String.valueOf(user.getId()));
				Gson gson = new Gson();
				Intent i = new Intent(getApplicationContext(), AnswersActivity.class);
				i.putExtra("filter", gson.toJson(filter));
				startActivity(i);				
			}
		});
		
		new userDetails().execute(user.getId());
	}
	
	private class userDetails extends AsyncTask<Integer, Void, JSONObject> {
		
		private APIComm comm = APIComm.getInstance();
		@Override
		protected JSONObject doInBackground(Integer... params) {
			JSONObject userDetails = null;
			try {
				userDetails = comm.fetchUser(params[0]);
			} catch (Exception e) {
				Log.i("APP", e.getMessage());				
			}
			
			return userDetails;
		}
		
		protected void onPostExecute(JSONObject userJson) {
			user.addDetails(userJson);
			joinDate.setText(user.getJoinDate().toString());
			String website = user.getWebsite();
			if (website != null) {
				txtWebsite.setText(website);
				txtWebsite.setLinksClickable(true);				
			}
		}
		
	}

}
