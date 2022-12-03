/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

import edu.wm.cs.cs301.pranavgonepalli.gui.Robot.Direction;

/**
 * Class: UnreliableRobot
 * 
 * Responsibilities:
 * Perform actions such as turning, moving, and sensing walls using UnreliableSensor.
 * Calculate battery level which gets depleted through actions.
 * Stop when it hits an obstacle.
 * Run the UnreliableSensor threads to make them fail, and also to repair them.
 * 
 * Collaborators:
 * ReliableRobot
 * ReliableSensor
 * UnreliableSensor
 * Control
 * @author Pranav Gonepalli
 *
 */
public class UnreliableRobot extends ReliableRobot {

	/**
	 * We don't need anything in here because everything is set in the 
	 * constructor of ReliableRobot (superclass).
	 */
	public UnreliableRobot() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	/**
	 * First, we get the sensor in the specified direction using getDistanceSensor()
	 * and check whether it is a reliable sensor or unreliable sensor. 
	 * If it is a reliable sensor, we return super.distanceToObstacle(direction).
	 * If it is unreliable, we check whether is is operational or not.
	 * If it is operational, we return super.distanceToObstacle(direction).
	 * If it is not operational, we find a sensor that is operational using 
	 * getWorkingSensor().
	 * Then, we rotate the robot so the working sensor is facing towards the 
	 * direction we want to sense. 
	 * We sense using super.distanceToObstacle(), store the value, and then rotate the
	 * robot back.
	 * Return the value.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		DistanceSensor sensor = getDistanceSensor(direction);
		ReliableSensor constant = new ReliableSensor();
		//If the sensor is a reliable sensor, return reliableSensor's distance to obstacle
		if(sensor.getClass() == constant.getClass()) {
			return super.distanceToObstacle(direction);
		}
		//If the sensor is operational, return reliableSensor's distance to obstacle
		if(((UnreliableSensor)sensor).getIsOperational()) {
			return super.distanceToObstacle(direction);
		}
		//Use helper method to get the direction of a working sensor
		Direction dir = getWorkingSensor();
		int distance = 0;
		//Rotate the robot so the working sensor is facing the direction we want to sense
		//Store the distance, and then rotate back
		if(direction == Direction.FORWARD) {
			if(dir == Direction.LEFT) {
				rotate(Turn.RIGHT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.LEFT);
			}
			else if(dir == Direction.RIGHT) {
				rotate(Turn.LEFT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.RIGHT);
			}
			else if(dir == Direction.BACKWARD) {
				rotate(Turn.AROUND);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.AROUND);
			}
			else {
				distance = super.distanceToObstacle(direction);
			}
		}
		else if(direction == Direction.LEFT) {
			if(dir == Direction.FORWARD) {
				rotate(Turn.LEFT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.RIGHT);
			}
			else if(dir == Direction.BACKWARD) {
				rotate(Turn.RIGHT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.LEFT);
			}
			else if(dir == Direction.RIGHT) {
				rotate(Turn.AROUND);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.AROUND);
			}
			else {
				distance = super.distanceToObstacle(direction);
			}
		}
		else if(direction == Direction.RIGHT) {
			if(dir == Direction.FORWARD) {
				rotate(Turn.RIGHT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.LEFT);
			}
			else if(dir == Direction.BACKWARD) {
				rotate(Turn.LEFT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.RIGHT);
			}
			else if(dir == Direction.LEFT) {
				rotate(Turn.AROUND);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.AROUND);
			}
			else {
				distance = super.distanceToObstacle(direction);
			}
		}
		else {
			if(dir == Direction.LEFT) {
				rotate(Turn.LEFT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.RIGHT);
			}
			else if(dir == Direction.RIGHT) {
				rotate(Turn.RIGHT);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.LEFT);
			}
			else if(dir == Direction.FORWARD) {
				rotate(Turn.AROUND);
				distance = super.distanceToObstacle(dir);
				rotate(Turn.AROUND);
			}
			else {
				distance = super.distanceToObstacle(direction);
			}
		}
		//Return distance
		return distance;
	}
	
	/**
	 * We go through the directions and check if either the sensor in that direction
	 * is reliable or unreliable but is operational.
	 * If it is, we return that direction.
	 * If all the sensors are not working, we use Thread.sleep() to wait a bit, and then
	 * try getWorkingSensor() again, until a we get a sensor that works.
	 * @return direction of a working sensor
	 */
	public Direction getWorkingSensor() {
		ReliableSensor constant = new ReliableSensor();
		//Go through all the directions
		for(Direction dir : Direction.values()) {
			//Get the sensor in that direction
			DistanceSensor sensor = getDistanceSensor(dir);
			//If the sensor is a reliable sensor return direction
			if(sensor.getClass() == constant.getClass()) {
				return dir;
			}
			else {
				//Or, if the sensor is unreliable but operational, return direction
				if(((UnreliableSensor)sensor).getIsOperational()) {
					return dir;
				}
			}
		}
		//Plan B: If no sensors are working, wait a bit
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
		//Try again after waiting
		return getWorkingSensor();
	}
	
	/**
	 * Start the failure and repair process for the sensor in the specified direction.
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair) {
		if(direction == Direction.FORWARD) {
			getDistanceSensor(Direction.FORWARD).startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		else if(direction == Direction.LEFT) {
			getDistanceSensor(Direction.LEFT).startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		else if(direction == Direction.RIGHT) {
			getDistanceSensor(Direction.RIGHT).startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		else {
			getDistanceSensor(Direction.BACKWARD).startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
	}
	
	/**
	 * Stop the failure and repair process for the sensor in the specified direction.
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		if(direction == Direction.FORWARD) {
			getDistanceSensor(Direction.FORWARD).stopFailureAndRepairProcess();
		}
		else if(direction == Direction.LEFT) {
			getDistanceSensor(Direction.LEFT).stopFailureAndRepairProcess();
		}
		else if(direction == Direction.RIGHT) {
			getDistanceSensor(Direction.RIGHT).stopFailureAndRepairProcess();
		}
		else {
			getDistanceSensor(Direction.BACKWARD).stopFailureAndRepairProcess();
		}
	}
}
