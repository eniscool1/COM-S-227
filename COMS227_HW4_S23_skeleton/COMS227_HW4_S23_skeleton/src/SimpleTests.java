
import static api.CardinalDirection.*;

import api.Crossable;
import api.Path;
import api.Point;
import api.PointPair;
import api.PositionVector;
import hw4.CouplingLink;
import hw4.DeadEndLink;
import hw4.MultiFixedLink;
import hw4.MultiSwitchLink;
import hw4.StraightLink;
import hw4.SwitchLink;
import hw4.TurnLink;
import simulation.PathTypes;
import simulation.Track;


public class SimpleTests {
	public static void main(String args[]) {
		// set up a simple track with a single path and a dead end link
		Track track = new Track();
		Path path1 = track.addPathType(PathTypes.pathType5, 5, 5, WEST, EAST);
		Crossable link1 = new DeadEndLink();
		path1.setHighEndpointLink(link1);
		
		// set up the position vector (train) to be at the end of the path
		Point highPoint = path1.getHighpoint();
		Point beforeHighPoint = path1.getPointByIndex(highPoint.getPointIndex() - 1);
		PositionVector position = new PositionVector();
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		// test methods of DeadEndLink
		link1.trainEnteredCrossing(); // does nothing
		link1.trainExitedCrossing(); // does nothing
		System.out.println("DeadEndLink has " + link1.getNumPaths() + " paths.");
		System.out.println("Expected 1 path.");
		Point connectedPoint = link1.getConnectedPoint(highPoint);
		System.out.println("Connected point is " + connectedPoint);
		System.out.println("Expected null point.");
		link1.shiftPoints(position); // does nothing
		
		// set up another path to test CouplingLink
		Path path2 = track.addPathType(PathTypes.pathType5, 6, 5, WEST, EAST);
		CouplingLink link2 = new CouplingLink(path1.getHighpoint(), path2.getLowpoint());
		path1.setHighEndpointLink(link2);
		path2.setLowEndpointLink(link2);
		
		// test methods of CouplingLink
		link2.trainEnteredCrossing(); // does nothing
		link2.trainExitedCrossing(); // does nothing
		System.out.println("CouplingLink has " + link2.getNumPaths() + " paths.");
		System.out.println("Expected 2 paths.");
		

		// test that the high end of path 1 is connected to the low end of path 2
		connectedPoint = link2.getConnectedPoint(highPoint);
		if (connectedPoint == path2.getLowpoint()) {
			System.out.println("Path 1 high point is connected to path 2 low point as expected.");
		} else {
			System.out.println("Path 1 high point is not connected to path 2 low point.");
		}

		// test shiftPoints
		link2.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
				&& position.getPointB().getPointIndex() == 1) {
			System.out.println("Position correctly moved to path 2.");
		} else {
			System.out.println("Position not correctly moved to path 2.");
		}
				
		// set up another path to test the 3-path links
		Path path3 = track.addPathType(PathTypes.pathType1, 6, 4, WEST, NORTH);
		
		// test StraightLink, TurnLink, and SwitchLink
		StraightLink link3 = new StraightLink(path1.getHighpoint(), path2.getLowpoint(), path3.getLowpoint());
		TurnLink link4 = new TurnLink(path1.getHighpoint(), path2.getLowpoint(), path3.getLowpoint());
		SwitchLink link5 = new SwitchLink(path1.getHighpoint(), path2.getLowpoint(), path3.getLowpoint());
		connectedPoint = link3.getConnectedPoint(highPoint);
		if (connectedPoint == path2.getLowpoint()) {
			System.out.println("StraightLink: Path 1 high point is connected to path 2 low point as expected.");
		} else {
			System.out.println("StraightLink: Path 1 high point is not connected to path 2 low point.");
		}
		connectedPoint = link4.getConnectedPoint(highPoint);
		if (connectedPoint == path3.getLowpoint()) {
			System.out.println("TurnLink: Path 1 high point is connected to path 3 low point as expected.");
		} else {
			System.out.println("TurnLink: Path 1 high point is not connected to path 3 low point.");
		}
		connectedPoint = link5.getConnectedPoint(highPoint);
		if (connectedPoint == path2.getLowpoint()) {
			System.out.println("TurnLink (false): Path 1 high point is connected to path 2 low point as expected.");
		} else {
			System.out.println("TurnLink (false): Path 1 high point is not connected to path 2 low point.");
		}
		link5.setTurn(true);
		connectedPoint = link5.getConnectedPoint(highPoint);
		if (connectedPoint == path3.getLowpoint()) {
			System.out.println("TurnLink (true): Path 1 high point is connected to path 3 low point as expected.");
		} else {
			System.out.println("TurnLink (true): Path 1 high point is not connected to path 3 low point.");
		}
		
		// other ppls test code from here until 152
		
		
		System.out.println();
		System.out.println("Straight, turn, switch link - shiftPoints test");


		//straight test

		System.out.println("straight test A - B");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link3.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		//turn test

		System.out.println("Turn Test - A - C");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link4.shiftPoints(position);
		if (position.getPointA() == path3.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}


