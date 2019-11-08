package server;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import management.ClientManagement;

public class Testing {
	public static void main(String[] args) {
		// Starte den Server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();
		
		ClientManagement m = new ClientManagement();
		m.setCredentials("username", "pwddummy");
		Client c = new Client(m);

		// Create message.
		JSONObject jo = new JSONObject();

		JSONObject jsMode0 = (JSONObject) jo.clone();
		JSONObject jsMode2 = (JSONObject) jo.clone();
		jsMode0.put("mode", "0");
		jsMode2.put("mode", "2");

		// Send login-message.
		c.request(jsMode0);
		
		// Send update-Request.
		c.request(jsMode2);
	}
}
