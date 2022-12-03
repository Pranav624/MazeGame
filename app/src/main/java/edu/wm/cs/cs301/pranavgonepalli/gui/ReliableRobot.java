/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

import edu.wm.cs.cs301.pranavgonepalli.generation.CardinalDirection;
import edu.wm.cs.cs301.pranavgonepalli.generation.Floorplan;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;
import edu.wm.cs.cs301.pranavgonepalli.gui.Constants.UserInput;

/**
 * Class: ReliableRobot
 * 
 * Responsibilities:
 * Performs actions such as turning, moving, and sensing walls using ReliableSensor.
 * Calculate battery level which gets depleted through actions.
 * Stop when it hits an obstacle.
 * 
 * Collaborators:
 * Robot
 * Control
 * ReliableSensor
 * 
 * @author Pranav Gonepalli
 *
 */
public class ReliableRobot implements Robot {

	//Instance variables
	private StatePlaying control;
	private DistanceSensor forward;
	private DistanceSensor backward;
	private DistanceSensor left;
	private DistanceSensor right;
	private float[] batteryLevel;
	private int odometer;
	private boolean stopped;

	/**
	 * Instantiate all the sensors and set their directions.
	 * Also set batteryLevel to 3500 and odometer to 0.
	 */
	public ReliableRobot() {
		// TODO Auto-generated constructor stub
		batteryLevel = new float[1];
		batteryLevel[0] = 3500;
		odometer = 0;
		stopped = false;
	}
	
	/**
	 * Set control to controller.
	 */
	@Override
	public void setStatePlaying(StatePlaying statePlaying) {
		// TODO Auto-generated method stub
		control = statePlaying;
	}
	
	/**
	 * Get the controller.
	 * @return
	 */
	public StatePlaying getController() {
		return control;
	}

	/**
	 * First set the direction of sensor to be mountedDirection using 
	 * sensor.setSensorDirection().
	 * Then, depending on what mountedDirection is, set the specified
	 * sensor equal to sensor.
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// TODO Auto-generated method stub
		sensor.setSensorDirection(mountedDirection);
		//sensor.setMaze(control.getMaze());
		if(mountedDirection == Direction.FORWARD) {
			forward = sensor;
		}
		else if(mountedDirection == Direction.BACKWARD) {
			backward = sensor;
		}
		else if(mountedDirection == Direction.LEFT) {
			left = sensor;
		}
		else if(mountedDirection == Direction.RIGHT) {
			right = sensor;
		}
	}
	
	/**
	 * Returns the sensor corresponding to mountedDirection.
	 */
	@Override
	public DistanceSensor getDistanceSensor(Direction mountedDirection) {
		if(mountedDirection == Direction.FORWARD) {
			return forward;
		}
		else if(mountedDirection == Direction.BACKWARD) {
			return backward;
		}
		else if(mountedDirection == Direction.LEFT) {
			return left;
		}
		else {
			return right;
		}
	}

