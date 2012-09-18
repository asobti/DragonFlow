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

public class UsersActivity extends ListActivity{
	
	private ArrayList<User> users;
	private Integer usersPage = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		users = new ArrayList<User>();
		new getUsers().execute();
	}
	
	private final class getUsers extends AsyncTask<Void,Void,ArrayList<User>>{
		
		private ProgressDialog progress;
		private APIComm comm = APIComm.getInstance();
		
		protected void onPreExecute() {
			progress = ProgressDialog.show(UsersActivity.this,"DragonFlow","Fetching users...",false);
		}
		
		protected void onPostExecute(final ArrayList<User> users) {
			if (this.progress.isShowing()) {
				this.progress.dismiss();
			}
			
			if (users == null) {
				Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
			} else if (users.size() == 0) {
				Toast.makeText(getApplicationContext(), "No users found", Toast.LENGTH_LONG).show();
			} else {
				String[] usersArray = new String[users.size() + 1];
				
				for(int i = 0; i < users.size(); i++) {
					usersArray[i] = users.get(i).getName();
				}
				
				usersArray[users.size()] = "Load more users";
				
				try {
					setListAdapter(new ArrayAdapter<String>(UsersActivity.this, R.layout.list_user_item, R.id.label, usersArray));
					ListView lv = getListView();
					
					lv.setOnItemClickListener(new OnItemClickListener(){
					   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						   if (id < users.size()) {
							   User user = users.get((int)id);
							   Gson gson = new Gson();
							   String userJson = gson.toJson(user);
							   
							   Intent intent = new Intent(getApplicationContext(),UserActivity.class);
							   intent.putExtra("user", userJson);						   
							   startActivity(intent);
						   } else {
							   new getUsers().execute();
						   }
					   }
				   });
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}
			}
		}
		
		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			// TODO Auto-generated method stub			
			ArrayList<User> newUsers = null;
			try {
				newUsers = comm.fetchUsers(usersPage++);
			} catch (Exception e) {
				Log.i("APP", e.getMessage());
			}
			
			if (newUsers != null) {
				for(User user : newUsers) {
					users.add(user);
				}
			}			
			
			return users;
		}
		
	}	
}
