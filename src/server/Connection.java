package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

final class Connection {
	static void send(java.net.Socket socket, String msg) throws IOException {
		PrintWriter printWriter =
		 	    new PrintWriter(
		 	    new OutputStreamWriter(
		 	 	    socket.getOutputStream()));
		printWriter.print(msg);
		printWriter.flush();
	}
	
	static String read(java.net.Socket socket) throws IOException {
		BufferedReader bufferedReader = 
				new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[200];
		
		// Block until message received.
		int characterCount = bufferedReader.read(buffer, 0, 200);
		String msg = new String(buffer, 0, characterCount);
		return msg;
	}
}
