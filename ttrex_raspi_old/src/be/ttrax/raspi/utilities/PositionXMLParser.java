package be.ttrax.raspi.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import be.ttrax.raspi.identities.RunStick;


/***
 * Parses the start positions for the runner sticks
 * from the available xml file 
 * 
 * 
 * @author Christopher
 *
 */
public class PositionXMLParser {

	Document dom;
    private Map<Integer,RunStick> stickMap ;

	public PositionXMLParser() {
		
		stickMap = new HashMap<Integer,RunStick>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Location of the xml file
			
			File destination = new File("configure.xml");
			
			URL url = new URL("http://eng.studev.groept.be/thesis/a12_coptermotion/upload/configure.xml");
			
			//Try to update the config file with a new config file
			try{
				FileUtils.copyURLToFile(url, destination);
			
			}catch(IOException e){
				
				System.out.println("Cannot read config.xml from web, using old one instead");
			}
			
			dom = db.parse(destination);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	
	}

private void parseDocument(){
	
	//Root element
	Element uselessDocEl = dom.getDocumentElement();
	Element docEl = (Element)uselessDocEl.getFirstChild();
	
	
	//nodelist of elements
	NodeList nl = docEl.getChildNodes();
	
	if(nl != null && nl.getLength() > 0) {
		for(int i = 0 ; i < nl.getLength();i++) {

			//get the employee element
			Element el = (Element)nl.item(i);

			RunStick stick = stickFromElement(el);
			
			stickMap.put(stick.getId(), stick);

		}
	}
}

public Map<Integer,RunStick> getRunnerMap(){
	
	parseDocument();
	return stickMap;
	
}


private RunStick stickFromElement(Element e){
	
	int id = Integer.parseInt(e.getAttribute("id"));
	double longitude = 0;
	double latitude = 0;
	
	NodeList childNodes = e.getChildNodes();
	
	if(childNodes != null && childNodes.getLength()>0){
		for(int i = 0; i < childNodes.getLength();i++){
			
			Element el = (Element)childNodes.item(i);
			
			if(el.getNodeName() == "startLatitude"){
				
				latitude = Double.parseDouble(el.getTextContent());
			}
			
			if(el.getNodeName() == "startLongitude"){
			
				longitude = Double.parseDouble(el.getTextContent());
				
			}
			
			
		}
		
	}
	
	TtrexPosition pos = new TtrexPosition(longitude,latitude);
	RunStick stick = new RunStick(id,pos);
		
	return stick;
	
	}
	
	

}
