/**
 * 
 */
package edu.wm.cs.cs301.pranavgonepalli.gui;

/**
 * Class: UnreliableSensor
 * 
 * Responsibilities:
 * Extends ReliableSensor.
 * Sometimes this sensor might fail, in which case it repairs itself.
 * 
 * Collaborators:
 * ReliableSensor
 * Thread
 * 
 * @author Pranav Gonepalli
 *
 */
public class UnreliableSensor extends ReliableSensor implements Runnable {

	//Instance variables
	private boolean isOperational;
	private int timeToRepair;
	private int timeBetweenFailures;
	private Thread thread;
	
	/**
	 * We don't need anything in here except setting isOperational to true.
	 */
	public UnreliableSensor() {
		// TODO Auto-generated constructor stub
		super();
		isOperational = true;
	}

	/**
	 * First, we make a while loop to keep running while thread is not null.
	 * First we set isOperational to false.
	 * Then, we use Thread.sleep() to sleep for the repair time to show
	 * that the sensor is being repaired.
	 * After that, we set isOperational to true meaning the sensor was repaired.
	 * Then, we use Thread.sleep() to sleep again for the amount of time
	 * between failures.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(thread != null) {
			try {
				//Set sensor to be not operational
				isOperational = false;
				//Wait
				Thread.sleep(timeToRepair);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
			try {
				//Set sensor to be operational
				isOperational = true;
				//Wait
				Thread.sleep(timeBetweenFailures);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * Returns thread.
	 * For testing.
	 * @return thread
	 */
	public Thread getThread() {
		return thread;
	}


	/**
	 * Returns if the sensor is operational.
	 * @return
	 */
	@Override
	public boolean getIsOperational() {
		return isOperational;
	}
	
	/**
	 * Sets the sensor to be operational or not.
	 * @param operational
	 */
	public void setOperational(boolean operational) {
		isOperational = operational;
	}
	
	/**
	 * Start the thread that fails and repairs.
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair) {
		thread = new Thread(this);
		timeToRepair = meanTimeToRepair;
		timeBetweenFailures = meanTimeBetweenFailures;
		thread.start();
	}
	
	/**
	 * Terminate the thread that fails and repairs.
	 */
	@Override
	public void stopFailureAndRepairProcess() {
		thread.interrupt();
		thread = null;
	}

}
