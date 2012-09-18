package me.notimplementedexception.dragonflow;

import com.google.gson.Gson;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

   public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   
	   String[] home_items = getResources().getStringArray(R.array.home_items);	   
	   this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_home_item, R.id.label, home_items));
	   Bundle extras = getIntent().getExtras();
	   String site = null;
	   if (extras != null) {
		   site = extras.getString("site");
	   }	   
	   
	   ListView lv = getListView();
	   
	   // click listener for the listview on home page
	   lv.setOnItemClickListener(new OnItemClickListener(){
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			   String item = ((TextView)view).getText().toString();
			   
			   Intent i = null;
			   if (item.toLowerCase().equals("questions")) {
				   QuestionFilter filter = new QuestionFilter();
				   Gson gson = new Gson();				   
				   i = new Intent(getApplicationContext(), QuestionsActivity.class);
				   i.putExtra("filter", gson.toJson(filter));
			   } else if (item.toLowerCase().equals("users")) {
				   i = new Intent(getApplicationContext(), UsersActivity.class);
			   } else if (item.toLowerCase().equals("tags")) {
				   i = new Intent(getApplicationContext(), TagsActivity.class);
			   } else if (item.toLowerCase().equals("search")) {
				   i = new Intent(getApplicationContext(), SearchActivity.class);
			   } else if (item.toLowerCase().equals("change site")) {
				   i = new Intent(getApplicationContext(), SiteActivity.class);
			   } else if (item.toLowerCase().equals("about")) {
				   i = new Intent(getApplicationContext(), AboutActivity.class);
			   }
			   
			   if (i != null) {				   
				   startActivity(i);
			   } else {
				   Toast.makeText(getApplicationContext(), "Not Implemented Yet", Toast.LENGTH_SHORT).show();
			   }
			   
		   }
	   });
	   
	   if (site != null) {
		   Toast.makeText(getApplicationContext(), "Site changed to " + site, Toast.LENGTH_SHORT).show();
	   }
   }
   
   public boolean onSearchRequested() {
	   Intent i = new Intent(getApplicationContext(), SearchActivity.class);
	   startActivity(i);
	   return true;
   }
    
}
