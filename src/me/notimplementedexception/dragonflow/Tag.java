package me.notimplementedexception.dragonflow;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;

public class Tag {
	private String name;
	private String wiki;
	private int count;
	
	public Tag(String name) {
		this.name = name;
	}
	
	public Tag(JSONObject tag) throws JSONException {
		this.name = tag.getString("name");
		this.count = tag.getInt("count");
	}
	
	public void addWiki(JSONObject tagWikiJson) throws JSONException {		
		this.wiki = Html.fromHtml(tagWikiJson.getString("excerpt")).toString();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer getCount() {
		return this.count;
	}
	
	public String getWiki() {
		return this.wiki;
	}
}
