package me.notimplementedexception.dragonflow;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

public class Question {
	private Integer id;
	private Integer score;
	private Integer answerCount;
	private String title;
	private ArrayList<String> tags;
	private User owner;
	private Boolean answered;
	private String body;
	
	public Question(JSONObject question) throws JSONException {
		this.id = question.getInt("question_id");
		this.score = question.getInt("score");
		this.answerCount = question.getInt("answer_count");
		this.title = Html.fromHtml(question.getString("title")).toString();
		this.answered = question.getBoolean("is_answered");
		this.tags = new ArrayList<String>();
		// parse array of tags
		JSONArray tagsJson = question.getJSONArray("tags");
		for(int i = 0; i < tagsJson.length(); i++) {
			String tag = tagsJson.getString(i);			
			this.tags.add(tag);
		}
		
		// parse user
		JSONObject userJson = question.getJSONObject("owner");
		User user = new User(userJson);
		this.owner = user;
	}
	
	public void addDetails(JSONObject detailsJson) throws JSONException {
		JSONArray array = detailsJson.getJSONArray("items");
		JSONObject queDetails = array.getJSONObject(0);
		this.body = queDetails.getString("body");
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getBody() {
		return this.body;
	}
}
