package server;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Testing {
	public static void main(String[] args) {
		// Starte den Server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();

		// Create message.
		JSONObject jo = new JSONObject();
		jo.put("latestUpdateTime", "0");
		jo.put("credentials", getLoginCredentials());

		JSONObject jsMode0 = (JSONObject) jo.clone();
		JSONObject jsMode2 = (JSONObject) jo.clone();
		jsMode0.put("mode", "0");
		jsMode2.put("mode", "2");

		// Send login-message.
		sendRequest(jsMode0);
		
		// Send update-Request.
		sendRequest(jsMode2);
	}

	//  Ersetze durch non-static.
	static JSONArray getLoginCredentials() {
		JSONArray credentials = new JSONArray();
		credentials.add("username");
		credentials.add("pwd2");

		return credentials;
	}

	/*static String sendRequest(JSONObject jo) {
		String reply = "";

		try {
			Client c = new Client();
			c.connect();
			c.send(jo.toString());
			reply = c.read();
			c.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[CLIENT] Reply:			" + reply);
		return reply;
	}*/
}
