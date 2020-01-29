package view;

import javafx.application.Platform;

class ServerUIUpdater implements Runnable {
	private ServerUIController controller;

	ServerUIUpdater(ServerUIController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		while (true) {
			Runnable updater = new Runnable() {
				@Override
				public void run() {
					try {
						controller.loadLog();
						controller.loadUser();
						controller.loadGames();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Platform.runLater(updater);
		}
	}

}
