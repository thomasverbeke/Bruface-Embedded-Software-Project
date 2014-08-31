package raspi.handlers;

import shared.frames.TrackUpdate;
import raspi.identities.Track;
/**
 * Framehandler is responsible of handling frames 
 * that are coming from the webserver. And forwards them to a track
 * 
 * 	
 * @author Christopher
 *
 */
public class FrameHandler {

	private Track track;
	
	
	/**
	 * Constructor for the FrameHandler
	 */
	public FrameHandler() {

		
	}
	
	
	/**
	 * Parse frame (is an object) from the stream 
	 * 
	 * @param obj
	 */
	public void parseFrame(Object obj){
		
		if(obj instanceof TrackUpdate ){
			
			TrackUpdate update = (TrackUpdate) obj;
		
			track.updateTrack(update.getList());
			
		} else {
			
			//Object is not recognized
			//Just drop packet
		}
		
		
	}
	
	/**
	 * Setter for the track where the frames should go to
	 * 
	 * @param trc
	 */
	public void setTrack(Track trc){
		
		this.track = trc;
	}
	
	
	

}
