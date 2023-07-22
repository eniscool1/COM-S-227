package hw4;

import api.Point;
import api.PointPair;
import api.PositionVector;


/**
 * Models a link with a minimum of 2 to a maximum of 6 paths. The following figure shows 6 paths.
 *  \   /
 *   \ /
 * ---.---
 *   / \
 *  /   \
 *
 * The connections are provided to the constructor as an array of 2 to 6 point pairs. Each point pair indicates the two endpoints where the train comes from and goes to.
 * The number of point pairs should be the same as the number of paths.
 * 
 * @author Ian Nelson
 *
 */
public class MultiFixedLink extends AbstractLink {

	
	/**
	 * Holds all pairs of connections for the MultiLink
	 */
	private PointPair[] connections;

	/**
	 * Creates a new MultiFixedLink.
	 * 
	 * @param connections - Array of PointPairs that holds each connection for the MultiLink
	 */
	public MultiFixedLink(PointPair[] connections) {
		this.setConnections(connections);
	}

	@Override
	public void shiftPoints(PositionVector position) {
		
		int travel;
		Point point = null;
		
		
		for (PointPair pair : getConnections()) {
			if (pair.getPointA() == position.getPointB()) {
				point = pair.getPointB();
			} else if (pair.getPointB() == position.getPointB()) {
				point = pair.getPointA();
			} else { 
				continue;
			}
			
			travel = getTravel(point);		
			int endpointIndex = point.getPointIndex();
			
			position.setPointA(point);
			position.setPointB(point.getPath().getPointByIndex(endpointIndex + travel));
			
			break;
		}
		
	}

	@Override
	public Point getConnectedPoint(Point point) {
		
		for (PointPair pair : getConnections()) {
			if (pair.getPointA().getPath() == point.getPath()) {
				return pair.getPointB();
			} else if (pair.getPointB().getPath() == point.getPath()) {
				return pair.getPointA();
			}
		}
		
		// isn't on any of the paths in the link
		return null;
	}

	@Override
	public int getNumPaths() {
		return getConnections().length;
	}

	/**
	 * @return the connections
	 */
	protected PointPair[] getConnections() {
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	protected void setConnections(PointPair[] connections) {
		this.connections = connections;
	}

}
