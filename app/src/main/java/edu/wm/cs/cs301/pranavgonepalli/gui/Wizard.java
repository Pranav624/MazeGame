/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

import edu.wm.cs.cs301.pranavgonepalli.generation.CardinalDirection;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;
import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Turn;
import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Direction;

/**
 * Class: Wizard
 * 
 * Responsibilities:
 * Driver algorithm user may select to run an automated game.
 * Get out of the maze as quickly as possible.
 * Use sensors to avoid running into walls.
 * Get energy used by the robot to get out of the maze and how far the robot traveled.
 * 
 * Collaborators:
 * RobotDriver
 * Robot
 * Maze
 * 
 * @author Pranav Gonepalli
 *
 */
public class Wizard implements RobotDriver {
	
	protected Robot robot;
	protected Maze m;

	/**
	 * Nothing in here.
	 */
	public Wizard() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets robot to r.
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		robot = r;
	}
	
	/**
	 * Gets the robot.
	 * For testing.
	 * @return
	 */
	public Robot getRobot() {
		return robot;
	}

	/**
	 * Sets m to maze.
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		m = maze;
	}
	
	/**
	 * Gets the maze.
	 * For testing.
	 * @return
	 */
	public Maze getMaze() {
		return m;
	}

	/**
	 * First we make sure the exit is reachable by using getPathLength() and if it's not,
	 * we return false.
	 * Then, we use a while that stops when robot is at the exit and is facing into eternity.
	 * Inside the while loop, we call drive1step2exit().
	 * We also check if the robot stopped using robot.hasStopped() and if it did, we throw
	 * an exception.
	 * Then we check if the robot has enough energy to move 1 more space and it does, we move 
	 * 1 space and return true and if it doesn't, we throw an exception.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		//If the exit is not reachable from the starting position, we return false
		if(getPathLength() == Integer.MAX_VALUE) {
			return false;
		}
		//We keep calling drive1Step2Exit until the robot is in the exit position and facing the exit
		while(!(robot.isAtExit() && robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD))) {
			//If the robot stopped, that means it crashed or ran out of energy because when we called robot.move(1) 
			//at the end of drive1Step2Exit(), the robot would stop if it ran into a wall or ran out of energy.
			if(robot.hasStopped()) {
				throw new Exception("Robot has stopped.");
			}
			//If drive1Step2Exit threw an exception, it means the robot ran out of energy
			try {
				drive1Step2Exit();
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("Robot has stopped.");
			}
		}
		//If the robot has enough battery to move, we move, completing the maze
		//If not, we throw an exception
		//Here we have to check if it has enough battery because although robot.move() will 
		//say that the robot stopped, it won't throw an exception meaning the game won't end.
		if(robot.getBatteryLevel() >= robot.getEnergyForStepForward()) {
			robot.move(1);
			return true;
		}
		else {
			throw new Exception("Not enough energy to move.");
		}
	}

	/**
	 * First, check if the robot is in the exit position using robot.isAtExit().
	 * If it is, we see if the robot is facing the exit using robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD).
	 * If it is, we return false. If it is not, we check to make sure it has enough
	 * energy and then turn it towards the exit and return false.
	 * Then we get the current position of the robot using robot.getCurrentPosition() and use that
	 * to get the position of the neighbor using maze.getNeighborCloserToExit().
	 * Then, we find dx and dy by subtracting current position from neighbor position, 
	 * and use CardinalDirection.getDirection() to get the direction facing the neighbor.
	 * Then we make the robot face the neighbor based on the current direction it is facing.
	 * We use a while loop to keep going while the robot is not facing the direction
	 * of the neighbor.
	 * Then inside the while loop, we make the robot turn so it's closer to the direction of the neighbor,
	 * making sure it has enough energy to do so. If it doesn't throw an exception.
	 * Finally, check if the robot has enough energy to move 1 space and make it move if it does.
	 * If it doesn't, throw an exception.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
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
			int dx = neighborPosition[0] - position[0];
			int dy = neighborPosition[1] - position[1];
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
			//We move the robot since it is now facing the direction of its neighbor
			//We don't need to check if it has enough energy because that is checked in the robot's move method
			robot.move(1);
			return true;
		}
	}

	/**
	 * Returns 3500 - the robot's battery which you get by calling 
	 * robot.getBatteryLevel(), because 3500 is the battery the 
	 * robot has at the beginning so if you subtract it by the 
	 * robot's current battery, you get energy consumed.
	 */
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 3500 - robot.getBatteryLevel();
	}

	/**
	 * Return robot's odometer reading.
	 */
	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return robot.getOdometerReading();
	}

}
