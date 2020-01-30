package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import management.ServerManagement;

/**
 * Server for the game. Listens on port 3141 and attempts to deal with all
 * incoming connections.
 */
public class Server extends Thread {
	volatile ServerManagement management = new ServerManagement();

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
			addLog("WARNING: This socket is already taken by another instance!");
			addLog("Server will be shut down...");
			e.printStackTrace();
		}
	}

	/**
	 * Returns a log of all events happened since the latest start of the server.
	 * 
	 * @return Logs.
	 */
	public ArrayList<String> getLogs() {
		return management.getLogs();
	}

	/**
	 * Returns the management of this server.
	 * 
	 * @return Server's management.
	 */
	public ServerManagement getManagement() {
		return management;
	}

	/**
	 * Adds the given String to the log.
	 * 
	 * @param stringToLog String to add to the log.
	 */
	void addLog(String stringToLog) {
		management.log(stringToLog);
	}
}