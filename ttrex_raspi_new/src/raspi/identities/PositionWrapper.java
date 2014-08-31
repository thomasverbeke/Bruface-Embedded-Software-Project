package raspi.identities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


import shared.utilities.TtrexPosition;

/**
 * This class defines a position point on the track 
 * Because the TtrexPosition is defined in a shared library, 
 * it is not easy to persist such objects
 * This class is for easy persistance of the track
 * @author Christopher
 *
 */

//Use Java JPA Keywords
@Entity
@NamedQueries({
	@NamedQuery(name="PositionWrapper.getTrackCollection", 
		query = "SELECT c FROM PositionWrapper c WHERE c.track_id = :track_id ORDER BY track_seq_num"),
		@NamedQuery(name="PositionWrapper.deleteTrack", 
		query = "DELETE FROM PositionWrapper c WHERE track_id = :track_id")
})
public class PositionWrapper {
	
	@Id @GeneratedValue long unique_id;
	int track_id;
	int track_seq_num;
	double longitude;
	double latitude;
	
	public PositionWrapper(int track_id, int seq, double longitude, double latitude) {
		
		this.track_id = track_id;
		this.track_seq_num = seq;
		this.longitude = longitude;
		this.latitude = latitude;
	
	}
	
	
	public TtrexPosition getTtrexPosition(){
		
		return new TtrexPosition(longitude, latitude);
	}

	
	

}
