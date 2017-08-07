
/**
 * Driver for Tree class
 * @author Gajjan jasani
 * @version 11/12/2015
 */
public class TreeDriver {

	/**
	 * Entry point for our application
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1){
			
			System.out.println("Usage: file_name");
			System.out.println("Please try again using a "
					+ "valid file name");
			System.exit(0);
		}
		
		Tree myTree = new Tree();
		myTree.readPaths(args[0]); // <-- calls buildCompleteTree
		myTree.setLevelEnd(myTree.root);
		myTree.setSiblingLinks(myTree.root);
		myTree.printTreeLevels();
		
		// for extra tree details uncomment the following 2 lines
		
		// myTree.printTree();
		// myTree.printPathLinks();
		
		
	}

}
