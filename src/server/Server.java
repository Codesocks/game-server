package server;

import java.io.IOException;
import java.net.ServerSocket;
import management.ServerManagement;

public class Server extends Thread {
	ServerManagement management = new ServerManagement();
	
	@Override
	public void run() {
		ServerSocket server;
		
		try {
			server = new ServerSocket(3141);
			
			// einzelner Thread bearbeitet eine aufgebaute Verbindung
			while (true) {
				ServerThread mulThread = new ServerThread(server.accept(), this);
				mulThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ServerManagement getManagement() {
		return management;
	}
}