	/**
	 * Return control.getCurrentPosition() if it is in the maze.
	 * If it is not, throw an Exception.
	 * Use control.getMaze().isValidPosition() to see
	 * if the position is in the maze.
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		int[] position = control.getCurrentPosition();
		if(control.getMaze().isValidPosition(position[0], position[1])) {
			return position;
		}
		else {
			throw new Exception("Position is not in maze.");
		}
	}

	/**
	 * Return control.getCurrentDirection().
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		return control.getCurrentDirection();
	}

	/**
	 * Return batteryLevel.
	 */
	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return batteryLevel[0];
	}

	/**
	 * Set batteryLevel to level.
	 */
	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub
		batteryLevel[0] = level;
	}

	/**
	 * Return 12 because the energy required for a quarter turn is 3 
	 * so the energy required for a full rotation is 3 * 4 = 12.
	 */
	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		return 12;
	}

	/**
	 * Return 6 because that's the energy required to step forward.
	 */
	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		return 6;
	}

	/**
	 * Returns odometer.
	 */
	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	/**
	 * Sets odometer to 0.
	 */
	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		odometer = 0;
	}

	/**
	 * First check if it is a left or right turn. 
	 * If it is, make the sure the robot has atleast 3 energy, and perform the
	 * turn using control.handleKeyboardInput(UserInput.Direction).
	 * Then subtract the 3 from batteryLevel.
	 * If it doesn't have enough battery, set stopped to True.
	 * If turn == around, make sure the robot has atleast 6 energy, and perform 
	 * the turn using control.handleKeyboardInput(UserInput.Direction) twice.
	 * Then subtract 6 from the battery level.
	 * If it doesn't have enough battery, set stopped to True.
	 */
	@Override
	public void rotate(Turn turn) {
		// TODO Auto-generated method stub
		if(turn == Turn.LEFT || turn == Turn.RIGHT) {
			//Since we're only turning left or right, battery level has to be at least 3
			if(getBatteryLevel() >= 3) {
				if(turn == Turn.LEFT) {
					control.handleUserInput(UserInput.LEFT, 0);
				}
				else {
					control.handleUserInput(UserInput.RIGHT, 0);
				}
				//Subtract 3 from battery level
				batteryLevel[0]-=3;
			}
			//If not enough battery, robot stopped
			else {
				stopped = true;
			}
		}
		else if(turn == Turn.AROUND) {
			if(getBatteryLevel() >= 6) {
				//Since we're turning around, battery level has to be at least 6
				control.handleUserInput(UserInput.LEFT, 0);
				control.handleUserInput(UserInput.LEFT, 0);
				//Subtract 6 from battery level
				batteryLevel[0]-=6;
			}
			//If not enough battery, robot stopped
			else {
				stopped = true;
			}
		}

	}

	/**
	 * Check if distance is less than 0. If it is, throw IllegalArgumentException.
	 * Then, we make a for-loop that goes from 0 to distance.
	 * Inside the for-loop, we check that the robot hasn't stopped using hasStopped() 
	 * and if we have enough battery to move 1 step and and if we pass these two tests, 
	 * we move the robot using control.handleKeyboardInput(UserInput.UP), add one to the odometer, 
	 * and subtract the energy to move 1 step from batteryLevel.
	 */
	@Override
	public void move(int distance) {
		// TODO Auto-generated method stub
		if(distance < 0) {
			throw new IllegalArgumentException("Distance can't be negative.");
		}
		for(int step = 0; step < distance; step++) {
			try {
				//If there is a wall in front of the robot and it still has more steps to go, stop the robot and end the for-loop
				if(control.getMaze().hasWall(control.getCurrentPosition()[0], control.getCurrentPosition()[1], getCurrentDirection())) {
					stopped = true;
					//throw new Exception("Robot crashed.");
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				stopped = true;
			}
			//If robot hasn't stopped and has enough battery, move it
			if(!hasStopped() && getBatteryLevel() >= getEnergyForStepForward()) {
				odometer++;
				control.handleUserInput(UserInput.UP, 0);
				//Subtract energy for step forward from battery level
				batteryLevel[0]-=getEnergyForStepForward();
			}
			//If robot doesn't have enough battery, it stopped
			else {
				stopped = true;
				break;
			}
		}
	}

	/**
	 * First check if the battery level is over 40, which is how much you need to jump.
	 * If not, don't do anything and set stopped to true.
	 * Then, make sure there is a wall in front of the robot.
	 * Then, we make sure the position to be jumped to is in the maze. 
	 * We do this by getting the current position of the robot and adding or subtracting 1
	 * from the x or y direction based on which way it's facing.
	 * Then we use maze.isValidPosition() to see if it's in the maze.
	 * If it is, we jump. If not, we don't do anything and set stopped to true.
	 */
	@Override
	public void jump() {
		// TODO Auto-generated method stub
		if(getBatteryLevel() >= 40) {
			//First check that there is a wall in front of the robot
			if(control.getMaze().hasWall(control.getCurrentPosition()[0], control.getCurrentPosition()[1], getCurrentDirection())) {
				int[] position = control.getCurrentPosition();
				CardinalDirection direction = getCurrentDirection();
				//Find the position that the robot is going to jump to
				if(direction == CardinalDirection.North) {
					position[1]-=1;
				}
				else if(direction == CardinalDirection.East) {
					position[0]+=1;
				}
				else if(direction == CardinalDirection.South) {
					position[1]+=1;
				}
				else {
					position[0]-=1;
				}
				//Make sure the position is in the maze
				//If it is, jump
				if(control.getMaze().isValidPosition(position[0], position[1])) {
					odometer++;
					control.handleUserInput(UserInput.JUMP, 0);
					//Subtract 40 from battery level
					batteryLevel[0]-=40;
				}
				//If position isn't in the maze, robot can't jump so it stops
				else {
					stopped = true;
				}
			}
		}
		else {
			stopped = true;
		}
	}

	/**
	 * First get the floorplan using control.getMaze().getFloorplan().
	 * Then, check if the robot's current position is the exit position by using
	 * floorplan.isExitPosition(getCurrentPosition).
	 * Return false if not.
	 */
	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		try {
			return control.getMaze().getFloorplan().isExitPosition(getCurrentPosition()[0], getCurrentPosition()[1]);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	/**
	 * First get the floorplan using control.getMaze().getFloorplan().
	 * Then, check if the robot's current position is inside a room by using
	 * floorplan.isInRoom(getCurrentPosition).
	 * Return false if not.
	 */
	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		try {
			return control.getMaze().getFloorplan().isInRoom(getCurrentPosition()[0], getCurrentPosition()[1]);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * Return stopped.
	 */
	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		return stopped;
	}
	
	/**
	 * Set stopped to stop.
	 * @param stop
	 */
	public void setStopped(boolean stop) {
		stopped = stop;
	}

	/**
	 * We first see which direction the robot is facing using getCurrentDirection().
	 * If the robot is facing north and the direction parameter is forward, we sense north using
	 * sensor.distanceToObstacle(getCurrentPosition(), north, batterylevel).
	 * If the robot is facing north and the direction parameter is left, we sense west, etc.
	 * We use a try catch block and throw an exception if we catch.
	 * If we don't, we return the distance and subtract 1 from batteryLevel.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		CardinalDirection robotDirection = getCurrentDirection();
		if(robotDirection == CardinalDirection.North) {
			try {
				if(direction == Direction.FORWARD) {
					return forward.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
				}
				else if(direction == Direction.BACKWARD) {
					return backward.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
				}
				else if(direction == Direction.LEFT) {
					return left.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
				}
				else if(direction == Direction.RIGHT) {
					return right.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new UnsupportedOperationException("PowerFailure");
			}
		}
		else if(robotDirection == CardinalDirection.East) {
			try {
				if(direction == Direction.FORWARD) {
					return forward.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
				}
				else if(direction == Direction.BACKWARD) {
					return backward.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
				}
				else if(direction == Direction.LEFT) {
					return left.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
				}
				else if(direction == Direction.RIGHT) {
					return right.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new UnsupportedOperationException("PowerFailure");
			}
		}
		else if(robotDirection == CardinalDirection.South) {
			try {
				if(direction == Direction.FORWARD) {
					return forward.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
				}
				else if(direction == Direction.BACKWARD) {
					return backward.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
				}
				else if(direction == Direction.LEFT) {
					return left.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
				}
				else if(direction == Direction.RIGHT) {
					return right.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new UnsupportedOperationException("PowerFailure");
			}
		}
		else if(robotDirection == CardinalDirection.West) {
			try {
				if(direction == Direction.FORWARD) {
					return forward.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
				}
				else if(direction == Direction.BACKWARD) {
					return backward.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
				}
				else if(direction == Direction.LEFT) {
					return left.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
				}
				else if(direction == Direction.RIGHT) {
					return right.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new UnsupportedOperationException("PowerFailure");
			}
		}
		return 0;
	}

	/**
	 * We check if distanceToObstacle(direction) == Integer.MAX_VALUE and return true if it is and
	 * false if it isn't.
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		if(distanceToObstacle(direction) == Integer.MAX_VALUE) {
			return true;
		}
		return false;
	}

	/**
	 * Not for project 3.
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	/**
	 * Not for project 3.
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
