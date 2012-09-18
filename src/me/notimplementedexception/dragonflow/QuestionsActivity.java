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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QuestionsActivity extends ListActivity {
	
	private Integer questionPage = 1;		
	private QuestionFilter filter;
	private ArrayList<Question> questions;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        questions = new ArrayList<Question>();
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        filter = gson.fromJson(extras.getString("filter"), QuestionFilter.class);               
        
        new getQuestions().execute(filter);
    }	 
	
	private final class getQuestions extends AsyncTask<QuestionFilter,Void,ArrayList<Question>>	
	{
		private ProgressDialog progressdialog;
		private APIComm comm = APIComm.getInstance();
		
		protected ArrayList<Question> doInBackground(QuestionFilter... params)
		{
			ArrayList<Question> newQuestions = null;
			QuestionFilter filter = params[0];
			switch(filter.type) {
			case NONE:
				newQuestions = comm.fetchQuestions(questionPage++);
				break;
			case TAG:
				newQuestions = comm.fetchQuestionsByTag(filter.value, questionPage++);
				break;
			case USER:
				newQuestions = comm.fetchQuestionsByUser(filter.value, questionPage++);
			case QUERY:
				newQuestions = comm.fetchQuestionsBySearch(filter.value, questionPage++);
			}			
			
			if (newQuestions != null) {
				for(Question que : newQuestions) {
					questions.add(que);
				}
			}
			
			return questions;			
		}
		
		protected void onPostExecute(final ArrayList<Question> questions)
		{			
			// dismiss progress dialog
			if (progressdialog.isShowing()) {
				progressdialog.dismiss();
			}
			
			if (questions == null) {
				Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
			} else if (questions.size() == 0) {
				Toast.makeText(getApplicationContext(), "No questions found", Toast.LENGTH_LONG).show();
			} else {
				String[] queArray = new String[questions.size() + 1];
				for(int i = 0; i < questions.size(); i++) {
					queArray[i] = questions.get(i).getTitle();
				}
				queArray[questions.size()] = "Load more questions...";
				try{
					setListAdapter(new ArrayAdapter<String>(QuestionsActivity.this, R.layout.list_question_item, R.id.label, queArray));
					ListView lv = getListView();
					
					lv.setOnItemClickListener(new OnItemClickListener(){
					   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						   if (id < questions.size()) {
							   Question que = questions.get((int)id);
							   Gson gson = new Gson();
							   String queJson = gson.toJson(que);
							   
							   Intent intent = new Intent(getApplicationContext(),QuestionActivity.class);
							   intent.putExtra("question", queJson);						   
							   startActivity(intent);   
						   } else {
							   // load more
							   new getQuestions().execute(filter);
						   }
						   
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
			progressdialog = ProgressDialog.show(QuestionsActivity.this,"DragonFlow","Fetching questions...",false);
		}		
	}
}
