package hw4;


import api.Point;
import api.PositionVector;

/**
 * Models a switchable link with three paths. A boolean turn determines which path trains take. By default turn is set to false. The following figure shows the three paths labeled A, B, and C.
 * A     B
 * ---.---
 *     \
 *      \C
 *
 * The paths A and B run in the same direction and C branches away.
 * When turn is true a train from A passes to C.
 * When turn is false a train from A passes to B.
 * A train from B always passes to A.
 * A train from C always passes to A.
 * The turn cannot be modified when the train is in the crossing.
 * 
 * @author Ian Nelson
 *
 */
public class SwitchLink extends AbstractLink {
	
	/**
	 * Hold whether or not the train will turn when coming from path A to B or C
	 */
	private boolean turn;
	
	/**
	 * Keeps track of whether or not the train is in the crossing
	 */
	private boolean inCrossing;
	
	/**
	 * The endpoint of path A
	 */
	private Point endpointA;
	
	/**
	 * The endpoint of path B
	 */
	private Point endpointB;
	
	/**
	 * The endpoint of path C
	 */
	private Point endpointC;

	
	/**
	 * 
	 * Creates a new SwitchLink.
	 * 
	 * @param endpointA - The endpoint of path A.
	 * @param endpointB - The endpoint of path B.
	 * @param endpointC - The endpoint of path C.
	 */
	public SwitchLink(Point endpointA, Point endpointB, Point endpointC) {
		this.endpointA = endpointA;
		this.endpointB = endpointB;
		this.endpointC = endpointC;
	}

	@Override
	public void shiftPoints(PositionVector positionVector) {
		
		int travel;
	
		if (positionVector.getPointB() == endpointA) {
			
			Point point;
			
			if (turn == true) {
				point = endpointC;
			} else {
				point = endpointB;
			}
			
			
			travel = getTravel(point);
			int endpointIndex = point.getPointIndex();
			
			positionVector.setPointA(point);
			positionVector.setPointB(point.getPath().getPointByIndex(endpointIndex + travel));
			
		} else if (positionVector.getPointB() == endpointB || positionVector.getPointB() == endpointC) {
			
			travel = getTravel(endpointA);
			int endpointIndex = endpointA.getPointIndex();
			
			positionVector.setPointA(endpointA);
			positionVector.setPointB(endpointA.getPath().getPointByIndex(endpointIndex + travel));
		}
		
	}

	@Override
	public Point getConnectedPoint(Point point) {
		
		// if point is on path A
		if (point.getPath() == endpointA.getPath()) {
			if (turn == true) {
				return endpointC;
			} else {
				return endpointB;
			}
		} else if (point.getPath() == endpointB.getPath() || point.getPath() == endpointC.getPath()) {
			return endpointA;
		}
		
		// point is not on any of the paths in this link
		return null;
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
	 * Sets Whether the turn is on (true) or off (false) 
	 * @param b - the new value of turn
	 */
	public void setTurn(boolean b) {
		if (inCrossing) {
			return;
		}
		turn = b;
	}

	@Override
	public int getNumPaths() {
		return 3;
	}
}
