/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

import edu.wm.cs.cs301.pranavgonepalli.generation.CardinalDirection;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;
import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Direction;

/**
 * Class: ReliableSensor
 * 
 * Responsibilities: 
 * Measure distances to obstacles in all four cardinal directions.
 * Calculate the amount of energy each sensing action takes up.
 * Since this sensor is always reliable, it will never fail.
 * 
 * Collaborators:
 * DistanceSensor
 * Maze
 * Floorplan
 * 
 * @author Pranav Gonepalli
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	private Maze m;
	private Direction dir;

	/**
	 * Nothing needed in here because we will use the set
	 * methods to set the direction of the sensor, as well
	 * as the maze to use when using the sensor.
	 */
	public ReliableSensor() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Make sure the parameters are not null. If they are, throw IllegalArgumentException.
	 * Make sure current position is in the maze. If it is not, throw IllegalArgumentException.
	 * If the power supply is insufficient (less than 1), throw an Exception.
	 * Make an int dist and set it equal to 0, an int x which is the x value of currentPosition,
	 * and an int y which is the y value of currentPosition.
	 * Then, make a while loop that keeps running while m.getFlooplan() has no wall, and increase 
	 * or decrease x or y based on the direction (North: y--, East: x++, South y++, West: x--).
	 * Check to make sure this position is inside the maze using maze.isValidPosition(). If it is
	 * not, return Integer.MAX_VALUE because it means the robot exited the maze.
	 * Inside the while loop, add one to dist each time it goes through the loop.
	 * At the end of the method, return dist, which is the distance from the robot to the 
	 * nearest obstacle in the direction currentDirection.
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
		//Throw exception is parameters are null
		if(currentPosition == null || currentDirection == null || powersupply == null) {
			throw new IllegalArgumentException("Parameters can not be null.");
		}
		//Throw exception if currentPosition isn't in the maze
		if(!m.isValidPosition(currentPosition[0], currentPosition[1])) {
			throw new IllegalArgumentException("Position is not in the maze.");
		}
		//Throw exception if robot doesn't have enough battery to sense
		if(powersupply[0] < 1) {
			throw new Exception("PowerFailure");
		}
		int dist = 0;
		int x = currentPosition[0];
		int y = currentPosition[1];
		//Keep going until there is a wall in the current direction from robot's position
		while(m.getFloorplan().hasNoWall(x, y, currentDirection)) {
			//Depending on direction, we add/subtract from x/y until we reach a wall
			if(currentDirection == CardinalDirection.North) {
				y--;
				//If we're not in the maze anymore, that means we're at the exit so return Integer.MAX_VALUE
				if(!m.isValidPosition(x, y)) {
					return Integer.MAX_VALUE;
				}
			}
			else if(currentDirection == CardinalDirection.East) {
				x++;
				if(!m.isValidPosition(x, y)) {
					return Integer.MAX_VALUE;
				}
			}
			else if(currentDirection == CardinalDirection.South) {
				y++;
				if(!m.isValidPosition(x, y)) {
					return Integer.MAX_VALUE;
				}
			}
			else if(currentDirection == CardinalDirection.West) {
				x--;
				if(!m.isValidPosition(x, y)) {
					return Integer.MAX_VALUE;
				}
			}
			//Increment dist
			dist++;
		}
		//Subtract 1 from the battery
		powersupply[0]-=1;
		return dist;
	}
	
	/**
	 * Sets m equal to maze.
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		m = maze;
	}
	
	/**
	 * Returns the maze
	 * @return
	 */
	public Maze getMaze() {
		return m;
	}

	/**
	 * Sets dir equal to mountedDirection.
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub
		dir = mountedDirection;
	}
	
	/**
	 * Returns the sensor direction.
	 * @return
	 */
	public Direction getSensorDirection() {
		return dir;
	}

	/**
	 * Returns 1 because that's the energy required to sense.
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean getIsOperational(){
		return true;
	}
	/**
	 * Not used in ReliableSensor.
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	/**
	 * Not used in ReliableSensor.
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
