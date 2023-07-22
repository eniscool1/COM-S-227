package hw2;

import api.PlayerPosition;
import api.BallType;
import static api.PlayerPosition.*;
import static api.BallType.*;

/**
 * Class that models the game of three-cushion billiards.
 * 
 * @author Ian Nelson
 */
public class ThreeCushion {
	
	/**
	 * Holds the current inning player
	 */
	private PlayerPosition currentInningPlayer;
	
	/**
	 * Holds the current inning ball
	 */
	private BallType currentInningBall;
	
	/**
	 * Holds the lag winner
	 */
	private PlayerPosition lagWinner;
	
	/**
	 * Holds player A's ball
	 */
	private BallType playerABall;
	
	/**
	 * Holds player B's ball
	 */
	private BallType playerBBall;
	
	/**
	 * Keeps track of the first ball hit
	 */
	private BallType ball1;
	
	/**
	 * Keeps track of the second ball hit
	 */
	private BallType ball2;
	
	/**
	 * Holds whether the current shot is a break shot or not
	 */
	private boolean isBreakShot;
	
	/**
	 * Holds whether the current shot is active or not (in other words are balls still in motion)
	 */
	private boolean isShotActive;
	
	/**
	 * Holds whether or not a bank shot is possible after three cushion hits (no ball can be hit before three cushions have been hit in order for true)
	 */
	private boolean possibleBankShot;
	
	/**
	 * Holds whether or not the current inning has started
	 */
	private boolean isInningStarted;
	
	/**
	 * Holds whether the current shot has a foul or not
	 */
	private boolean shotHasFoul;
	
	/**
	 * Holds whether the last strike was a cushion or not
	 */
	private boolean isLastHitCushion;
	
	/**
	 * Holds whether the game is over or not (updates only after a shot has ended)
	 */
	private boolean isGameOver;
	
	/**
	 * Keeps track of the current inning number
	 */
	private int currentInning;
	
	/**
	 * Keeps track of player A's score
	 */
	private int playerAScore;
	
	/**
	 * Keeps track of player B's score
	 */
	private int playerBScore;
	
	/**
	 * Keeps track of the number of times the cue ball has hit a cushion in a single shot
	 */
	private int cushionCount;
	
	/**
	 * Keeps track of the required points to win
	 */
	private int pointsToWin;
	

	// TODO: EVERTHING ELSE GOES HERE
	// Note that this code will not compile until you have put in stubs for all
	// the required methods.

