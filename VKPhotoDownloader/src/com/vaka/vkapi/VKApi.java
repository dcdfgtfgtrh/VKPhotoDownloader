package com.vaka.vkapi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class VKApi {
	String ACCES_TOKEN, login, password, client_id, display, redirect_uri,
			response_type, ip_h, to, scope, location, apiURI;
	public int uid;
	HttpPost post;
	HttpResponse response;
	boolean isLoggedIn = false;

	public boolean isLogged() {
		return isLoggedIn;
	}

	public VKApi() {
		client_id = "3179114";
		// client_id = "3181305"; test client
		display = "wap";
		redirect_uri = "http://oauth.vk.com/blank.html";
		response_type = "token";
		scope = "photos";
		apiURI = "https://api.vk.com/method/";
	}

	/*
	 * this will obtain acces_token needed for using api methods
	 */
	public void doLogin(String login, String password) throws IOException,
			VKException {
		this.login = login;
		this.password = password;
		HttpClient client = new DefaultHttpClient();

		/*
		 * now form and execute first post. Response will give the login form
		 */
		post = new HttpPost("http://oauth.vk.com/authorize?" + "client_id="
				+ client_id + "&scope=" + scope + "&redirect_uri="
				+ redirect_uri + "&display=" + display + "&response_type="
				+ response_type);
		response = client.execute(post);
		post.abort();
		/*
		 * parse POST form from response
		 */
		HttpEntity ent = response.getEntity();

		BufferedInputStream bis = new BufferedInputStream(ent.getContent());
		byte[] buffer = new byte[1024];
		int count;
		StringBuilder sb = new StringBuilder();
		while ((count = bis.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, count, "utf-8"));
		}
		location = sb.toString();
		/*
		 * parse "ip_h" ant "to" required for log in
		 */
		ip_h = findKey(location, "name=\"ip_h\" value=\"", "\"");
		to = findKey(location, "name=\"to\" value=\"", "\"");

		/*
		 * fill the login form and post it. The response will redirect to
		 * ACCESS_TOKEN obtaining; or to the permissions granting, if user runs
		 * the app for the first time, or the permissions have changed; or to the
		 * login form if either login or password are incorrect
		 */
		post = new HttpPost("https://login.vk.com/?act=login&soft=1&utf8=1"
				+ "&q=1" + "&ip_h=" + ip_h + "&_origin=http://oauth.vk.com"
				+ "&to=" + to + "&email=" + login + "&pass="
				+ URLEncoder.encode(password, "UTF-8"));
		response = client.execute(post);
		post.abort();
		location = response.getFirstHeader("location").getValue();
		post = new HttpPost(location);
		response = client.execute(post);
		post.abort();

		/*
		 * if it is redirect (response contains header "location") we got
		 * redirect to ACCESS_TOKEN obtaining; if not, it is POST form for
		 * permissions granting or the login form
		 */
		if (!response.containsHeader("location")) {
			ent = response.getEntity();
			bis = new BufferedInputStream(ent.getContent());
			buffer = new byte[1024];
			count = 0;
			sb = new StringBuilder();
			while ((count = bis.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, count, "utf-8"));
			}
			location = sb.toString();
			location = findKey(location, " action=\"", "\"");
			/*
			 * recognize login form and throw exception
			 */
			if (location.startsWith("https://login.vk.com/?act=login&soft=1")) {
				throw new VKException(VKException.LOGIN_PW,
						"Не верный логин/пароль");
			}
		} else {
			location = response.getFirstHeader("location").getValue();
		}

		post = new HttpPost(location);
		try {
			response = client.execute(post);
		} catch (IOException e) {
			System.out.println("Couldn`t get token");
			e.printStackTrace();
		}
		post.abort();
		// saving token

		location = response.getFirstHeader("location").getValue();
		ACCES_TOKEN = location.split("#")[1].split("&")[0].split("=")[1];
		uid = Integer
				.parseInt(location.split("#")[1].split("&")[2].split("=")[1]);
		isLoggedIn = true;
	}

	public void doLogout() {
		isLoggedIn = false;
	}

	private String findKey(String source, String patternbegin, String patternend) {
		int startkey = source.indexOf(patternbegin);
		if (startkey > -1) {
			int stopkey = source.indexOf(patternend,
					startkey + patternbegin.length());
			if (stopkey > -1) {
				String key = source.substring(startkey + patternbegin.length(),
						stopkey);
				return key;
			}
		}
		return null;
	}

	public JSONObject apipost(String method, String args) throws JSONException,
			IOException {
		HttpClient client = new DefaultHttpClient();
		// POST | GET as described in api
		HttpGet get = new HttpGet(apiURI + method + args + "&access_token="
				+ ACCES_TOKEN);
		response = client.execute(get);

		byte[] buffer = new byte[1024];
		int count;
		BufferedInputStream bis = new BufferedInputStream(response.getEntity()
				.getContent());
		StringBuilder sb = new StringBuilder();
		while ((count = bis.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, count, "utf-8"));
		}
		System.out.println(sb.toString());
		return new JSONObject(sb.toString());
	}
}
