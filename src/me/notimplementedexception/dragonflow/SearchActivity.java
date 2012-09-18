package me.notimplementedexception.dragonflow;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity{
	private TextView searchInstr;
	private Button searchBtn;
	private EditText searchQuery;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search_view);
		
		searchInstr = (TextView)findViewById(R.id.searchInstr);
		searchInstr.setText("Enter your search terms above and press the 'Search' button");
		
		searchBtn = (Button)findViewById(R.id.searchBtn);
		searchQuery = (EditText)findViewById(R.id.searchQuery);
		
		searchBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String query = searchQuery.getText().toString();
				if (query.trim().equals("")) {
					Toast.makeText(getApplicationContext(), "You must enter a search query", Toast.LENGTH_SHORT).show();
				} else {
					QuestionFilter filter = new QuestionFilter(filterType.QUERY, query);
					Intent i = new Intent(getApplicationContext(), QuestionsActivity.class);
					Gson gson = new Gson();
					i.putExtra("filter", gson.toJson(filter));
					startActivity(i);
				}								
			}			
		});
	}
}
