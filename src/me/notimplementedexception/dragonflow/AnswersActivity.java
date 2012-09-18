package me.notimplementedexception.dragonflow;

import java.util.ArrayList;
import com.google.gson.Gson;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AnswersActivity extends ListActivity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        QuestionFilter filter = gson.fromJson(extras.getString("filter"), QuestionFilter.class);
        new getAnswers().execute(filter);                
    }
	 
	private final class getAnswers extends AsyncTask<QuestionFilter,Void,ArrayList<Answer>>	
	{
		private ProgressDialog progressdialog;
		private APIComm comm = APIComm.getInstance();
		private ArrayList<Answer> answers;
		protected ArrayList<Answer> doInBackground(QuestionFilter... params)
		{
			QuestionFilter filter = params[0];
			switch(filter.type) {
			case QUESTION:
				answers = comm.fetchAnswersByQuestion(filter.value);
				break;			
			case USER:
				answers = comm.fetchAnswersByUser(filter.value);
			}
			
			return answers;			
		}
		
		protected void onPostExecute(final ArrayList<Answer> questions)
		{			
			// dismiss progress dialog
			if (progressdialog.isShowing()) {
				progressdialog.dismiss();
			}
			
			if (answers == null) {
				Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
			} else if (answers.size() == 0) {
				Toast.makeText(getApplicationContext(), "No answers found", Toast.LENGTH_LONG).show();
			} else {
				String[] ansArray = new String[answers.size()];
				for(int i = 0; i < answers.size(); i++) {
					ansArray[i] = answers.get(i).getExcerpt();
				}
				
				try{
					setListAdapter(new ArrayAdapter<String>(AnswersActivity.this, R.layout.list_question_item, R.id.label, ansArray));
					ListView lv = getListView();
					
					lv.setOnItemClickListener(new OnItemClickListener(){
					   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						   Answer ans = answers.get((int)id);
						   Gson gson = new Gson();
						   String ansJson = gson.toJson(ans);
						   
						   Intent intent = new Intent(getApplicationContext(),AnswerActivity.class);
						   intent.putExtra("answer", ansJson);						   
						   startActivity(intent);
					   }
				   });
				} catch (Exception e){
					Log.i("APP", e.getMessage());
				}				
			}
		}		
		
		protected void onPreExecute()
		{
			// show a progress dialog while the questions are fetched
			progressdialog = ProgressDialog.show(AnswersActivity.this,"DragonFlow","Fetching answers...",false);
		}		
	}
}
