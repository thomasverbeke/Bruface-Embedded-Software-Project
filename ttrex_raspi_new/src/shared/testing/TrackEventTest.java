package shared.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.frames.TrackEvent;

public class TrackEventTest {
	@Test
	public void testSetGetRounds() {
		TrackEvent frame = new TrackEvent(0,10,4.707373380661011,50.874850779625284);
		frame.setRounds(1);
		assertEquals(frame.getRounds(),1,0);
	}
	

}
