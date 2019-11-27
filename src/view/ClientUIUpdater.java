package view;

import javafx.application.Platform;
import server.Client;

class ClientUIUpdater implements Runnable {
	ClientUIController controller;
	Client client;

	ClientUIUpdater(ClientUIController controller) {
		this.controller = controller;
		this.client = controller.getClient();
	}

	@Override
	public void run() {
		while (true) {
			Runnable updater = new Runnable() {
				@Override
				public void run() {
					try {
						client.execute("update");
						controller.openSelectedChat();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// The following prevents a loading of GUI-elements from a non-JavaFX-Thread.
			// This would cause crashing.
			Platform.runLater(updater);
		}
	}
}
