package me.notimplementedexception.dragonflow;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;


public class Answer {
	private Integer answer_id;
	private String body;
	private String excerpt;
	
	
	public Answer(JSONObject ans) throws JSONException {
		this.answer_id = ans.getInt("answer_id");
		this.body = Html.fromHtml(ans.getString("body")).toString();
		this.excerpt = body.substring(0,50) + "...";
	}
	
	public String getExcerpt() {
		return this.excerpt;
	}
	
	public String getBody() {
		return this.body;
	}
}
