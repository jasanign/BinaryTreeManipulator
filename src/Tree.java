//==========================================================================(80)
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedList;


/**
 * @author Gajjan Jasani
 * @version 11/12/2015
 */
public class Tree {

	/** Temporary storage for the paths starting at tempPath[1]*/
	ArrayList<PathNode> tempPath;
	/** Linked list to hold the tree */
	LinkedList<PathNode> myTree;
	/** Root of the tree */
	PathNode root;
	/** Level of a node in the tree */
	int level;
	/** Just an initializer of any int variable */
	final int initialize = -1;
	
	/**
	 * Default constructor for Tree class
	 */
	public Tree() {
		
		tempPath = new ArrayList<PathNode>();
		myTree = new LinkedList<>();
		root = null;
		level = initialize;
		
	}
	
	/**
	 * Reads inputFile given at the command line and places the 
	 * contents of each line into the path field found in each 
	 * PathNode object. The order of PathNode objects is the same
	 * as found in the text file. Adds the new PathNode object to 
	 * tempPath starting at tempPath [ 1 ].
	 * @param inputFile Name of the input file to be read
	 */
	public void readPaths(String inputFile){
		
		try(Scanner myScanner = new Scanner(new File(inputFile))){
			
			while(myScanner.hasNextLine()){
				
				String line = myScanner.nextLine();
				//split the lines at space and create an array out of them
				String[] ints = line.split(" ");
				//To prevent an empty line with a space from breaking 
				// the code	especially at the end of the file
				if(ints.length == 0){
					continue;
				}

				int pathSize = ints.length;
				//PathNode to save path present in current line of input file
				PathNode newPath = new PathNode();
				
				for(int i = 0; i < pathSize; i++){
					int vertex = initialize;
					try{
						vertex = Integer.parseInt(ints[i]);
					} catch (NumberFormatException nfe){
						System.out.println(" Input file has unreadable "
								+ "data...");
						System.out.println("Please check iput file and "
								+ "try again");
						System.exit(0);
					}
					newPath.path.add(vertex);
				}
				tempPath.add(newPath);
			}
		} catch (FileNotFoundException fnfe) {
			
			System.out.println("Input file not found...");
			System.exit(0);
		}
		// Set the last node's isLastNode field to true
		tempPath.get(tempPath.size()-1).isLastNode = true;
		
		// set first node node on the list as the root of the tree
		root = tempPath.get(0);
		
		// build the tree from the list of nodes
		buildCompleteTree(tempPath, 0, root);
		
		// Print all the paths extracted from input file
		//System.out.println("Paths extracted from the input file");
		//System.out.println(tempPath.toString());
		
	}
	
	/**
	 * Recursively builds a complete binary tree. Places PathNode 
	 * objects in tempPath into a complete binary tree in order of
	 * appearance in the text file. The left child of a parent located
	 * at tempPaths[index] is found at tempPath[2*index] and the right
	 * child is found at tempPath[2*index + 1].
	 * @param tempPaths tempPath
	 * @param index index of the current node in tempPath
	 * @param newParent parent of the current node
	 * @return Returns a reference to the node just placed in the tree
	 */
	public PathNode buildCompleteTree(ArrayList<PathNode> tempPaths,
										int index, PathNode newParent){
				
		if (index >= tempPaths.size()){
			
			return null;
		}
		//current node
		PathNode node= tempPaths.get(index);
		//add current node to the tree
		myTree.add(node);
		
		node.left = buildCompleteTree(tempPaths, 2*index+1, node); 
		//if left leaf is present, set this node as its parent
		if(node.left != null){
			node.left.parent = node;
		}
		
		node.right = buildCompleteTree(tempPaths, 2*index+2, node);
		//if right leaf is present, set this node as its parent
		if(node.right != null){
			node.right.parent = node;
		}

		return node;
		
	}
	
	/**
	 * Recursive method that sets isLevelEnd
	 * @param root Root of the tree
	 */
	public void setLevelEnd(PathNode root){
		
		root.isLevelEnd = true;
		// set all the left most leaves as level ends of the tree
		if(root.left != null){
			setLevelEnd(root.left);
		}
	}
	
