package be.ttrax.raspi.identities;


/**
 * Lap are the laps already finished
 * TODO: Implement this class in project
 * 
 * @author Christopher
 *
 */
public class Lap {

	private int runnerID;
	private long lapTime; //lapTime in msec
	private double averageVelocity; //
	
	public Lap(int runnerID, long lapTime, double velocity) {
		// TODO Auto-generated constructor stub
		
		this.runnerID = runnerID;
		this.lapTime = lapTime;
		this.averageVelocity = velocity;
	
	}

	/**
	 * @return the runnerID
	 */
	public int getRunnerID() {
		return runnerID;
	}

	/**
	 * @param runnerID the runnerID to set
	 */
	public void setRunnerID(int runnerID) {
		this.runnerID = runnerID;
	}

	/**
	 * @return the lapTime
	 */
	public long getLapTime() {
		return lapTime;
	}

	/**
	 * @param lapTime the lapTime to set
	 */
	public void setLapTime(long lapTime) {
		this.lapTime = lapTime;
	}

	/**
	 * @return the averageVelocity
	 */
	public double getAverageVelocity() {
		return averageVelocity;
	}

	/**
	 * @param averageVelocity the averageVelocity to set
	 */
	public void setAverageVelocity(double averageVelocity) {
		this.averageVelocity = averageVelocity;
	}
	
	
	

}
