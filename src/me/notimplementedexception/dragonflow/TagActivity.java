package me.notimplementedexception.dragonflow;

import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TagActivity extends Activity {
	
	protected Tag tag;
	protected TextView txtTitle;
	protected TextView txtCount;
	protected TextView txtWiki;
	protected Button button;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.tag_view);
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			Gson gson = new Gson();
			tag = gson.fromJson(extras.getString("tag"), Tag.class);
		}
		
		txtTitle = (TextView)findViewById(R.id.tagTitle);
		txtTitle.setText(tag.getName());
		
		txtCount = (TextView)findViewById(R.id.tagCount);
		txtCount.setText(String.valueOf(tag.getCount()));
		
		txtWiki = (TextView)findViewById(R.id.tagWiki);
		
		button = (Button)findViewById(R.id.tagBtn);
		button.setText("View questions tagged " + tag.getName());
		
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
				QuestionFilter filter = new QuestionFilter(filterType.TAG, tag.getName());
				Gson gson = new Gson();
				i.putExtra("filter", gson.toJson(filter));
				startActivity(i);
			}
		});
		
		new getWiki().execute(tag.getName());
	}
	
	private class getWiki extends AsyncTask<String, Void, JSONObject>{

		private APIComm comm = APIComm.getInstance();
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject tagWiki = comm.fetchTagWiki(params[0]);
			return tagWiki;
		}
		
		protected void onPostExecute(JSONObject tagWikiJson) {
			try {
				tag.addWiki(tagWikiJson);
				txtWiki.setText(tag.getWiki());
			} catch (Exception e) {
				Log.i("APP", e.getMessage());
				txtWiki.setText("No summary found");
			}
			
		}
		
	}

}
