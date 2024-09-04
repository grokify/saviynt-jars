// from Saviynt forums: https://forums.saviynt.com/t5/identity-governance/custom-action-external-jar-in-user-update-rule/m-p/29164
// this version works as an External JAR Job.
package com.saviynt.custom.UserPreprocessValidator.validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class CustomActionClass {

	public CustomActionClass() {
	}

	public void CustomMethod() {
		String accessToken = null;
		try {
			URL url = new URL("URL");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			String requestBody = "{\"username\": \"username\", \"password\": \"password\"}";
			byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
			OutputStream os = conn.getOutputStream();
			os.write(input, 0, input.length);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder responseBuilder = new StringBuilder();
			while ((output = br.readLine()) != null) {
				responseBuilder.append(output);
			}
			Gson gson = new Gson();
			TokendataResponse response = gson.fromJson(responseBuilder.toString(), TokendataResponse.class);

			accessToken = response.getAccessToken();
			System.out.println("token---------->" + accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return accessToken;

		List<String> username = null;
		try {
			URL url = new URL("URL");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setDoOutput(true);
			String requestBody = "{\"filtercriteria\":{\"customproperty1\":\"Term\"}}";
			byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
			OutputStream os = conn.getOutputStream();
			os.write(input, 0, input.length);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder responseBuilder = new StringBuilder();
			while ((output = br.readLine()) != null) {
				responseBuilder.append(output);
			}

			Gson gson = new Gson();
			UserListResponse response = gson.fromJson(responseBuilder.toString(), UserListResponse.class);
			List<Userlist> users = response.getUserlist();
			username = users.stream().map(Userlist::getUsername).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String usernames : username) {
			try {
				System.out.println("API Calling for" + usernames);
				URL url = new URL("URL");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Authorization", "Bearer " + accessToken);
				conn.setDoOutput(true);
				String requestBody = "{\"analyticsname\":\"UserCustomAnalytics\",\"attributes\":{\"username\":"
						+ usernames + "}}";
				byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
				OutputStream os = conn.getOutputStream();
				os.write(input, 0, input.length);
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				StringBuilder responseBuilder = new StringBuilder();
				while ((output = br.readLine()) != null) {
					responseBuilder.append(output);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}