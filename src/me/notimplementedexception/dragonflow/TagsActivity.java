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

public class TagsActivity extends ListActivity{
	
	private ArrayList<Tag> tags;
	private Integer tagPage = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tags = new ArrayList<Tag>();
		new getTags().execute();
	}
	
	private final class getTags extends AsyncTask<Void,Void,ArrayList<Tag>>{
		
		private ProgressDialog progress;
		private APIComm comm = APIComm.getInstance();
		
		protected void onPreExecute() {
			progress = ProgressDialog.show(TagsActivity.this,"DragonFlow","Fetching tags...",false);
		}
		
		protected void onPostExecute(final ArrayList<Tag> tags) {
			if (this.progress.isShowing()) {
				this.progress.dismiss();
			}
			
			if (tags == null) {
				Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
			} else if (tags.size() == 0) {
				Toast.makeText(getApplicationContext(), "No tags found", Toast.LENGTH_LONG).show();
			} else {
				String[] tagsArray = new String[tags.size() + 1];
				
				for(int i = 0; i < tags.size(); i++) {
					tagsArray[i] = tags.get(i).getName();
				}
				
				tagsArray[tags.size()] = "Load more tags...";
				
				try {
					setListAdapter(new ArrayAdapter<String>(TagsActivity.this, R.layout.list_tag_item, R.id.label, tagsArray));
					ListView lv = getListView();
					
					lv.setOnItemClickListener(new OnItemClickListener(){
					   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						   if (id < tags.size()) {
							   Tag tag = tags.get((int)id);
							   Gson gson = new Gson();
							   String tagJson = gson.toJson(tag);
							   
							   Intent intent = new Intent(getApplicationContext(),TagActivity.class);
							   intent.putExtra("tag", tagJson);						   
							   startActivity(intent);   
						   } else {
							   new getTags().execute();
						   }						   
					   }
				   });
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}
			}
		}
		@Override
		protected ArrayList<Tag> doInBackground(Void... params) {
			// TODO Auto-generated method stub			
			ArrayList<Tag> newTags = null;
			try {
				newTags = comm.fetchTags(tagPage++);
			} catch (Exception e) {
				Log.i("APP", e.getMessage());
			}
			 
			if (newTags != null) {
				for(Tag tag : newTags) {
					tags.add(tag);
				}
			}
			
			return tags;
		}
		
	}	
}
