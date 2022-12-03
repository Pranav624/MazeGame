/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

import java.util.ArrayList;

import edu.wm.cs.cs301.pranavgonepalli.generation.CardinalDirection;
import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Direction;
import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Turn;

/**
 * Class: SmartWizard
 * 
 * Responsibilities:
 * Driver algorithm user may select to run an automated game.
 * Get out of the maze as quickly as possible.
 * Use sensors to avoid running into walls.
 * Get energy used by the robot to get out of the maze and how far the robot traveled.
 * If the robot can jump over a wall to get to exit faster, do it.
 * 
 * Collaborators:
 * Wizard
 * Robot
 * Maze
 * 
 * @author Pranav Gonepalli
 *
 */
public class SmartWizard extends Wizard {

	/**
	 * Nothing in here.
	 */
	public SmartWizard() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	/**
	 * First, check if the robot is in the exit position using robot.isAtExit().
	 * If it is, we see if the robot is facing the exit using robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD).
	 * If it is, we return false. If it is not, we check to make sure it has enough
	 * energy and then turn it towards the exit and return false.
	 * Then we get the current position of the robot using robot.getCurrentPosition() and use that
	 * to get the position of the neighbor using maze.getNeighborCloserToExit().
	 * Then, we get an arraylist of all the neighbors using the helper method
	 * getNeighbors().
	 * We then go through the neighbors arraylist and see which one is closest to the exit.
	 * We only count it if it is farther than 5 steps away from the actual neighbor without jumping because
	 * if it isn't, then it's cheaper in terms of energy to just go around instead of jumping over.
	 * Then, we find dx and dy by subtracting current position from neighbor position, 
	 * and use CardinalDirection.getDirection() to get the direction facing the neighbor.
	 * Then we make the robot face the neighbor based on the current direction it is facing.
	 * We use a while loop to keep going while the robot is not facing the direction
	 * of the neighbor.
	 * Then inside the while loop, we make the robot turn so it's closer to the direction of the neighbor,
	 * making sure it has enough energy to do so. If it doesn't throw an exception.
	 * Now we check if the robot stopped because if it did, that means it didn't have enough energy to 
	 * turn so we throw an exception.
	 * Then, we check if the neighbor is over a wall or not.
	 * If it is, we jump and if not, we move 1 space.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		//Check if the robot is already in the exit position
		if(robot.isAtExit()) {
			//If it's already facing the exit, we don't do anything
			if(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				return false;
			}
			//If it's not, we rotate it so it's facing the exit
			else if(robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
				robot.rotate(Turn.LEFT);
			}
			else if(robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
				robot.rotate(Turn.RIGHT);
			}
			else if(robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD)) {
				robot.rotate(Turn.AROUND);
			}
			//If the robot didn't have enough energy to rotate, we throw an exception
			if(robot.hasStopped()) {
				throw new Exception("Not enough energy to turn.");
			}
			return false;
		}
		else {
			//We get the position of the robot and its neighbor
			int[] position = robot.getCurrentPosition();
			int[] neighborPosition = m.getNeighborCloserToExit(position[0], position[1]);
			ArrayList<int[]> neighbors = getNeighbors(position[0], position[1]);
			int x = neighborPosition[0];
			int y = neighborPosition[1];
			//Go through neighbors
			for(int[] pos : neighbors) {
				//If the distance from this position is smaller, set x and y to this position
				if((m.getDistanceToExit(x, y) - m.getDistanceToExit(pos[0], pos[1]) > 5)
					&& (m.getDistanceToExit(pos[0], pos[1]) < m.getDistanceToExit(x, y))) {
					x = pos[0];
					y = pos[1];
				}
			}
			int dx = x - position[0];
			int dy = y - position[1];
			//We find the direction facing robot's neighbor from its current position
			CardinalDirection neighborDirection = CardinalDirection.getDirection(dx, dy);
			//We keep rotating the robot until it is facing its neighbor
			while(robot.getCurrentDirection() != neighborDirection) {
				if(robot.hasStopped()) {
					throw new Exception("Not enough energy to turn.");
				}
				CardinalDirection robotDirection = robot.getCurrentDirection();
				if(robotDirection == CardinalDirection.North) {
					if(neighborDirection == CardinalDirection.East) {
						robot.rotate(Turn.LEFT);
					}
					else {
						robot.rotate(Turn.RIGHT);
					}
				}
				else if(robotDirection == CardinalDirection.East) {
					if(neighborDirection == CardinalDirection.South) {
						robot.rotate(Turn.LEFT);
					}
					else {
						robot.rotate(Turn.RIGHT);
					}
				}
				else if(robotDirection == CardinalDirection.South) {
					if(neighborDirection == CardinalDirection.West) {
						robot.rotate(Turn.LEFT);
					}
					else {
						robot.rotate(Turn.RIGHT);
					}
				}
				else if(robotDirection == CardinalDirection.West) {
					if(neighborDirection == CardinalDirection.North) {
						robot.rotate(Turn.LEFT);
					}
					else {
						robot.rotate(Turn.RIGHT);
					}
				}
			}
			//If the robot stopped, that means it didn't have enough energy to turn so we throw an exception
			if(robot.hasStopped()) {
				throw new Exception("Not enough energy to turn.");
			}
			//If the neighbor is not across a wall, we just move
			if(x == neighborPosition[0] && y == neighborPosition[1]) {
				robot.move(1);
				return true;
			}
			//If it is, we jump
			else {
				robot.jump();
				return true;
			}
		}
	}
	
	/**
	 * Get all the neighbors of the current position, if they are in the maze.
	 * We check each neighbor position and if it is in the maze, 
	 * we add it to the arraylist of neighbors.
	 * @param x
	 * @param y
	 * @return neighbors arraylist
	 */
	public ArrayList<int[]> getNeighbors(int x, int y) {
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		//Check if each adjacent tile is a valid position
		//If it is, add to arraylist
		if(m.isValidPosition(x+1, y)) {
			int[] neighbor = {x+1, y};
			neighbors.add(neighbor);
		}
		if(m.isValidPosition(x-1, y)) {
			int[] neighbor = {x-1, y};
			neighbors.add(neighbor);
		}
		if(m.isValidPosition(x, y+1)) {
			int[] neighbor = {x, y+1};
			neighbors.add(neighbor);
		}
		if(m.isValidPosition(x, y-1)) {
			int[] neighbor = {x, y-1};
			neighbors.add(neighbor);
		}
		//Return arraylist
		return neighbors;
	}
}
