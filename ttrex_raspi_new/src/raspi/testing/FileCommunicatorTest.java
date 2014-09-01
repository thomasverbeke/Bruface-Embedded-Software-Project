package raspi.testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Test;

import raspi.communication.FileCommunicator;
import raspi.handlers.FrameHandler;
import raspi.handlers.PositionInputHandler;
import raspi.identities.RunStick;
import raspi.identities.Track;
import raspi.nmea.NMEAParser;

public class FileCommunicatorTest {
   

	@Test
	public void test() {
		FileCommunicator fileSim;
		PositionInputHandler positionHandler;
		FrameHandler handler;
		Track track;
		
		try {
			fileSim = new FileCommunicator();
		} catch (FileNotFoundException e) {
			fileSim = null;
			// TODO Auto-generated catch block
			fail("File not found exception");
		}
		
		positionHandler = new PositionInputHandler();
		handler = new FrameHandler();

		track = new Track();

		//Set track of handler
		handler.setTrack(track);
		positionHandler.attachPositionObserver(track);

		NMEAParser fileParser = new NMEAParser(fileSim.getStream(),positionHandler);
		fileParser.setTimeout(300);
		
		track.addRunnerStick(new RunStick(0));
		fileParser.startReading();

		while(track.getCounter() == 0){
			//do nothing
		}
		
		fileParser.stopReading();
		double error = track.getRunnerStick(0).getLastPosition().getError();
		//check result
		assertEquals(29120,error,1);
	}

}
