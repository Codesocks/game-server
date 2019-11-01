package server;

import java.io.IOException;

import org.json.simple.JSONObject;

public class Testing {	
	public static void main(String[] args) {
		// Starte den Server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();
		
		// Verbinde den Client.
		try {
			// Create message.
			JSONObject jo = new JSONObject();
			jo.put("username", "user1");
			jo.put("pwd", "abc");
			jo.put("mode", "game");
			
			
			// Send message.
			Client c = new Client();
			c.connect();			
			c.send(jo.toString());
			System.out.println(c.read());
			c.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
