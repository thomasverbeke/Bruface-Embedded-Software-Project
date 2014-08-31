/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raspi.nmea;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import raspi.handlers.PositionInputHandler;
import shared.utilities.TtrexPosition;
import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;

/**
 * NMEA Parser that handles an inputstream and gets useful position objects
 * out of the stream
 * 
 * @author Christopher, Thomas
 */
public class NMEAParser {
    
	private int timeout_ms;
    private SentenceReader reader;
    private NMEAParser.SentenceHandler sentenceHandler;
    private PositionInputHandler positionHandler;
    public ArrayList<Double> list;
    
    /**
     * Constructor for the NMEAParser
     * @param stream
     * @param positionHandler
     */
    public NMEAParser(InputStream stream, PositionInputHandler positionHandler) {
        timeout_ms = 0;
        reader = new SentenceReader(stream);
        sentenceHandler = new SentenceHandler();
        this.positionHandler = positionHandler;
        reader.addSentenceListener(sentenceHandler);
        list = new ArrayList<Double>();
    }
    
    /**
     * Start reading from the InputStream
     * 
     */
    public void startReading(){
        
        reader.start();
    }
    
    
    public void stopReading(){
    	reader.stop();
    }
    
    public void setTimeout(int value){
    	
    	timeout_ms = value;
    	
    }
 
    
    //Inner helper class to parse the sentence events created by the SentenceReader
    class SentenceHandler implements SentenceListener{

            
            public void readingPaused() {
                System.out.println("Reading Paused");
            }

            
            public void readingStarted() {
                System.out.println("Reading Started");
            }

            
            public void readingStopped() {
                System.out.println("Reading Stopped");
            }

      
            public void sentenceRead(SentenceEvent event) {
                
            	if(timeout_ms >0){
            		
            		try {
						Thread.sleep(timeout_ms);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	
            	try {
                	
            		Runtime.getRuntime().exec("clear");
            		
                } catch(Exception e){
                		
                }
                	
            	Sentence sc = event.getSentence(); 	
            	String id = sc.getSentenceId();
          
            	//Check if it equals some of the used NMEA Sentences
            	
            	if(id.equals("GGA")){
            		GGASentence gga = (GGASentence) sc;
            		Date d = new Date();
            		TtrexPosition pos = new TtrexPosition(gga.getPosition().getLongitude(),
            									gga.getPosition().getLatitude(),gga.getTime().toDate(d));
            		
            		//positionHandler.handlePosition(pos,gga.getRunnerID()); 
            		//Had to remove this line because the adapted marine api is no longer available; pending response from Christopher
            		//System.out.print(gga.getPosition().getLongitude()+" "+gga.getPosition().getLatitude()+" "+gga.getTime().toDate(d));
            		list.add(gga.getPosition().getLatitude());
            		positionHandler.handlePosition(pos,0);
            		
            	}else if (id.equals("GSV")){
            		//GSV is Global Sattelites view and gives info about visible sattelites
            		
            	}else if(id.equals("GSA")){       		
            		//Overall GPS Information

            	}else if(id.equals("RMC")){
            		
            		RMCSentence rmc = (RMCSentence) sc;
            		
            		Date d = new Date();
            	
            		TtrexPosition pos = new TtrexPosition(rmc.getPosition().getLongitude(), rmc.getPosition().getLatitude(), rmc.getTime().toDate(d));
            		//Directly convert from knots to m/s
            		pos.setSpeed((double)rmc.getSpeed()* 0.514444444);
            		//positionHandler.handlePosition(pos,rmc.getRunnerID());
            		//Had to remove this line because the adapted marine api is no longer available; pending response from Christopher
            		positionHandler.handlePosition(pos,0);
            		
            	}else{
            		//System.out.println("Other sentence than expected");
            		//System.out.println(sc.toSentence());
            	}      
            }
    } 

}

   
    

