package com.greatmrpark.app.tournament;

/**
 * Alan Luo
 * Player objects for use within Bracket
 * Fundraiser participants are assigned an ID, this ID will identify which instance is "theirs"
 * Wins and Losses will determine if they proceed in winner's bracket or loser's bracket.
 */
public class Player {
	private int PLAYER_ID;
	private String PLAYER_NAME;
	private int PLAYER_WINS;
	private int PLAYER_LOSSES;
	private int TIME_SINCE_LAST_MATCH;
	
	public Player() {
		setID(9999);
		setName("John Doe");
		setWins(9999);
		setLosses(9999);
		setTimeSinceLast(9999);
	}
	
	public Player(String name, int id) {
		setID(id);
		setName(name);
		setWins(0);
		setLosses(0);
		setTimeSinceLast(0);
	}

	public int getID() {
		return PLAYER_ID;
	}

	public void setID(int id) {
		PLAYER_ID = id;
	}

	public String getName() {
		return PLAYER_NAME;
	}

	public void setName(String name) {
		PLAYER_NAME = name;
	}

	public int getWins() {
		return PLAYER_WINS;
	}

	public void setWins(int wins) {
		PLAYER_WINS = wins;
	}

	public int getLosses() {
		return PLAYER_LOSSES;
	}

	public void setLosses(int losses) {
		PLAYER_LOSSES = losses;
	}

	public int getTimeSinceLast() {
		return TIME_SINCE_LAST_MATCH;
	}

	public void setTimeSinceLast(int time) {
		TIME_SINCE_LAST_MATCH = time;
	}
	
	public String toString() {
		String formatted = ( "ID: " + getID() + "\n"
							+"Name: " + getName() + "\n"
							+"Score (W-L): " + getWins() + "-" +getLosses() + "\n"
							);
		return formatted;
	}
	
}
