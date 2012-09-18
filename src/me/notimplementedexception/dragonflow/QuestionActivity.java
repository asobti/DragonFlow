package me.notimplementedexception.dragonflow;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity{
	
	protected Question question;
	protected TextView txtTitle;
	protected TextView txtBody;
	protected Button ansButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.question_view);
		
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
			Gson gson = new Gson();
			question = gson.fromJson(extras.getString("question"), Question.class);
		}
		
		ansButton = (Button)findViewById(R.id.QueAnsBtn);
		
		ansButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				QuestionFilter filter = new QuestionFilter(filterType.QUESTION, String.valueOf(question.getId()));
				Gson gson = new Gson();
				Intent i = new Intent(getApplicationContext(), AnswersActivity.class);
				i.putExtra("filter", gson.toJson(filter));
				startActivity(i);
			}
		});
		
		txtTitle = (TextView)findViewById(R.id.questionTitle);
		txtTitle.setText(question.getTitle());		
		
		txtBody = (TextView)findViewById(R.id.questionDetail);
		txtBody.setMovementMethod(new ScrollingMovementMethod());
		new getQuestionDetails().execute(question.getId());
	}
	
	private class getQuestionDetails extends AsyncTask<Integer, Void, JSONObject>{
		
		private ProgressDialog progress;
		private APIComm comm = APIComm.getInstance();
		
		protected void onPreExecute() {
			progress = ProgressDialog.show(QuestionActivity.this,"DragonFlow","Fetching question...",true);
		}
		
		protected void onPostExecute(JSONObject queDetailsJson) {
			if (this.progress.isShowing()) {
				this.progress.dismiss();
			}
			
			try {
				question.addDetails(queDetailsJson);
				Spanned body = Html.fromHtml(question.getBody());
				txtBody.setText(body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("APP", e.getMessage());
				Toast.makeText(getApplicationContext(), "Unable to fetch question", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			JSONObject queDetailsJson = comm.fetchQuestionDetails(params[0]);			
			return queDetailsJson;
		}		
		
	}
}
