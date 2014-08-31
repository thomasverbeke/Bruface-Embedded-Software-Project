package be.ttrax.raspi.communication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

/**
 * Class in the communication package that is called to 
 * load a simulation file from the hard drive, to simulate the system
 * when no external runners are connected
 * 
 * 
 * @author Christopher
 *
 */
public class FileCommunicator implements Runnable {

	private Thread thread;
	private ByteArrayInputStream stream;
	private File file;
	
	public FileCommunicator() throws FileNotFoundException {
	
		//File is hard-coded on disk
		file = new File("/home/pi/java/15_05_13_gps_log_filtered_data.data");
		
		//Try to open the stream
		try {
			stream= new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			System.out.println("FileCommunicator: Simulation data file could not be opened");
		}
		
		//Use Marks to iteratively loop through the file, to create a 
		//continuous inputstream if like it would come from the serial port
		if(!stream.markSupported()){
			throw new RuntimeException("Mark/Reset is not supported");
		}
		
		//Start thread			
		this.thread = new Thread(this);
		thread.start();
		
	}
	
	/**
	 * Getter for the inputstream
	 * 
	 * @return the inputstream, null if file could not be opened
	 */
	public InputStream getStream(){
		
		return stream;
	}
	
	
	/**
	 * Method called when the tread is started
	 * 
	 */
	public void run(){
		
		try{
		while(true){
		
		//Continuously check if the stream is still available,
		//If not, reset the stream to the starting point
		if(stream.available() > 0){
			Thread.sleep(500);
		}else{
			stream.reset();
		}
		}
		
		}catch(Exception e){
			
			System.out.println("Error in FileCommunicator");
			e.printStackTrace();
		}
		
	}

}