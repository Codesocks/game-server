package view;

import java.util.ArrayList;

import javafx.application.Platform;
import management.ClientManagement;
import management.GameInvitation;
import server.Client;

class ClientUIUpdater implements Runnable {
	private ClientUIController controller;
	private ClientManagement management;
	private Client client;

	ClientUIUpdater(ClientUIController controller, ClientManagement management) {
		this.controller = controller;
		this.management = management;
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
						
						ArrayList<GameInvitation> invitations = management.getReceivedInvitations();
						if(invitations != null && invitations.size() > 0 && invitations.get(invitations.size() - 1).getCreationTime() > System.currentTimeMillis() - 20000) {
								controller.openGameAcceptationDialogue();
						}
					} catch (Exception e) {
						// Server is currently not available.
					}
				}
			};

			try {
				Thread.sleep(550);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// The following prevents a loading of GUI-elements from a non-JavaFX-Thread.
			// This would cause crashing.
			Platform.runLater(updater);
		}
}}
