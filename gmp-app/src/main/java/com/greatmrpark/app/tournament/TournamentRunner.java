package com.greatmrpark.app.tournament;

import java.util.ArrayList;
import java.util.Scanner;

public class TournamentRunner {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		Bracket bracket;
		ArrayList<Player> initialPlayerList = new ArrayList<Player>();
		boolean IS_RUNNING = true;
		String newName;
		String option1;
		String option2;
		int i = 0;
		
		/** Initial loop to add players
		 * Takes names until FINISH is entered and registration closes.
		 * Then instantiates bracket with first round matches from list.
		 */
		while(IS_RUNNING) {
			System.out.print("Enter player's name or FINISH to close registration: ");
			newName = keyboard.nextLine();
			if(newName.equals("FINISH"))
				IS_RUNNING = false;
			else {
				initialPlayerList.add(new Player(newName, initialPlayerList.size() + 1));
			}
		}
		IS_RUNNING = true;
		bracket = new Bracket(initialPlayerList);
		
		/** Main loop - runs commands
		 * 1. Print all matches - DONE
		 * 2. Print all players
		 * 3. Assign winner to a match by ID
		 * 4. Search for player
		 * 5. Search for match
		 */
		while(IS_RUNNING) {
			System.out.println("1 - Print all matches");
			System.out.println("2 - Print all players");
			System.out.println("3 - Set winner of match");
			System.out.println("4 - Search for player");
			System.out.println("5 - Search for match");
			System.out.print("Command: ");
			option1 = keyboard.next();
			keyboard.nextLine();
			
			switch(option1) {
				case "1" :
					bracket.printAllMatches();
					continue;
				case "2" :
					for(Player p:initialPlayerList)
						System.out.println(p);
					continue;
				case "3" :
					System.out.print("Match ID: ");
					if(keyboard.hasNextInt()) {
						option1 = keyboard.nextLine();
						Match m = bracket.findMatch(Integer.parseInt(option1));
						System.out.println(m);
						System.out.print("Winner's ID: ");
						option1 = keyboard.next();
						keyboard.nextLine();
						m.setWinner(Integer.parseInt(option1)); // Method already ensures that ID passed is in the match.
						continue;
					}
					else {
						System.out.println("Invalid input.");
						continue;
					}
				case "4" :	
					System.out.print("Player name or player ID: ");
					option1 = keyboard.nextLine();
					keyboard.nextLine();
					if(keyboard.hasNextInt()) {
						Player p = bracket.findPlayer(Integer.parseInt(option1));
						System.out.println(p);
						System.out.print("Display matches? (Y or any other input): ");
						option1 = keyboard.nextLine();
						keyboard.nextLine();
						if(option1.equals("Y")) {
							System.out.println(bracket.printAllPlayerMatches(p.getID()));
							continue;
						}
						else {
							continue;
						}
					}
					else {
						Player p = bracket.findPlayer(option1);
						System.out.println(p);
						System.out.println(bracket.printAllPlayerMatches(p.getID()));
						continue;
					}
					
				case "5" :
					System.out.print("Name of player in match or match ID: ");
					option1 = keyboard.nextLine();
					keyboard.nextLine();
					if(keyboard.hasNextInt()) {
						Match m = bracket.findMatch(Integer.parseInt(option1));
						System.out.println(m);
						continue;
					}
				default :
					System.out.println("Invalid input.");
					continue;
			}
		}
		
	}
}
