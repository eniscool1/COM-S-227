package hw4;

import api.Crossable;
import api.Point;
import api.PositionVector;


/**
 * Models a link between paths. This class implements Crossable which extends Traversable.
 * 
 * Note to graders:
 * My goal when writing these classes was not to minimize as much code as possible but to follow the is a - has a structure.
 * This led to less inheritance than possible but I feel is clearer to read.
 * I created a helper method in this class (getTravel()) that is used in every shift points method.
 * I also extended multiFixedLink to MultiSwitchLink as they share identical code except for the crossing logic
 * Finally, in AbstractLink, I made just getNumPaths abstract because that's the only method that I implement everywhere
 * so it was the only option to make fully abstract
 * 
 * @author Ian Nelson
 * 
 */
public abstract class AbstractLink implements Crossable {

	public void shiftPoints(PositionVector positionVector) {
	}

	public Point getConnectedPoint(Point point) {
		return null;
	}

	public void trainEnteredCrossing() {	
	}

	public void trainExitedCrossing() {	
	}
	
	public abstract int getNumPaths();
	
	/**
	 * Given an endpoint returns 1 if going in on the high end and -1 if going in on the low end
	 * 
	 * @param point - the endpoint to find direction
	 * @return
	 */
	protected int getTravel(Point point) {
		
		// true if going in on low end
		if (point.getPointIndex() == 0) {
			return 1;
		} else { // true if going in on high end
			return -1;
		}
	}
}
