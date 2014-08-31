package raspi.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import raspi.identities.RunStick;
import shared.utilities.TtrexPosition;


/***
 * Parses the start positions for the runner sticks
 * from the available xml file 
 * 
 * 
 * @author Christopher, Thomas (rewrote the whole class because it was not working)
 *
 */
public class PositionXMLParser {

	Document dom;
	private Map<Integer,RunStick> stickMap ;

	public PositionXMLParser(File xmlFile) {
		
		stickMap = new HashMap<Integer,RunStick>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Location of the xml file
			
			dom = db.parse(xmlFile);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}


	private void parseDocument(){
		dom.getDocumentElement().normalize();
		//Root element
		Element root = dom.getDocumentElement();
		System.out.println(root.getNodeName());
		
		
		NodeList nl = root.getElementsByTagName("group");
		
		if(nl != null) {
			for(int i = 0 ; i < nl.getLength();i++) {
	
				//get the employee element
				Node node = nl.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					double latitude = 0;
					double longitude = 0;
					Element el = (Element) node;

					int id = Integer.parseInt(el.getAttribute("id"));

					latitude = Double.parseDouble(el.getElementsByTagName("startLatitude").item(0).getTextContent());
					longitude = Double.parseDouble(el.getElementsByTagName("startLongitude").item(0).getTextContent());
					TtrexPosition pos = new TtrexPosition(longitude,latitude);
					RunStick stick = new RunStick(id,pos);
					
					stickMap.put(stick.getId(), stick);
				}
				
	
			}
		}
	}

	public Map<Integer,RunStick> getRunnerMap(){
		
		parseDocument();
		return stickMap;
		
	}



	
	

}
