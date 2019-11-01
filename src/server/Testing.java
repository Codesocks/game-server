package server;

import java.io.IOException;

public class Testing {
	public static void main(String[] args) {
		// Starte den Server.
		Server s = new Server();
		Thread server = new Thread(s);
		s.start();
		
		// Verbinde den Client.
		try {
			Client c = new Client();
			c.connect();
			c.send("String2");
			System.out.println(c.read());
			c.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
