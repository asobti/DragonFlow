package me.notimplementedexception.dragonflow;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private Integer id;
	private String name;
	private Integer reputation;
	private String website;
	private Date joinDate;
	
	public User(JSONObject user) throws JSONException {
		this.id = user.getInt("user_id");
		this.name = user.getString("display_name");
		this.reputation = user.getInt("reputation");
	}
	
	public void addDetails(JSONObject details) {
		try {
			this.website = details.getString("website_url");
		} catch (JSONException e) {
			this.website = null;
		}
		
		try {
			this.joinDate = new Date(details.getLong("creation_date") * 1000);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			this.joinDate = null;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer getId() {
		return this.id;		
	}
	
	public Integer getReputation() {
		return this.reputation;
	}
	
	public String getWebsite() {
		return this.website;
	}
	
	public Date getJoinDate() {
		return this.joinDate;
	}
}
