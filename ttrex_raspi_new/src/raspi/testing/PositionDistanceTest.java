package raspi.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import shared.utilities.TtrexPosition;

public class PositionDistanceTest {

	private double distance;

	@Test
	public void test() {
		TtrexPosition source;
		
		//trivial cases
		source = new TtrexPosition(0,0);
		distance = source.getDistance(new TtrexPosition(0,0));
		assertEquals(distance,0,0);
		
		source = new TtrexPosition(4.708591103553772,50.874177152661424);
		distance = source.getDistance(new TtrexPosition(4.708591103553772,50.874177152661424));
		assertEquals(distance,0,0);
		
		//distance 2 coord
		source = new TtrexPosition(4.708591103553772,50.874177152661424);
		distance = source.getDistance(new TtrexPosition(4.708912968635559,50.87400451302794));
		assertEquals(distance,30,1);
		
	}

}
