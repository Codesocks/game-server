package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread {

	@Override
	public void run() {
		ServerSocket server;
		try {
			server = new ServerSocket(3141);
			
			// einzelner Thread bearbeitet eine aufgebaute Verbindung
			while (true) {
				ServerThread mulThread = new ServerThread(server.accept());
				mulThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}