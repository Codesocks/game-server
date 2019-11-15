package server;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import management.ClientManagement;

public class ClientConsole {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ClientManagement m = new ClientManagement();
		m.setCredentials("username", "pwddummy");
		Client c = new Client(m);
		
		System.out.println("Console: ...");
		
		while (true) {
			Scanner bf = new Scanner(System.in);
			String input = bf.nextLine();
			
			// Request.
			JSONObject j = new JSONObject();
			if(input.equals("signin")) {
				j.put("mode", "0");
				System.out.println(c.request(j));
			
			} else if(input.equals("signout")) {
				j.put("mode", "1");
				System.out.println(c.request(j));	
				
			} else if(input.equals("update")) {
				j.put("mode", "2");
				j.put("latestUpdateTime", m.getLatestUpdateTime());
				j.put("messages", m.getLatestMessages());
				
				JSONObject reply = Connection.stringToJSONObject(c.request(j));
				if((Boolean)reply.get("playerUpdateAvailable")) {
					m.setOnlinePlayers((JSONArray) reply.get("onlinePlayers"));
					System.out.println("The list of online players has been updated.");
				}
				if(((JSONArray) reply.get("messages")).size() > 0) {
					m.addReceivedMessages((JSONArray) reply.get("messages"));
					System.out.println("You have ("+ ((JSONArray) reply.get("messages")).size() + ") new messages.");
				}
				System.out.println(reply);
			
			} else if(input.contains("sendmsg ")) {
				input = input.substring(8);
				String[] message = input.split(";");
				m.sendMessage(message[0], message[1]);
				System.out.println("The following message will be send to @" + message[1] + ": '" + message[0] + "'");
				
			} else if(input.contains("setusername ")) {
				input = input.substring(12);
				m.setCredentials(input, "pwddummy");
				System.out.println("Your username was set to '" + input + "'");
			
			} else {
				System.out.println("Invalid input :(");
			}
		}
	}
}
