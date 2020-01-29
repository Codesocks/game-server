package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

abstract class Connection {
	Socket socket;
	
	void send(String msg) throws IOException {
		PrintWriter printWriter =
		 	    new PrintWriter(
		 	    new OutputStreamWriter(
		 	 	    socket.getOutputStream()));
		printWriter.print(msg);
		printWriter.flush();
	}
	
	String read() throws IOException {
		BufferedReader bufferedReader = 
				new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[40000];
		
		// Block until message received.
		int characterCount = bufferedReader.read(buffer, 0, 40000);
		String msg = new String(buffer, 0, characterCount);
		return msg;
	}
	
	JSONObject stringToJSONObject(String string) {
		try {
			JSONParser parser = new JSONParser();
			return (JSONObject) parser.parse(string);
		} catch (ParseException e) {
			// Parsing failed. Server is probably offline.
			return null;
		}
	}
}
