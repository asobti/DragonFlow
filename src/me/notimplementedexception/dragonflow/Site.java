package me.notimplementedexception.dragonflow;

import org.json.JSONException;
import org.json.JSONObject;

public class Site {
	private String name;
	private String endpoint;
	
	
	public Site(JSONObject obj) throws JSONException {
		this.name = obj.getString("name");
		this.endpoint = obj.getString("api_site_parameter");
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEndpoint() {
		return this.endpoint;
	}
}	
