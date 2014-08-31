package be.ttrax.raspi.main;


import java.io.InputStream;

import be.ttrax.raspi.communication.FileCommunicator;
import be.ttrax.raspi.communication.GSMCommunicator;
import be.ttrax.raspi.communication.SerialCommunicator;
import be.ttrax.raspi.communication.WebCommunicator;
import be.ttrax.raspi.handlers.FrameHandler;
import be.ttrax.raspi.handlers.PositionInputHandler;
import be.ttrax.raspi.identities.Track;
import be.ttrax.raspi.nmea.NMEAParser;
/**
 * Main class for the Java software on the RaspBerry Pi
 * Creates all communicators, creates a track and starts Parsing
 * 
 * @author Christopher
 */
public class RaspiTtrex {
	
 
    private SerialCommunicator serial;
    private GSMCommunicator gsm;
    private FileCommunicator fileSim;
    private FrameHandler handler;
    private WebCommunicator web;
    private PositionInputHandler positionHandler;
 
    private Track track;
    
    
public RaspiTtrex(){
	
	//Try to open the different communicators
	try {
		
		this.serial = new SerialCommunicator();
		this.gsm = new GSMCommunicator();
		//this.fileSim = new FileCommunicator();
		
	} catch (Exception e) {
		
		System.out.println("Error in Constructor raspberry");
		e.printStackTrace();
	}
	
	//Create new positionInputHander (for the parsers output)
	positionHandler = new PositionInputHandler();
	//Create new Framehandler (for the web input)
	handler = new FrameHandler();
	
	//Try to start up the server socket to communicate with the Web Server
	try {
		web = new WebCommunicator(handler);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Create a Track
	track = new Track(web);
	//Set track of handler
	handler.setTrack(track);
	
	positionHandler.attachPositionObserver(track);
	
}


public static void main(String args[]){
    
        RaspiTtrex mainProg = new RaspiTtrex();
        

        try{
        
        mainProg.initiate();
        
        }catch(Exception e){
        	System.out.println("Error in initiating");
        	e.printStackTrace();
        	System.exit(0);
        }
        
        
 
}

/**
 * Initiates the parsers
 */
private void initiate(){
	
	try{
	
	if(serial != null){
		
		this.setupSerialInputParsing();
		
	}
	if(gsm != null && (gsm.getInputStream() != null)){
		
		NMEAParser gsmParser = new NMEAParser(gsm.getInputStream(),positionHandler);
		gsmParser.startReading();
	}
	if(fileSim != null){
		
		NMEAParser fileParser = new NMEAParser(fileSim.getStream(),positionHandler);
		fileParser.setTimeout(300);
		fileParser.startReading();
		
	}
	
	}catch(Exception e){
		
		e.printStackTrace();
	}
}
	
	
/**
 * Setup the serial input and start parsing NMEA Sentences
 * @throws Exception
 */
private void setupSerialInputParsing() throws Exception{
	
    //setup serial communication
    serial.setupCommunication();
    
    System.out.println("Serial communication setup succeeded");
    
    InputStream stream = serial.getInputStream();
    
    NMEAParser parser = new NMEAParser(stream,positionHandler);
    parser.startReading();
    System.out.println("Started reading from serial port");

	
}


    
    



}