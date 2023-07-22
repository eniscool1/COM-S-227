package hw4;


import api.PointPair;



/**
 * 
 * Models a link with a minimum of 2 to a maximum of 6 paths. The following figure shows 6 paths.
 *  \   /
 *   \ /
 * ---.---
 *   / \
 *  /   \
 *
 * The connections are provided to the constructor as an array of 2 to 6 point pairs. Each point pair describes the two endpoints where the train comes from and goes to.
 * The turn cannot be modified when the train is in the crossing.
 *
 * The number of point pairs should be the same as the number of paths.
 * 
 * @author Ian Nelson
 *
 */
public class MultiSwitchLink extends MultiFixedLink { 

	
	/**
	 * Keeps track of whether or not the train is in the crossing
	 */
	private boolean inCrossing;

	/**
	 * Creates a new MultiSwitchLink.
	 * 
	 * @param connections - Array of PointPairs that holds each connection for the MultiLink
	 */
	public MultiSwitchLink(PointPair[] connections) {
		super(connections);
	}

	@Override
	public void trainEnteredCrossing() {
		inCrossing = true;
	}

	@Override
	public void trainExitedCrossing() {
		inCrossing = false;
	}

	/**
	 * Updates the connection point pair at the given index. The connection cannot be modified (method does nothing)
	 * when the train is currently in (entered but not exited) the crossing.
	 * 
	 * @param pointPair - the new connection
	 * @param i - the index in the PointPair array
	 */
	public void switchConnection(PointPair pair, int i) {
		if (inCrossing) {
			return;
		}
		getConnections()[i] = pair;
		
		
	}

}
