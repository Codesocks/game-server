package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	Socket client;

	ServerThread(Socket client) {
		this.client = client;
	}

	public void run() {
		// Bearbeitung einer aufgebauten Verbindung
		try {
			String msg = Connection.read(client);
			Connection.send(client, msg);

			// Fehler bei Ein- und Ausgabe
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (client != null)
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
