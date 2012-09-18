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
import android.widget.AdapterView.OnItemClickListener;

public class SiteActivity extends ListActivity {
	
	private ArrayList<Site> sites;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        sites = new ArrayList<Site>();                       
        
        new getSites().execute();
    }
	
	private final class getSites extends AsyncTask<Void,Void,ArrayList<Site>>
	{
		private ProgressDialog progressdialog;
		private APIComm comm = APIComm.getInstance();
		
		@Override
		protected void onPreExecute()
		{
			progressdialog = ProgressDialog.show(SiteActivity.this,"DragonFlow","Fetching sites...",false);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<Site> sites)
		{
			if (progressdialog.isShowing()) {
				progressdialog.dismiss();
			}
			
			String[] sitesArr = new String[sites.size()];
			
			for(int i = 0; i < sites.size(); i++) {
				sitesArr[i] = sites.get(i).getName();
			}
			
			try{
				setListAdapter(new ArrayAdapter<String>(SiteActivity.this, R.layout.list_question_item, R.id.label, sitesArr));
				ListView lv = getListView();
				
				lv.setOnItemClickListener(new OnItemClickListener(){
				   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {					   
					   Site site = sites.get((int)id);
					   String endpoint = site.getEndpoint();
					   comm.setSite(endpoint);
					   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					   intent.putExtra("site", site.getName());
					   startActivity(intent);					   
				   }
			   });
			} catch (Exception e){
				Log.i("APP", e.getMessage());
			}	
		}
		
		@Override
		protected ArrayList<Site> doInBackground(Void... params) {					
			return comm.fetchSites();
		}
		
	}
}
