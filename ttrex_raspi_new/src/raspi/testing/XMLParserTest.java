package raspi.testing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import raspi.identities.RunStick;
import raspi.utilities.PositionXMLParser;
import shared.utilities.TtrexPosition;

/***
 * @author Thomas
 *
 */
public class XMLParserTest {
	RunStick stick;
	private Map<Integer,RunStick> stickMap ;
	private Map<Integer,RunStick> result ;

	@Test
	public void test() {
		File xmlFile = new File("/Users/thomasverbeke/Code/Project/ttrex_raspi/data/configure.xml");
		PositionXMLParser parser = new PositionXMLParser(xmlFile);
		
		stickMap = new HashMap<Integer,RunStick>();

		stickMap.put(0, new RunStick(0,new TtrexPosition(4.7078025341034,50.87529760219)));
		stickMap.put(1, new RunStick(1,new TtrexPosition(4.7099107503891,50.874796619023)));
		stickMap.put(2, new RunStick(2,new TtrexPosition(4.7080332040787,50.874400567708)));

		result = parser.getRunnerMap();
		assertEquals(stickMap.get(0).getStartPosition().getLat(),result.get(0).getStartPosition().getLat(),0);
		assertEquals(stickMap.get(1).getStartPosition().getLat(),result.get(1).getStartPosition().getLat(),0);
		assertEquals(stickMap.get(2).getStartPosition().getLat(),result.get(2).getStartPosition().getLat(),0);
		
	}
	
	
	


}
