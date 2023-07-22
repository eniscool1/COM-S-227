package hw4;


/**
 * 
 * Models a link that connects a single path to nothing. getConnectedPoint() returns null and shiftPoints() does nothing.
 * 
 * @author Ian Nelson
 *
 */
public class DeadEndLink extends AbstractLink {
	
	@Override
	public int getNumPaths() {
		return 1;
	}

}