	/**
	 * Recursive method that sets the sibling link of PathNode objects 
	 * as shown below
	 * @param root Root of the tree
	 */
	public void setSiblingLinks(PathNode root){
		//if root is null, meaning the tree doesn't exist
		if(root == null){
			return;
		}
		
		// when we drop down to the left most leaf in the tree
		if(root.isLevelEnd){	
			//increase level by one
			level++;
			//update the level of the current root
			root.level = level;
			
		//if node is not left most
		} else {
			//set its level to be the same as its sibling's level
			// which is the same as the left most node's level
			root.level = root.sibling.level;
		}
		
		// no left child means no links to setup
		if(root.left == null){
			return;
		}
		
		// if left child is present and if it's not the left most in order
		//(meaning it definitely has a sibling)
		if(root.left != null && !(root.isLevelEnd)){
			
			//set current root's sibling's right child to be the sibling
			//of current root's left child
			//(no need to check if current root's sibling has a right child
			//or not because we built a COMPLETE tree)
			root.left.sibling = root.sibling.right;
			
		}
		
		// if current root has a right child too then
		if(root.right != null){
			
			// set the current root's left child to be the sibling 
			//of the current root's right child
			root.right.sibling = root.left;
			
			//make the current root's left child the new root and
			//repeat the process until we reach the bottom of the tree
			setSiblingLinks(root.left);
			
			//when we reach the bottom-left leaf move over to current root's
			//right child and start setting links
			setSiblingLinks(root.right);
		} else {
			//because setSiblingLinks(root.left); will not execute here
			//we have to set last left (root.left) child's level manually
			if(root.left.isLevelEnd){
				level++;
				root.left.level = level;
			} else {
				root.left.level = root.left.sibling.level;
			}
		}
	}
	
	/**
	 * Prints the path lengths from left to right at each level in the tree
	 */
	public void printTreeLevels(){
		
		// Main list that holds lists holding PathNodes at each level of tree
		ArrayList<ArrayList<PathNode>> levelWiseList = new ArrayList<>();
		
		// creating an ArrayList for each level and adding it to the main list
		for(int i = 0; i <= level; i++){
			levelWiseList.add(new ArrayList<PathNode>());
		}
		
		//traversing through the tree to sort PathNodes with different
		//levels and place them to their correspondingLevelLists
		for(int i = 0; i < myTree.size(); i++){
			
			//current PathNode during traversal
			PathNode temp = myTree.get(i);
			
			//checking the level of the PathNode and finding the list 
			//that corresponds to that level
			ArrayList<PathNode> correspondingLevelList;
			correspondingLevelList = levelWiseList.get(temp.level);
			
			//Adding the current PathNode to its corresponding level list
			correspondingLevelList.add(temp);
		}

		//printing the root PathNode with the special title "Root"
		System.out.println("Root: "+root.path.size()+root.toString());
		
		//Outer for loop for going through one by one level
		for (int i = 1; i < levelWiseList.size(); i++){
			System.out.print("Level "+i+": ");
			ArrayList<PathNode> correspondingLevelList;
			correspondingLevelList = levelWiseList.get(i);
			//Printing all the PathNodes at iTH level
			for (int j = 0; j < correspondingLevelList.size(); j++){
				if(j == correspondingLevelList.size()-1){
					System.out.print(correspondingLevelList.get(j).path.size()+
							correspondingLevelList.get(j).toString());
				} else {
					System.out.print(correspondingLevelList.get(j).path.size()+
						correspondingLevelList.get(j).toString()+" -> ");
				}
			}
			System.out.println();
		}
		
	}
	
	
	//+++++++ Extra helper methods for detailed description of tree +++++++
	
	protected void name() {
		
	} void printTree(){
		System.out.println("Tree paths");
		System.out.println(myTree.toString());
	}
	
	protected void printPathLinks(){
		
		for(int i = 0; i < tempPath.size(); i++){
			PathNode temp = tempPath.get(i);
			if(temp.left != null){
				System.out.println(""+i+" "+temp.toString()+
						  " Left\t-> "+temp.left.toString());
			}
			if(temp.right != null){
				System.out.println(""+i+" "+temp.toString()+
						" Right\t-> "+temp.right.toString());
			}
			if(i != 0){
			System.out.println(""+i+" "+temp.toString()+
					" Parent\t-> "+temp.parent.toString());
			}
			if(temp.sibling != null){
				System.out.println(""+i+" "+temp.toString()+
					" sibling\t-> "+	temp.sibling.toString());
			}
			if(temp.isLevelEnd){
				System.out.println(temp.toString()+" <- is Level End.");
			}
			if(temp.isLastNode){
				System.out.println(temp.toString()+" <- is Last Node.");
			}
			
			System.out.println(temp.toString()+" level -> "
														+temp.level);
		}		
	}
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
