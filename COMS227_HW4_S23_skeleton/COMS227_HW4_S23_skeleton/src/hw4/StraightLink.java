package hw4;

import api.Point;
import api.PositionVector;

/**
 * Models a fixed link with three paths. The following figure shows the three paths labeled A, B, and C.
 * A     B
 * ---.---
 *     \
 *      \C
 *
 * The paths A and B run in the same direction and C branches away.
 * A train from A always passes to B.
 * A train from B always passes to A.
 * A train from C always passes to A.
 * 
 * @author Ian Nelson
 *
 */
public class StraightLink extends AbstractLink {
	
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
	 * Creates a new StraightLink.
	 * 
	 * @param endpointA - The endpoint of path A.
	 * @param endpointB - The endpoint of path B.
	 * @param endpointC - The endpoint of path C.
	 */
	public StraightLink(Point endpointA, Point endpointB, Point endpointC) {
		this.endpointA = endpointA;
		this.endpointB = endpointB;
		this.endpointC = endpointC;
	}

	@Override
	public void shiftPoints(PositionVector positionVector) {
		
		int travel;
		
		if (positionVector.getPointB() == endpointA) {
			
			travel = getTravel(endpointB);
			int endpointIndex = endpointB.getPointIndex();
			
			positionVector.setPointA(endpointB);
			positionVector.setPointB(endpointB.getPath().getPointByIndex(endpointIndex + travel));
			
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
			return endpointB;
		} else if (point.getPath() == endpointB.getPath() || point.getPath() == endpointC.getPath()) {
			return endpointA;
		}
		
		// point is not on any of the paths in this link
		return null;
	}

	@Override
	public int getNumPaths() {
		return 3;
	}


}
