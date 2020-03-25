package com.greatmrpark.app.tournament;

import java.util.ArrayList;
/**
 * Alan Luo
 * Bracket object for tournament fundraiser
 * In short, it contains a list of players and matches.
 * Upon instantiation it generates matches based on Player objects created from input.
 */
public class Bracket {
	private ArrayList<Player> PLAYER_LIST;
	private ArrayList<Match> MATCH_LIST = new ArrayList<Match>();
	
	public Bracket() {
		PLAYER_LIST = new ArrayList<Player>();
		MATCH_LIST = new ArrayList<Match>();
	}
	
	public Bracket(ArrayList<Player> playerList) {
		PLAYER_LIST = playerList;
		generateMatches();
	}
	
	/**
	 * TO-DO
	 * 1. Randomize order of PLAYER_LIST. - DONE
	 * 2. Determine total number of matches in bracket (winners and losers combined).
	 * 3. Determine number of matches in first round.
	 * 4. Determine number of byes in first round.
	 */
	private void generateMatches() {
		Player temp;
		int index; // For random sort
		int matches = 0; // Number of total matches
		int byes = 0; // Number of byes in first round
		int n = 0;
		int first = 0;
		
		// Random sort
		for(int i = 0; i < PLAYER_LIST.size(); i++) {
			index = (int) (Math.random() * PLAYER_LIST.size());
			temp = PLAYER_LIST.get(i);
			PLAYER_LIST.set(i, PLAYER_LIST.get(index));
			PLAYER_LIST.set(index, temp);	
		}
		
		// DEBUG
		for(Player p : PLAYER_LIST) {
			System.out.println(p);
		}
		
		// Determine total matches
		matches = (2 * PLAYER_LIST.size()) - 1;
		
		// Set MATCH_LIST to appropriate size
		for(int i = 0; i < matches; i++) {
			MATCH_LIST.add( new Match(i + 1) );
		}
		
		// Determine first round size
		first = numFirstRound(PLAYER_LIST.size());
		
		// Fills first round matches
		index = 0;
		for( int i = 0; i < first; i++ ) {
			MATCH_LIST.get(i).setPlayerOne(PLAYER_LIST.get(index).getID());
			index++;
		}
		
		for( int i = 0; i < first; i++ ) {
			if(index < PLAYER_LIST.size()) {
				MATCH_LIST.get(i).setPlayerTwo(PLAYER_LIST.get(index).getID());
				index++;
			}
			else {
				MATCH_LIST.get(i).setPlayerTwo(9999);
			}
		}
		
		// DEBUG
		for(Match m:MATCH_LIST) {
			System.out.println(m);
		}
	}
	
	/**
	 * Prints out all player names as opposed to player toString
	 */
	public void printPlayerNames() {
		for(Player p : PLAYER_LIST){
			System.out.println(p.getName());
		}
	}
	
	public Player findPlayer(int ID) {
		for(Player p : PLAYER_LIST) {
			if(p.getID() == ID)
				return p;
		}
		return null;
	}
	
	public Player findPlayer(String name) {
		for(Player p : PLAYER_LIST) {
			if(p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	public Match findMatch(int ID) {
		for(Match m : MATCH_LIST) {
			if(m.id() == ID)
				return m;
		}
		return null;
	}
	
	public Match findMatch(String name) {
		for(Match m : MATCH_LIST) {
			if(name.equals( findPlayer( m.playerOne() ).getName()) || name.equals( findPlayer( m.playerTwo() ).getName()) )
				return m;
		}
		return null;
	}
	
	public String printAllPlayerMatches(int ID) {
		String toReturn = "";
		for(Match m : MATCH_LIST) {
			if(ID == m.playerOne() || ID == m.playerTwo())
				toReturn += m + "\n";
		}
		return toReturn;
	}
	
	public void setWinner(Match m, int ID) {
		if(m.playerOne() == ID || m.playerTwo() == ID) {
			m.setWinner(ID);
		}
		else
			System.out.println("ID not found in match.");
	}
	
	public void printAllMatches() {
		for(Match m : MATCH_LIST) {
			System.out.println(m);
		}
	}
	
	public int matchNum() { 
		return MATCH_LIST.size();
	}
	
	public int playerNum() {
		return PLAYER_LIST.size();
	}
	
	private int smallestNum(int numPlayers) {
		int num  = 1; // num = 2^n
		while(num < numPlayers) {
			num *= 2;
		}
		return num;
	}
	
	private int byes(int numPlayers) {
		return (smallestNum(numPlayers) - numPlayers);
	}
	
	private int numFirstRound(int numPlayers) {
		return (numPlayers + byes(numPlayers))/2;
	}
	
}
