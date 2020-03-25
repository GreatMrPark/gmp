package com.greatmrpark.app.tournament;

/**
 * Alan Luo
 * Match object for use within Bracket.
 * Has two booleans that mark characteristics of the match.
 * These determine whether or not a match is finished and if it's within the winner or loser's bracket.
 * Match objects are never interacted with directly, all modifying goes through Bracket.
 * Player objects are never actually used, instead player IDs are used to reference to players.
 */
public class Match {
	private int PLAYER_ONE_ID;
	private int PLAYER_TWO_ID;
	private int MATCH_ID;
	private boolean IS_MATCH_FINISHED;
	private int WINNER; // Is positive if PLAYER_ONE wins, negative if PLAYER_TWO wins.
	private String SCORE;
	private boolean IS_WINNER;
	
	public Match(int ID) {
		MATCH_ID = ID;
		setScore("0-0");
	}
	
	public void setPlayerOne(int ID) {
		PLAYER_ONE_ID = ID;
	}
	
	public int playerOne() {
		return PLAYER_ONE_ID;
	}
	
	public void setPlayerTwo(int ID) {
		PLAYER_TWO_ID = ID;
	}
	
	public int playerTwo() {
		return PLAYER_TWO_ID;
	}
	
	public int id() {
		return MATCH_ID;
	}
	
	public void setID(int ID) {
		MATCH_ID = ID;
	}
	
	public boolean isFinished() {
		return IS_MATCH_FINISHED;
	}
	
	public void setIsFinished(boolean status) {
		IS_MATCH_FINISHED = status;
	}

	public int winner() {
		return WINNER;
	}

	public void setWinner(int w) {
		WINNER = w;
	}

	public String score() {
		return SCORE;
	}

	public void setScore(String s) {
		SCORE = s;
	}

	public boolean isWinnerLoser() {
		return IS_WINNER;
	}

	public String toString() {
		String winner;
		String score;
		if(winner() == 9999) {
			winner = "TBD";
			score = "TBD";
		}
		else {
			winner = "" + winner();
			score = score();
		}

		String formatted = ( "Match ID: " + MATCH_ID + "\n"
							+"Player One:" + PLAYER_ONE_ID + "\n"
							+"Player Two:" + PLAYER_TWO_ID + "\n"
							+"Winner:" + WINNER +"\n"
							+"Score:" + SCORE + "\n"
							);
		return formatted;
	}
}
