package shared.utilities;

import java.io.Serializable;
import java.util.Date;
import static java.lang.Math.*;



/**
 * Class to represent a point on the track, or a location of a runner
 * It's not just named Position because it could give troubles with other
 * classes that are already named "Position"
 * @author Christopher, Thomas (added override for equals)
 *
 */
public class TtrexPosition implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Date timestamp;
	double longitude;
	double latitude;
	double error;
	double percentage;
	double speed;

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public TtrexPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.speed = 0;
		this.error = 0;
	}
	
	public TtrexPosition(double longitude, double latitude, Date time){
		this.longitude = longitude;
		this.latitude = latitude;
		this.timestamp = time;
		this.speed = 0;
		this.error = 0;
	}
	
	/**
	 * Compute the distance between the object position parameters and
	 * the TtrexPosition pos parameter
	 * 
	 * 
	 * @param pos
	 * @return
	 */
	public double getDistance(TtrexPosition pos){
		
		final double EARTH_DIAMETER = 6371.01 * 1000; //meters
		
		//Formula copied at following link
		//https://github.com/thomasverbeke/geocalc/blob/master/src/main/java/com/grum/geocalc/EarthCalc.java
		double diffLongitudes = toRadians(this.longitude - pos.getLong());
		double slat = toRadians(pos.getLat());
		double flat = toRadians(this.latitude);
		
		//Vincenty formula
        double c = sqrt(pow(cos(flat) * sin(diffLongitudes), 2d) + pow(cos(slat) * sin(flat) - sin(slat) * cos(flat) * cos(diffLongitudes), 2d));
        c = c / (sin(slat) * sin(flat) + cos(slat) * cos(flat) * cos(diffLongitudes));
        c = atan(c);
		
		c = c*EARTH_DIAMETER;
		
		return c;
	}
	
	
	public double getLong(){
		return longitude;
	}
	
	public double getLat(){
		return latitude;
	}
	
	public void setLong(double longitude){
		this.longitude = longitude;
	}
	
	public void setLat(double latitude){
		this.latitude = latitude;	
	}
	
	public Date getTimeStamp(){
		return timestamp;
	}
	
	
	public void setError(double error){
		
		this.error = error;
	}

	public double getError(){
		
		return error;
	}
	
	public double getPercentage(){
	
		
		return percentage;
	}
	
	
	public void setPercentage(double percentage){
		
		this.percentage = percentage;
	}
	
	@Override
	public boolean equals(Object obj) {
	       if (!(obj instanceof TtrexPosition)){
	    	   return false;
	       }
	        
	       if (obj == this){
	            return true;
	       }
	       
	       TtrexPosition position = (TtrexPosition) obj;
	       
	       if (position.latitude == this.latitude && position.longitude == this.longitude){
	    	   return true;
	       } 
	       
		return false;
	       
	}
	     
}
