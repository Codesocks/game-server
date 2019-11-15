package server;

import java.util.Scanner;
import management.ClientManagement;

public class ClientCLI {	
	public static void main(String[] args) {
		ClientManagement m = new ClientManagement();
		m.setCredentials("username", "pwddummy");
		Client c = new Client(m);
		
		System.out.println("Console (Client Command Line Interface):");
		
		while (true) {
			Scanner bf = new Scanner(System.in);
			String input = bf.nextLine();
			
			c.execute(input);
		}
	}
}
