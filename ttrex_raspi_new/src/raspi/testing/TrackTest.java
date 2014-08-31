package raspi.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import raspi.communication.WebCommunicator;
import raspi.identities.RunStick;
import raspi.identities.Track;
import shared.utilities.TtrexPosition;
/** 
 *  @author Thomas 
 * 
 * **/
public class TrackTest {
	Track track = new Track();
	/** Update track & store in database & check **/
	@Test
	public void updateTrack() {	
		//first we update track; store it in database
		track.clearTrack();
		List<TtrexPosition> trackList = new ArrayList<TtrexPosition>();
		trackList.add(new TtrexPosition(4.707373380661011,  50.874850779625284));
		trackList.add(new TtrexPosition(4.7080278396606445, 50.87434979165544));
		trackList.add(new TtrexPosition(4.708800315856934,  50.874072214921206));
		trackList.add(new TtrexPosition(4.709497690200806,  50.874390412502265));
		trackList.add(new TtrexPosition(4.710162878036499,  50.87497264074957));
		trackList.add(new TtrexPosition(4.709497690200806,  50.87527729216628));
		trackList.add(new TtrexPosition(4.708456993103027,  50.87569703085609));
		trackList.add(new TtrexPosition(4.707791805267334,  50.87529083218301));
		
		track.updateTrack(trackList);
		List<TtrexPosition> result = new ArrayList<TtrexPosition>();
		
		//now load it from the database again
		result = track.loadTrackDb(1);
		
		assertEquals(result.get(0).getLat(),50.874850779625284,0.001);
		assertEquals(result.get(1).getLat(),50.87434979165544,0.001);
		assertEquals(result.get(2).getLat(),50.874072214921206,0.001);
		assertEquals(result.get(3).getLat(),50.874390412502265,0.001);
		assertEquals(result.get(4).getLat(),50.87497264074957,0.001);
		assertEquals(result.get(5).getLat(),50.87527729216628,0.001);
		assertEquals(result.get(6).getLat(),50.87569703085609,0.001);
		assertEquals(result.get(7).getLat(),50.87529083218301,0.001);
		
	}
	
	/** Simulates incoming position data from GPS for 1 runner**/
	@Test
	public void HandlePositionEvent() {	
		//define a stick, with starting position
		//feed it a few frames
		//check overall output
		RunStick runner = new RunStick(1, new TtrexPosition(4.7073894739151,50.87480000406278)); // start at pos 0, id=1
		track.addRunnerStick(runner);
		track.HandlePositionEvent(new TtrexPosition(4.7074538469314575,50.87459351620533),1);
		track.HandlePositionEvent(new TtrexPosition(4.708043932914734,50.87456982081892),1);
		track.HandlePositionEvent(new TtrexPosition(4.708709120750427,50.87399435773546),1);
		track.HandlePositionEvent(new TtrexPosition(4.709519147872925,50.87458336104122),1);
		track.HandlePositionEvent(new TtrexPosition(4.70988392829895,50.874759383572936),1);
		track.HandlePositionEvent(new TtrexPosition(4.710119962692261,50.87484739458948),1);
		track.HandlePositionEvent(new TtrexPosition(4.70988929271698,50.87514189178251),1);
		track.HandlePositionEvent(new TtrexPosition(4.7094011306762695,50.87539915217536),1);
		track.HandlePositionEvent(new TtrexPosition(4.709068536758423,50.875433002121326),1);
		track.HandlePositionEvent(new TtrexPosition(4.708800315856934,50.87549731695092),1);
		track.HandlePositionEvent(new TtrexPosition(4.708440899848938,50.87562594634389),1);
		track.HandlePositionEvent(new TtrexPosition(4.708237051963806,50.87543638711458),1);
		track.HandlePositionEvent(new TtrexPosition(4.708011746406555,50.87518928196157),1);
		track.HandlePositionEvent(new TtrexPosition(4.7078776359558105,50.87505388132208),1);
		track.HandlePositionEvent(new TtrexPosition(4.707373380661011,50.87477630878132),1);
		track.HandlePositionEvent(new TtrexPosition(4.7078025341033936,50.87440733784466),1);
		track.HandlePositionEvent(new TtrexPosition(4.708258509635925,50.87415345706334),1);
		track.HandlePositionEvent(new TtrexPosition(4.708591103553772,50.874177152661424),1);
		track.HandlePositionEvent(new TtrexPosition(4.708912968635559,50.87400451302794),1);
		track.HandlePositionEvent(new TtrexPosition(4.709304571151733,50.874231313983294),1);
		assertEquals(track.getRunnerStick(1).getNumRounds(),1,0); //rounds should be at 1
		System.out.println(track.getRunnerStick(1).getLastPosition().getLat()+" "+track.getRunnerStick(1).getLastPosition().getLong());
		assertEquals(track.getRunnerStick(1).getLastPosition(),new TtrexPosition(4.709497690200806,50.874390412502265));
	}
	
	/** Test all possible scenarios for matching a GPS location to a track point**/
	@Test
	public void getClosestTrackPoint() {	
		TtrexPosition result;
		
		//test when track is empty
		track.clearTrack();
		result = track.getClosestTrackPoint(new TtrexPosition(4.707834720611572,50.874498734587384),new TtrexPosition(4.707373380661011,50.874850779625284));
		assertNull(result);

		//now add track (distance between track points is arround 50m)
		List<TtrexPosition> trackList = new ArrayList<TtrexPosition>();
		trackList.add(new TtrexPosition(4.7073894739151,  		50.87480000406278)); 	//0
		trackList.add(new TtrexPosition(4.707931280136108, 		50.874488579402616)); 	//1
		trackList.add(new TtrexPosition(4.708408713340759, 		50.87417376757672)); 	//2
		trackList.add(new TtrexPosition(4.708848595619202,  	50.87397066204441)); 	//3		
		trackList.add(new TtrexPosition(4.7093528509140015,  	50.87425839462061)); 	//4
		trackList.add(new TtrexPosition(4.709733724594116,  	50.87461721157971));	//5 
		trackList.add(new TtrexPosition(4.7102004289627075,  	50.87496925572265));	//6 	
		trackList.add(new TtrexPosition(4.709621071815491,  	50.875250212120996));	//7
		trackList.add(new TtrexPosition(4.709020256996155,  	50.87547362202403));	//8
		trackList.add(new TtrexPosition(4.70838725566864,  		50.87563610128083));	//9
		trackList.add(new TtrexPosition(4.70789909362793,  		50.87529760218993));	//10
				 
		track.updateTrack(trackList);
		//test a valid position (second position; having the first position as the current position
		result = track.getClosestTrackPoint(new TtrexPosition(4.707931280136108, 50.874488579402616),new TtrexPosition(4.7073894739151,  50.87480000406278));
		
		//error should be 0! 
		assertEquals(0,result.getError(),0);

		//test a valid position (between second and first having the first position as the current position
		result = track.getClosestTrackPoint(new TtrexPosition(4.707786440849304,  50.87457997598601),new TtrexPosition(4.7073894739151,  50.87480000406278));
		//the point is in between the two points so the maximal error should be 50!
		assertEquals(0,result.getError(),50);
		
		//test an invalid position (a position behind the last position(first one) of the runner)
		result = track.getClosestTrackPoint(new TtrexPosition(4.709283113479614,  50.87552101186577),new TtrexPosition(4.7073894739151,  50.87480000406278));

		//should return lastposition
		assertEquals(result,new TtrexPosition(4.7073894739151,  50.87480000406278));

	}
}
