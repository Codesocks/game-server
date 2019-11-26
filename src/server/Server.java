package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import management.ServerManagement;

public class Server extends Thread {
	ServerManagement management = new ServerManagement();
	volatile ArrayList<String> log = new ArrayList<String>();
	
	@Override
	public void run() {
		ServerSocket server;
		addLog("Console (Server Command Line Interface):");
		addLog("Server started listening on Port 3141...");
		
		// Take incoming connections and handle them.
		try {
			server = new ServerSocket(3141);
			
			// einzelner Thread bearbeitet eine aufgebaute Verbindung
			while (true) {
				Thread mulThread = new Thread(new ServerThread(server.accept(), this));
				mulThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a log of all events happened since the latest start of the server.
	 * 
	 * @return Logs.
	 */
	public ArrayList<String> getLogs() {
		return log;
	}
	
	ServerManagement getManagement() {
		return management;
	}
	
	void addLog(String stringToLog) {
		log.add(stringToLog);
	}
}