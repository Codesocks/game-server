package view;

import server.Client;

public class MainUIChatUpdater implements Runnable {
    MainUI2Controller controller;
    Client client;

    MainUIChatUpdater(MainUI2Controller controller) {
        this.controller = controller;
        this.client = controller.getClient();
    }

    @Override
    public void run() {
        while(true) {
            try {
                client.execute("update");
                controller.openSelectedChat();
            } catch(Exception e) {
                // do nothing.
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
