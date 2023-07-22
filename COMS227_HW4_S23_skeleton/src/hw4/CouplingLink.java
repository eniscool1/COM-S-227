package hw4;

import api.Point;
import api.PositionVector;

/**
 * 
 * Models a link between exactly two paths. 
 * When the train reaches the endpoint of one of the paths it passes to the endpoint of the other path next.
 * 
 * @author Ian Nelson
 *
 */
public class CouplingLink extends AbstractLink {
	
	/**
	 * The endpoint of path 1
	 */
	private Point endpoint1;
	
	/**
	 * The endpoint of path 2
	 */
	private Point endpoint2;

	/**
	 * Creates a new CouplingLink.
	 * 
	 * @param endpoint1 - The endpoint of path 1
	 * @param endpoint2 - The endpoint of path 2
	 */
	public CouplingLink(Point endpoint1, Point endpoint2) {
		this.endpoint1 = endpoint1;
		this.endpoint2 = endpoint2;
	}

	@Override
	public void shiftPoints(PositionVector positionVector) {
		int travel;	
		
		if (positionVector.getPointB() == endpoint1) {
			
			travel = getTravel(endpoint2);		
			int endpointIndex = endpoint2.getPointIndex();
			
			positionVector.setPointA(endpoint2);
			positionVector.setPointB(endpoint2.getPath().getPointByIndex(endpointIndex + travel));

		} else if (positionVector.getPointB() == endpoint2) {
			
			travel = getTravel(endpoint1);
			int endpointIndex = endpoint1.getPointIndex();
			
			positionVector.setPointA(endpoint1);
			positionVector.setPointB(endpoint1.getPath().getPointByIndex(endpointIndex + 1));
		}
	}

	@Override
	public Point getConnectedPoint(Point point) {	
	
		if (point == endpoint1) {
			return endpoint2;
		} else if (point == endpoint2){
			return endpoint1;
		}
		return null;
	}

	@Override
	public int getNumPaths() {
		
		return 2;
	}

}
