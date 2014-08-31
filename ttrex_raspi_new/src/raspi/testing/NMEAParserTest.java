package raspi.testing;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.util.Date;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.nmea.util.Time;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import raspi.communication.FileCommunicator;
import raspi.handlers.PositionInputHandler;
import raspi.nmea.NMEAParser;
import shared.utilities.TtrexPosition;

/** 
 *  @author Thomas 
 * 
 * **/
public class NMEAParserTest {

	private ByteArrayInputStream stream;
	
	
	@Test
	public void test() {
		
		/** SETUP **/
		
		//Generate GGA & RMC sentences	
		
		//SAMPLE DATE
	 
		//	type	time					latitude		longitude		altitude (m)	speed (km/h)	course		sat		hdop	fix				
		//	T	 	2013-03-17 	09:15:00	50.97396		5.094806660		20.1			0.8				302.4		8		1.0		1
		//	T		2013-03-17  09:20:00	51.97398		5.094806080		21.9			0.7				302.4		8		1.0		1
		//	T		2013-03-17  09:25:00	52.97400		5.094806000		10.0			1.1				302.4		8		1.0		1
		//	T		2013-03-17 	09:30:00	53.97402		5.094798333		21.9			1.0				302.4		8		1.0		1
	
		
		SentenceFactory sf = SentenceFactory.getInstance();
		GGASentence gga = (GGASentence) sf.createParser(TalkerId.GP, "GGA");
		Position pos = new Position(50.97396, 5.094806660);
		pos.setAltitude(20.1);
		gga.setPosition(pos);
		gga.setHorizontalDOP(1.0);
		gga.setTime(new Time(9,15,0));
		
		String out = gga.toSentence()+"\r\n";
		
		pos = new Position(51.97398, 5.094806080);
		pos.setAltitude(21.9);
		gga.setPosition(pos);
		gga.setHorizontalDOP(1.0);
		gga.setTime(new Time(9,20,0));
		
		out += gga.toSentence()+"\r\n";
		
		pos = new Position(52.97400, 5.094806000);
		pos.setAltitude(10.0);
		gga.setPosition(pos);
		gga.setHorizontalDOP(1.0);
		gga.setTime(new Time(9,25,0));
		
		out += gga.toSentence()+"\r\n";
		
		pos = new Position(53.97402, 5.094798333);
		pos.setAltitude(21.9);
		gga.setPosition(pos);
		gga.setHorizontalDOP(1.0);
		gga.setTime(new Time(9,30,0));
		
		out += gga.toSentence()+"\r\n";
		
		System.out.print(out);
		
		stream= new ByteArrayInputStream(out.getBytes());
		

		//NMEAParser converts RAW NMEA data into a TtrexPosition and adds it to the positionHandler
		PositionInputHandler positionHandler = new PositionInputHandler();
		NMEAParser reader = new NMEAParser(stream, positionHandler);
		
		/** Execute **/
		reader.startReading();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/** Verify **/
		
		assertEquals(reader.list.get(0),50.97396,0.0010);
		assertEquals(reader.list.get(1),51.97398,0.0010);
		assertEquals(reader.list.get(2),52.97400,0.0010);
		assertEquals(reader.list.get(3),53.97402,0.0010);
		
		/** Teardown **/
		reader.stopReading();
	}

}