		//switch test
		System.out.println("Switch test, turn false: A - B");
		link5.setTurn(false);

		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link5.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}
		
		System.out.println();
		
		
		System.out.println();
		System.out.println("Straight, turn, switch link - shiftPoints test");


		//straight test

		System.out.println("straight test A - B");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link3.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		//turn test

		System.out.println("Turn Test - A - C");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link4.shiftPoints(position);
		if (position.getPointA() == path3.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}


		System.out.println("Turn test: B-A");

		Point lowPointB = path2.getLowpoint();
		Point afterLowPointB = path2.getPointByIndex(lowPointB.getPointIndex()+1);

		position.setPointA(afterLowPointB);
		position.setPointB(lowPointB);

		link4.shiftPoints(position);
		if (position.getPointA() == path1.getHighpoint()
		&& position.getPointB().getPointIndex() == path1.getHighpoint().getPointIndex()-1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		System.out.println("Turn test: C-A");

		Point lowPointC = path3.getLowpoint();
		Point afterLowPointC = path3.getPointByIndex(lowPointB.getPointIndex()+1);

		position.setPointA(afterLowPointC);
		position.setPointB(lowPointC);

		link4.shiftPoints(position);
		if (position.getPointA() == path1.getHighpoint()
		&& position.getPointB().getPointIndex() == path1.getHighpoint().getPointIndex()-1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		//switch test
		System.out.println("Switch test, turn false: A - B");
		link5.setTurn(false);

		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link5.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		System.out.println("switch test, turn true: A -C");
		link5.setTurn(true);

		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link5.shiftPoints(position);
		if (position.getPointA() == path3.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}



		System.out.println("switch test: B-A");

		link5.setTurn(false);

		position.setPointA(afterLowPointB);
		position.setPointB(lowPointB);

		link5.shiftPoints(position);
		if (position.getPointA() == path1.getHighpoint()
		&& position.getPointB().getPointIndex() == path1.getHighpoint().getPointIndex()-1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		System.out.println("switch test: C-A");

		link5.setTurn(false);


		position.setPointA(afterLowPointC);
		position.setPointB(lowPointC);

		link5.shiftPoints(position);
		if (position.getPointA() == path1.getHighpoint()
		&& position.getPointB().getPointIndex() == path1.getHighpoint().getPointIndex()-1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}


		System.out.println();


		//this next chunk of code goes IN BETWEEN the tests for multifixed and multiswitch. this is bc calling switchConnection for link7 (the multiswitch link) changes what is connected to the high point of path1 (not really sure if this is right or if that's supposed to happen but that code was given by the instructors so ???). heres the code:
		
		
		// End of non-official test code

		
		// set up another path to test the multi-path links
		Path path4 = track.addPathType(PathTypes.pathType2, 6, 4, NORTH, EAST);
		PointPair pair1 = new PointPair(path1.getHighpoint(), path3.getLowpoint());
		PointPair pair2 = new PointPair(path4.getHighpoint(), path2.getLowpoint());
		PointPair[] pairs = {pair1, pair2};
		
		MultiFixedLink link6 = new MultiFixedLink(pairs);
		MultiSwitchLink link7 = new MultiSwitchLink(pairs);
		connectedPoint = link6.getConnectedPoint(path1.getHighpoint());
		if (connectedPoint == path3.getLowpoint()) {
			System.out.println("MultiFixedLink: Path 1 high point is connected to path 3 low point as expected.");
		} else {
			System.out.println("MultiFixedLink: Path 1 high point is not connected to path 3 low point.");
		}
		
		// more unofficial test code
		
		//multifixed link test

		System.out.println("multifixed test: A - C");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link6.shiftPoints(position);
		if (position.getPointA() == path3.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		System.out.println("multifixed test: B - D");
		position.setPointA(afterLowPointB);
		position.setPointB(lowPointB);

		link6.shiftPoints(position);
		if (position.getPointA() == path4.getHighpoint()
		&& position.getPointB().getPointIndex() == path4.getHighpoint().getPointIndex()-1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}
		
		
		
		System.out.println("multiswitch test: A - B");
		position.setPointA(beforeHighPoint);
		position.setPointB(highPoint);

		link7.shiftPoints(position);
		if (position.getPointA() == path2.getLowpoint()
		&& position.getPointB().getPointIndex() == 1) {
		System.out.println("Position correctly moved to path 2.");
		} else {
		System.out.println("Position not correctly moved to path 2.");
		}

		// end of second unofficial test code
		
		link7.switchConnection(new PointPair(path1.getHighpoint(), path2.getLowpoint()), 0);
		connectedPoint = link7.getConnectedPoint(path1.getHighpoint());
		if (connectedPoint == path2.getLowpoint()) {
			System.out.println("MultiSwitchLink: Path 1 high point is connected to path 2 low point as expected.");
		} else {
			System.out.println("MultiSwitchLink: Path 1 high point is not connected to path 2 low point.");
		}
		
		// implemented up to here /\\/\/\/\/\/\/\/\\/\/\/\/\/\/\/\/\/\/\/\\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
		// so done! but need to rearrange everything for polymorphism also needs javaDoc
	}
}
