package model;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		Player p1 = new Player();
		Player p2 = new Player();
		CFGame mygame = new CFGame(p1,p2,8,4);
		boolean player = false;
		
		while(true) {
			Scanner bf = new Scanner(System.in);
			System.out.println("x:...");
			int x = bf.nextInt();
			System.out.println("y:...");
			int y = bf.nextInt();
			
			Move nextMV;
			if(player == false) nextMV = new Move(p1,x, y);
			else nextMV = new Move(p2,x, y);
			player = !player;
			
			mygame.move(nextMV);
			System.out.println(mygame.toString());
		}
	}

}
