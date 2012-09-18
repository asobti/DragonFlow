package me.notimplementedexception.dragonflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

/*
 * Responsible for all communications with the API
 * Exists as a singleton
 */
public class APIComm {
	
	private final String endPoint = "http://api.stackexchange.com/2.0/";
	private String site = "site=stackoverflow";	
	private static APIComm instance;
	
	private APIComm() {
		
	}
	
	public static APIComm getInstance() {
		if (instance == null) {
			instance = new APIComm();			
		}
		return instance;
	}
	
	public void setSite(String s) {
		this.site = "site=" + s;
	}
	
	/*
	 * Fetches a list of questions
	 */
	public ArrayList<Question> fetchQuestions(int page) {
		String requestUrl = this.endPoint + "questions?order=desc&sort=activity&page=" + String.valueOf(page) + "&" + this.site;
		JSONObject response = null;
		ArrayList<Question> questions = new ArrayList<Question>();
		
		try {
			response = this.makeRequest(requestUrl);
			JSONArray jsonQuestionsArray = response.getJSONArray("items");
			
			for (int i = 0; i < jsonQuestionsArray.length(); i++) {
				JSONObject queJson = jsonQuestionsArray.getJSONObject(i);
				try {
					Question question = new Question(queJson);
					questions.add(question);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
			
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			questions = null;
		}
		
		return questions;
	}
	
	/*
	 * Fetches questions that contain the tag 'tag'
	 * passed as argument
	 */
	public ArrayList<Question> fetchQuestionsByTag(String tag, int page) {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			tag = URLEncoder.encode(tag, "utf-8");
			String requestUrl = this.endPoint + "search?order=desc&sort=activity&tagged=" + tag + "&page=" + String.valueOf(page) + "&" + this.site;
			JSONObject questionsJson = this.makeRequest(requestUrl);
			JSONArray questionsArray = questionsJson.getJSONArray("items");
			for(int i = 0; i < questionsArray.length(); i++) {
				try {
					JSONObject queJson = questionsArray.getJSONObject(i);
					Question que = new Question(queJson);
					questions.add(que);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			questions = null;			
		}
		
		return questions;
	}
	
	/*
	 * Fetches questions by user
	 */
	public ArrayList<Question> fetchQuestionsByUser(String user, int page) {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			user = URLEncoder.encode(user, "utf-8");
			String requestUrl = this.endPoint + "users/" + user + "/questions?order=desc&sort=activity&page=" + String.valueOf(page) + "&" + this.site;
			JSONObject questionsJson = this.makeRequest(requestUrl);
			JSONArray questionsArray = questionsJson.getJSONArray("items");
			for(int i = 0; i < questionsArray.length(); i++) {
				try {
					JSONObject queJson = questionsArray.getJSONObject(i);
					Question que = new Question(queJson);
					questions.add(que);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			questions = null;			
		}
		
		return questions;
	}	
	
	/*
	 * Fetch questions by search query
	 */
	public ArrayList<Question> fetchQuestionsBySearch(String search, int page) {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			search = URLEncoder.encode(search, "utf-8");
			String requestUrl = this.endPoint + "search?order=desc&sort=activity&intitle=" + search + "&page=" + String.valueOf(page) + "&" + this.site;
			JSONObject questionsJson = this.makeRequest(requestUrl);
			JSONArray questionsArray = questionsJson.getJSONArray("items");
			for(int i = 0; i < questionsArray.length(); i++) {
				try {
					JSONObject queJson = questionsArray.getJSONObject(i);
					Question que = new Question(queJson);
					questions.add(que);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			questions = null;			
		}
		
		return questions;
	}	
	
	
	/*
	 * Fetches details of the question identified
	 * by the argument queid : int
	 */
	public JSONObject fetchQuestionDetails(int queid) {
		String requestUrl = this.endPoint + "questions/" + String.valueOf(queid) + "?order=desc&sort=activity&filter=!9hnGssGO4&" + this.site;
		JSONObject queDetailsJson = null;
		try {
			queDetailsJson = this.makeRequest(requestUrl);
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
		}
		
		return queDetailsJson;
	}
	
	/*
	 * Fetches a list of tags
	 */
	public ArrayList<Tag> fetchTags(int page) {
		String requestUrl = this.endPoint + "tags?order=desc&sort=popular&page=" + String.valueOf(page) + "&" + this.site;
		ArrayList<Tag> tags = new ArrayList<Tag>();
		try {
			JSONObject tagsJson = this.makeRequest(requestUrl);
			JSONArray tagsArray = tagsJson.getJSONArray("items");

			for(int i = 0; i < tagsArray.length(); i++) {
				try {
					JSONObject tagJson = tagsArray.getJSONObject(i);
					Tag tag = new Tag(tagJson);
					tags.add(tag);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());				
				}
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());		
			tags = null;
		}
		
		return tags;
	}
	
	public JSONObject fetchTagWiki(String tag) {		
		JSONObject wiki = null;
		try {
			tag = URLEncoder.encode(tag, "utf-8");
			String requestUrl = this.endPoint + "tags/" + tag + "/wikis?" + this.site;
			JSONObject response = this.makeRequest(requestUrl);
			JSONArray items = response.getJSONArray("items");
			wiki = items.getJSONObject(0);
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
		}
		
		return wiki;
	}
	
	/*
	 * Fetches a list of users
	 */
	public ArrayList<User> fetchUsers(int page) {
		ArrayList<User> users = new ArrayList<User>();
		
		String requestUrl = this.endPoint + "users?order=desc&sort=reputation&page=" + String.valueOf(page) + "&" + this.site;
		try {
			JSONObject usersJson = this.makeRequest(requestUrl);
			JSONArray usersArray = usersJson.getJSONArray("items");
			for(int i = 0; i < usersArray.length(); i++) {
				JSONObject userJson = usersArray.getJSONObject(i);
				User user = new User(userJson);
				users.add(user);
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			users = null;
		}
		
		return users;
	}
	
	public JSONObject fetchUser(Integer id) {
		JSONObject user = null;
		String requestUrl = this.endPoint + "users/" + id + "?order=desc&sort=reputation&" + this.site;
		try {
			JSONObject responseObj = this.makeRequest(requestUrl);
			JSONArray items = responseObj.getJSONArray("items");
			user = items.getJSONObject(0);
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
		}
		
		return user;
	}
	
	/*
	 * Fetches answers by User
	 */
	public ArrayList<Answer> fetchAnswersByUser(String user) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		try {
			user = URLEncoder.encode(user, "utf-8");
			String requestUrl = this.endPoint + "users/" + user + "/answers?order=desc&sort=activity&filter=!*LVw4pKRvjyBRppf&" + this.site;
			JSONObject answersJson = this.makeRequest(requestUrl);
			JSONArray answersArray = answersJson.getJSONArray("items");
			for(int i = 0; i < answersArray.length(); i++) {
				try {
					JSONObject ansJson = answersArray.getJSONObject(i);
					Answer ans = new Answer(ansJson);
					answers.add(ans);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			answers = null;			
		}
		
		return answers;
	}
	
	/*
	 * Fetches answers for a particular question
	 */
	public ArrayList<Answer> fetchAnswersByQuestion(String question) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		try {
			question = URLEncoder.encode(question, "utf-8");
			String requestUrl = this.endPoint + "questions/" + question + "/answers?order=desc&sort=activity&filter=!*LVw4pKRvjyBRppf&" + this.site;
			JSONObject answersJson = this.makeRequest(requestUrl);
			JSONArray answersArray = answersJson.getJSONArray("items");
			for(int i = 0; i < answersArray.length(); i++) {
				try {
					JSONObject ansJson = answersArray.getJSONObject(i);
					Answer ans = new Answer(ansJson);
					answers.add(ans);
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch (Exception e) {
			Log.i("APP", e.getMessage());
			answers = null;			
		}
		
		return answers;
	}
	
	public ArrayList<Site> fetchSites() {
		ArrayList<Site> sites = new ArrayList<Site>();
		
		try {
			String requestUrl = "https://api.stackexchange.com/2.0/sites?pagesize=100";
			JSONObject sitesJson = this.makeRequest(requestUrl);
			JSONArray sitesArray = sitesJson.getJSONArray("items");
			
			for(int i = 0; i < sitesArray.length(); i++) {
				try {
					sites.add(new Site(sitesArray.getJSONObject(i)));
				} catch (Exception e) {
					Log.i("APP", e.getMessage());
				}				
			}
		} catch(Exception e) {
			Log.i("APP", e.getMessage());
			sites = null;
		}
		
		return sites;
	}
	
	
	/*
	 * Makes the actual Http request, receives the Gzipped response
	 * and returns a JSONObject
	 */
	private JSONObject makeRequest(String url) throws IOException, JSONException {
		
		JSONObject response;		
		String jsonString;
		
		HttpClient httpclient = new DefaultHttpClient();
		
		// create the request
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("Accept-Encoding", "gzip");
		
		// execute the request
	    HttpResponse resp = httpclient.execute(request);
	    StatusLine statusLine = resp.getStatusLine();
	    
	    // check the request response status. Should be 200 OK
	    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {	    	
	    	Header contentEncoding = resp.getFirstHeader("Content-Encoding");
	    	InputStream instream = resp.getEntity().getContent();	    	
	    	// was the returned response gzip'ed?
	    	if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
	    		instream = new GZIPInputStream(instream);	    		
	    	}
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	    	StringBuilder responseString = new StringBuilder();
	    	String line;
	    	while ((line = reader.readLine()) != null) {
	    		responseString.append(line);
	    	}
	    	jsonString = responseString.toString();
	    	response = new JSONObject(jsonString);
	    } else {
	    	resp.getEntity().getContent().close();
	    	throw new IOException(statusLine.getReasonPhrase());
	    }		
		
		return response;
	}
}

