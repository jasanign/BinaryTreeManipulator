import java.util.ArrayList;


/**
 * This class saves a path of a tree in a linked list
 * @author Gajjan Jasani
 * @version 11/12/2015
 *
 */
public class PathNode {
	
	/**List of vertex ID ordered by appearance in the path*/
	ArrayList<Integer> path;
	/**Reference to left child*/
	PathNode left;
	/**Reference to right child*/
	PathNode right;
	/**Reference to the parent*/
	PathNode parent;
	/**Reference to the node directly to the left*/
	PathNode sibling;
	/**Tells if the node is last in the level going from right to left*/
	Boolean isLevelEnd;
	/**Tells if the node is the right-most node in the last level*/
	Boolean isLastNode;
	/**Level of node in tree*/
	int level;
	
	/**
	 * Default constructor of PathNode class
	 */
	public PathNode() {
		
		path = new ArrayList<Integer>();
		left = null;
		right = null;
		parent = null;
		sibling = null;
		isLevelEnd = false;
		isLastNode = false;
		level = -1;
	}
	
	public String toString() {
		
		return path.toString();
		
	}

}