	// The method below is provided for you and you should not modify it.
	// The compile errors will go away after you have written stubs for the
	// rest of the API methods.

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * <p>
	 * <tt>Player A*: X Player B: Y, Inning: Z</tt>
	 * <p>
	 * The asterisks next to the player's name indicates which player is at the
	 * table this inning. The number after the player's name is their score. Z is
	 * the inning number. Other messages will appear at the end of the string.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player A%s: %d, Player B%s: %d, Inning: %d %s%s";
		String playerATurn = "";
		String playerBTurn = "";
		String inningStatus = "";
		String gameStatus = "";
		if (getInningPlayer() == PLAYER_A) {
			playerATurn = "*";
		} else if (getInningPlayer() == PLAYER_B) {
			playerBTurn = "*";
		}
		if (isInningStarted()) {
			inningStatus = "started";
		} else {
			inningStatus = "not started";
		}
		if (isGameOver()) {
			gameStatus = ", game result final";
		}
		return String.format(fmt, playerATurn, getPlayerAScore(), playerBTurn, getPlayerBScore(), getInning(),
				inningStatus, gameStatus);
	}

	/**
	 * Creates a new game of three-cushion billiards with a given lag winner and the
	 * predetermined number of points required to win the game. The inning count
	 * starts at 1.
	 * 
	 * @param lagWinner   either player A or B
	 * @param pointsToWin the number of points a player needs to reach for the game
	 *                    to end
	 */
	public ThreeCushion(PlayerPosition lagWinner, int pointsToWin) {
		// TODO Auto-generated constructor stub
		currentInning = 1;
		playerAScore = 0;
		playerBScore = 0;
		this.pointsToWin = pointsToWin;
		this.lagWinner = lagWinner;
	}

	/**
	 * Indicates the given ball has impacted a cushion.
	 * 
	 * A cushion impact cannot happen before a stick strike. If this method is
	 * called before the start of a shot, or after the game ends, it should do
	 * nothing.
	 * 
	 */
	public void cueBallImpactCushion() {
		if (currentInningPlayer != null && isShotActive && !isGameOver) {

			cushionCount++;
			isLastHitCushion = true;
			if (isBreakShot && ball1 == null) {
				foul();
			}
		}
	}

	/**
	 * Indicates the player's cue ball has struck the given ball.
	 * 
	 * A ball strike cannot happen before a stick strike. If this method is called
	 * before the start of a shot, or after the game has ended, it should do
	 * nothing.
	 * 
	 * @param ball struck ball
	 */
	public void cueBallStrike(BallType ball) {
		if (currentInningPlayer != null && isShotActive && !isGameOver) {

			if (cushionCount >= 3 && ball1 == null && ball2 == null) {
				possibleBankShot = true;
			}

			if (ball1 == null) {
				ball1 = ball;
			} else if (ball2 == null && ball != ball1) {
				ball2 = ball;
			}

			if (isBreakShot && ball1 != RED && !shotHasFoul) {
				foul();
			}

		}
		isLastHitCushion = false;
	}

	/**
	 * Indicates the cue stick has struck the given ball. If a shot has not already
	 * begun, indicates the start of a new shot. If this method is called while a
	 * shot is still in progress (i.e., endShot() has not been called for the
	 * previous shot), a foul has been committed Also, if the player strikes
	 * anything other than their own cue ball, they committed a foul.
	 * 
	 * Calling this method signifies both the start of a shot and the start of an
	 * inning, assuming a shot or inning has not already begun, respectively.
	 * 
	 * Even if a foul has been committed, calling this method is considered the
	 * start of a shot. That includes even the case when the player strikes a ball
	 * other than their own cue ball. It is expected that the endShot() method will
	 * be called in any case to indicate the end of the shot.
	 * 
	 * No play can begin until the lag player has chosen who will break (see
	 * lagWinnderChooses). If this method is called before the break is chosen, or
	 * if the game has ended, it should do nothing.
	 * 
	 * @param ball
	 */
	public void cueStickStrike(BallType ball) {
		if (currentInningPlayer != null && !isGameOver) {

			isInningStarted = true;

			if ((ball != currentInningBall || isShotActive) && !shotHasFoul) {
				foul();
			}

			possibleBankShot = false;
			isShotActive = true;
			cushionCount = 0;
			ball1 = null;
			ball2 = null;
		}
	}

	/**
	 * Indicates that all balls have stopped motion. If the shot was valid and no
	 * foul was committed, the player scores 1 point.
	 * 
	 * The shot cannot end before it has started with a call to cueStickStrike or
	 * after it has ended. If this method is called before cueStickStrike, it should
	 * be ignored.
	 * 
	 * If this method is called after the game has ended, it should do nothing.
	 */
	public void endShot() {
		if (currentInningPlayer != null && !isGameOver && isShotActive) {
				
			// if statement is checking for a valid shot
			if (!shotHasFoul && cushionCount >= 3 && ball1 != null && ball2 != null && !isLastHitCushion) {

				if (currentInningPlayer == PLAYER_A) {
					playerAScore++;
				}
				// has to be player B
				else {
					playerBScore++;
				}
				isGameOver();
			}

			// if statement is checking for an invalid shot but not a foul
			else if ((!(cushionCount >= 3 && ball1 != null && ball2 != null) || isLastHitCushion) && !shotHasFoul) {
					
				if (currentInningPlayer == PLAYER_A) {
					currentInningPlayer = PLAYER_B;
					currentInningBall = playerBBall;
				}
				// has to be player B

				else {
					currentInningPlayer = PLAYER_A;
					currentInningBall = playerABall;
				}
				currentInning++;
				isInningStarted = false;
			}

			shotHasFoul = false;
			isShotActive = false;
			isBreakShot = false;
		}
	}

	/**
	 * A foul immediately ends the player's inning, even if the current shot has not
	 * yet ended. When a foul is called, the player does not score a point for the
	 * shot.
	 * 
	 * A foul may also be called before the inning has started. In that case the
	 * player whose turn it was to shot has their inning forfeited and the inning
	 * count is increased by one.
	 * 
	 * No foul can be called until the lag player has chosen who will break (see
	 * lagWinnerChooses()). If this method is called before the break is chosen, it
	 * should do nothing.
	 * 
	 * If this method is called after the game has ended, it should do nothing.
	 */
	public void foul() {

		if (currentInningPlayer != null && !isGameOver) {

			if (currentInningPlayer == PLAYER_A) {
				currentInningPlayer = PLAYER_B;
				currentInningBall = playerBBall;
			}
			// has to be player B
			else {
				currentInningPlayer = PLAYER_A;
				currentInningBall = playerABall;
			}

			if (isInningStarted) {
				// 
				shotHasFoul = true;
			}

			isInningStarted = false;
			currentInning++;
		}
	}
	
	/**
	 * Sets whether the player that won the lag chooses to break (take first shot),
	 * or chooses the other player to break. If this method is called more than once
	 * it should have no effect. In other words, the lag winner can only choose
	 * these options once and may not change their mind afterwards.
	 * 
	 * @param selfBreak if true the lag winner chooses to take the break shot
	 * @param cueball   the lag winners chosen cue ball (either WHITE or YELLOW)
	 */
	public void lagWinnerChooses(boolean selfBreak, BallType cueball) {

		if (getInning() == 1 && !isGameOver && !isBreakShot) {

			if ((cueball == WHITE && (lagWinner == PLAYER_A)) || (cueball == YELLOW && (lagWinner == PLAYER_B))) {
				playerABall = WHITE;
				playerBBall = YELLOW;
			} else {
				playerABall = YELLOW;
				playerBBall = WHITE;
			}

			if ((selfBreak && (lagWinner == PLAYER_A)) || (!selfBreak && (lagWinner == PLAYER_B))) {
				currentInningPlayer = PLAYER_A;
				currentInningBall = playerABall;
			} else {
				currentInningPlayer = PLAYER_B;
				currentInningBall = playerBBall;
			}

			isBreakShot = true;
		}

	}

	/**
	 * Gets the cue ball of the current player. If this method is called in between
	 * innings, the cue ball should be the for the player of the upcoming inning. If
	 * this method is called before the lag winner has chosen a cue ball, the cue
	 * ball is undefined (this method will return null).
	 * 
	 * @return
	 */
	public BallType getCueBall() {
		return currentInningBall;
	}

	/**
	 * Gets the inning number. The inning count starts at 1.
	 * 
	 * @return the inning number
	 */
	public int getInning() {
		return currentInning;
	}

	/**
	 * Gets the current player. If this method is called in between innings, the
	 * current player is the player of the upcoming inning. If this method is called
	 * before the lag winner has chosen to break, the current player is undefined
	 * (this method will return null).
	 * 
	 * @return the current player
	 */
	public PlayerPosition getInningPlayer() {
		return currentInningPlayer;
	}

	/**
	 * Gets the number of points scored by Player A.
	 * 
	 * @return the number of points
	 */
	public int getPlayerAScore() {
		return playerAScore;
	}

	/**
	 * Gets the number of points scored by Player B.
	 * 
	 * @return the number of points
	 */
	public int getPlayerBScore() {
		return playerBScore;
	}

	/**
	 * Returns true if and only if the most recently completed shot was a bank shot.
	 * A bank shot is when the cue ball impacts the cushions at least 3 times and
	 * then strikes both object balls.
	 * 
	 * @return true if shot was a bank shot, false otherwise
	 */
	public boolean isBankShot() {
		if (possibleBankShot && ball1 != null && ball2 != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if and only if this is the break shot (i.e., the first shot of
	 * the game).
	 * 
	 * @return true if this is the break shot, false otherwise
	 */
	public boolean isBreakShot() {
		return isBreakShot;
	}

	/**
	 * Returns true if the game is over (i.e., one of the players has reached the
	 * designated number of points to win).
	 * 
	 * @return true if the game is over, false otherwise
	 */
	public boolean isGameOver() {
		if (playerAScore == pointsToWin || playerBScore == pointsToWin) {
			isInningStarted = false;
			return isGameOver = true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the shooting player has taken their first shot of the inning.
	 * The inning starts at the beginning of the shot (i.e., the shot may not have
	 * ended yet).
	 * 
	 * @return true if the inning has started, false otherwise
	 */
	public boolean isInningStarted() {
		return isInningStarted;
	}

	/**
	 * Returns true if a shot has been taken (see cueStickStrike()), but not ended
	 * (see endShot()).
	 * 
	 * @return true if the shot has been started, false otherwise
	 */
	public boolean isShotStarted() {
		return isShotActive;
	}
}
