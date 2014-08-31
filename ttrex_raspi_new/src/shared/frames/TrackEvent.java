package shared.frames;


public class TrackEvent extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Class is a TrackEvent to update the webserver
	 * with a new accuracy
	 */
	private static final int frame_id = 1;
	private int runner_id;
	private double percentage;
	
	private double longitude;
	private double latitude;
	private int rounds = 0;
	private double speed =0;
	
	public TrackEvent(int runner_id, double percentage, double lng, double lat) {
		this.runner_id = runner_id;
		this.percentage = percentage;
		this.longitude = lng;
		this.latitude = lat;
		this.speed = 0;
	}
	
	public TrackEvent(int runner_id, double percentage, double lng, double lat, double speed) {
		this.runner_id = runner_id;
		this.percentage = percentage;
		this.longitude = lng;
		this.latitude = lat;
		this.speed = speed;
	}
	
	public TrackEvent(int runner_id, double percentage){
		
		this.runner_id = runner_id;
		this.percentage = percentage;
		this.longitude = 0;
		this.latitude = 0;
	}

	public int getID() {
		return frame_id ;
	}
	
	public int getRunnerId(){
		return runner_id;
	}
	
	public double getPercentage(){
	
		return percentage;
	}
	
	public double getLatitude(){
		
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	
	}
	
	public int getRounds(){
		return rounds;
	}
	
	public void setRounds(int rounds){
		this.rounds = rounds;
	}
	
	public double getSpeed(){
		return speed;
	}
}
