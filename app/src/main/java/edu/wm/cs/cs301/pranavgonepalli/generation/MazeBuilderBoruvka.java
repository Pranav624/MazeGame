package edu.wm.cs.cs301.pranavgonepalli.generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());
	
	//This will store all the edge weights for each wallboard.
	int[][][] edgeWeights;
	
	//This will store all the groups of cells in the maze, with each group being an arrayList
	//in cellsList.
	ArrayList<ArrayList<Integer>> groupsList;

	public MazeBuilderBoruvka() {
		// TODO Auto-generated constructor stub
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}

	/**
	 *   First make an arraylist of arraylists, with each arraylist in the arraylist containing each cell
		 in the maze.
		 Then, we have a while loop that stops when the length of arraylist is 1 because that means all
		 the cells are connected.
		 Then, we make a variable called wallsToBeRemoved which will contain all the walls that we have to 
		 remove.
		 Then, we'll go through the big arraylist and and get all the wallboard surrounding the group of cells 
		 in the arraylist within using getWalls(cells arraylist).
		 Then, we'll go through the wallboards and pick the one with the min weight using 
		 getEdgeWeight() and add that wallboard to wallsToBeRemoved.
		 Then, outside of the for-loop we go through all the wallsToBeRemoved and remove them, while also
		 adding an incrementing number to the beginning of all the arraylists that contain the cells that the 
		 wallboard separated.
		 This will help keep track of all the groups that we have to merge later.
		 After that, we combine the arraylists within the big arraylist using makeGroups() to make groups, which
		 are the cells in the maze that don't have walls between them.
	 */
	@Override
	protected void generatePathways() {
		
		//Sets all the edge weights.
		setEdgeWeights();
		//Initializes groupLists with each group containing one cell.
		groupsList = new ArrayList<ArrayList<Integer>>();
		for(int cellNum = 0; cellNum < width*height; cellNum++) {
			ArrayList<Integer> group = new ArrayList<Integer>();
			group.add(cellNum);
			groupsList.add(group);
		}
		//Stops when groupsList is size one because then the whole maze is connected.
		while(groupsList.size() > 1) {
			ArrayList<Wallboard> wallsToBeRemoved = new ArrayList<Wallboard>();
			for(int group = 0; group < groupsList.size(); group++) {
				//Gets all the walls of the group.
				ArrayList<Wallboard> walls = getWalls(groupsList.get(group));
				int min = 100;
				Wallboard minWall = null;
				//Finds the wall with the minimum weight and adds it to wallsToBeRemoved.
				for(int wall = 0; wall < walls.size(); wall++) {
					int x = walls.get(wall).getX();
					int y = walls.get(wall).getY();
					CardinalDirection cd = walls.get(wall).getDirection();
					int weight = getEdgeWeight(x, y, cd);
					if(weight < min) {
						min = weight;
						minWall = walls.get(wall);
					}
				}
				wallsToBeRemoved.add(minWall);
			}
			
			int num = -1;
			//Goes through wallsToBeRemoved and removes them.
			for(int wall = 0; wall < wallsToBeRemoved.size(); wall++) {
				int x = wallsToBeRemoved.get(wall).getX();
				int y = wallsToBeRemoved.get(wall).getY();
				CardinalDirection cd = wallsToBeRemoved.get(wall).getDirection();
				//Checks to see if a wall is there because it could've been removed in a previous iteration.
				if(floorplan.hasWall(x, y, cd)) {
					//Gets the cell number and the number of the neighboring cell.
					int cellNum = y*width + x;
					int neighborNum = wallsToBeRemoved.get(wall).getNeighborY()*width + 
							wallsToBeRemoved.get(wall).getNeighborX();
					ArrayList<Integer> cellGroup = new ArrayList<Integer>();
					ArrayList<Integer> neighborGroup = new ArrayList<Integer>();
					//Makes cellGroup and neighborGroup so we can make making groups easier.
					for(int group = 0; group < groupsList.size(); group++) {
						if(groupsList.get(group).contains(cellNum)) {
							cellGroup = groupsList.get(group);
						}
						if(groupsList.get(group).contains(neighborNum)) {
							neighborGroup = groupsList.get(group);
						}
					}
					//Checks to make sure the cell and it's neighbor aren't in the same group.
					if(neighborGroup.get(0) != cellGroup.get(0)) {
						floorplan.deleteWallboard(wallsToBeRemoved.get(wall));
						//Different situations for making groups.
						if(neighborGroup.get(0) < 0 && cellGroup.get(0) > 0) {
							cellGroup.add(0, neighborGroup.get(0));
						}
						else if(neighborGroup.get(0) > 0 && cellGroup.get(0) < 0) {
							neighborGroup.add(0, cellGroup.get(0));
						}
						else if(neighborGroup.get(0) < 0 && cellGroup.get(0) < 0) {
							int replace = neighborGroup.get(0);
							neighborGroup.set(0, cellGroup.get(0));
							//Go through the groups again so every cell in the group
							//can be changed to be included in the new group.
							for(int tempGroup = 0; tempGroup < groupsList.size(); tempGroup++) {
								if(groupsList.get(tempGroup).get(0) == replace) {
									groupsList.get(tempGroup).set(0, cellGroup.get(0));
								}
							}
						}
						else {
							cellGroup.add(0, num);
							neighborGroup.add(0, num);
							num--;
						}
						//Make the groups in groupsList equal to the groups with the number
						//at the front so we can make groups.
						for(int group = 0; group < groupsList.size(); group++) {
							if(groupsList.get(group).contains(cellNum)) {
								groupsList.set(group, cellGroup);
							}
							if(groupsList.get(group).contains(neighborNum)) {
								groupsList.set(group, neighborGroup);
							}
						}
					}
				}
			}
			//Make groups.
			makeGroups();
		}
	}
	
	/**
	 *   This method will combine all the connected cells into groups.
		 We go through each arraylist in the big arraylist and ad see what the value in index 0 is.
		 We then put all the cells with value i in index 0 in index i of a new arraylist.
		 This way, all the cells that are supposed to be grouped together, as we ensured in generatePathways(),
		 will be grouped together.
	 */
	private void makeGroups() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		//Go through group numbers, max number of groups is size of groupsList.
		for(int num = -1; num > -groupsList.size(); num--) {
			//Make a temp group to store the group, which we will add to temp.
			ArrayList<Integer> tempGroup = new ArrayList<Integer>();
			//Go through all the groups in groupsList.
			for(int group = 0; group < groupsList.size(); group++) {
				//Checks to see if the first value is num.
				if(groupsList.get(group).get(0) == num) {
					//If it is, we go through the group and add all the cells to tempGroup.
					for(int cell = 1; cell < groupsList.get(group).size(); cell++) {
						tempGroup.add(groupsList.get(group).get(cell));
					}
				}
			}
			if(tempGroup.size() > 0) {
				temp.add(tempGroup);
			}
		}
		groupsList = temp;
	}
	
	/**
	 *   Go through each cell in cells and check if each wall exists and can be taken down.
		 If it does, add it to a new AraryList<Wallboard>.
		 Return the ArrayList<Wallboard>.
	 * @param cells
	 * @return
	 */
	private ArrayList<Wallboard> getWalls(ArrayList<Integer> cells){
		ArrayList<Wallboard> walls = new ArrayList<Wallboard>(); 
		for(int cell = 0; cell < cells.size(); cell++) {
			//Turns the cell number into an x,y pair.
			int x = cells.get(cell)%width;
			int y = cells.get(cell)/width;
			for(CardinalDirection cd: CardinalDirection.values()) {
				Wallboard add = new Wallboard(x, y, cd);
				//Checks to see if the wall exists, is able to be torn down, and the neighboring cell
				//is not already in the group.
				//Adds wall to walls if it passes the if statement.
				if(floorplan.hasWall(x, y, cd) && floorplan.canTearDown(add)
						&& !cells.contains(add.getNeighborY()*width + add.getNeighborX())) {
					walls.add(add);
				}
			}
		}
		return walls;
	}
	
	/**
	 *   First instantiate edgeWeights with dimensions [width][height][4]. 
		 4 is for the 4 directions.
		 Go through each cell in the maze and another for loop for the direction.
		 Set that index in edgeWeights to a random int using random.nextInt().
		 Now when we getEdgeWeight we can just return the value in the corresponding
		 index in edgeWeights.
	 */
	public void setEdgeWeights() {
		edgeWeights = new int[width][height][4];
		Random rand = new Random();
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				for(int d = 0; d < 4; d++) {
					//Weights will only be between 0 and 100.
					edgeWeights[w][h][d] = rand.nextInt(100);
				}
			}
		}
	}
	
	/**
	 * First convert the direction to a number (North = 0, East = 1, South = 2, West = 3).
	 * Return the value in edgeWeights[x][y][direction].
	 * @param x
	 * @param y
	 * @param cd
	 * @return
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		int d = 0;
		if(cd == CardinalDirection.North) {
			d = 0;
		}
		else if(cd == CardinalDirection.East){
			d = 1;
		}
		else if(cd == CardinalDirection.South) {
			d = 2;
		}
		else {
			d = 3;
		}
		return edgeWeights[x][y][d];
	}
}
