package be.ttrax.raspi.communication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class that handles all the communication with the serial port on the Raspberry Pi
 * 
 * 
 * 
 * @author Christopher
 */
public class SerialCommunicator {
    
    private InputStream in;
    private OutputStream out;
    private SerialPort serialPort;
  
    /**
     * Constructor for the SerialCommunicator
     */
    public SerialCommunicator(){
    	
    }
    
    /**
     * Setup the communication to be able to send 
     * and receive input from the Raspberry Pi serial port
     * All the data for the serial port is hard-coded (baudrate,port,bits,stopbit,...)
     * Method also makes the input and output stream available
     * @throws Exception 
     */
    public void setupCommunication() throws Exception{

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyS80");
		
		if(portIdentifier.isCurrentlyOwned()){
			
			System.out.println("Port is currently in use");
		}else
		{
			
			CommPort commPort = portIdentifier.open(this.getClass().getName(),4000);
			
			if( commPort instanceof SerialPort ){
			
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				
			System.out.println("Port opened, info from setupCommunication in SerialCommunicator");
			
			}else{
				
				System.out.println("Error, was not a serial port");
			
			}
		}

	
    }

    /**
     * Close the connection on the serial port 
     */
    public void closeConnection(){
        
      serialPort.close();
      
    }
    
    /**
     * Function returns the input stream 
     * setupCommunication should be called first
     * @return 
     */
    public InputStream getInputStream(){
        return in;
    }
    
    /**
     * 
     * Getter for the outputstream in case
     * data is to be written to the serial port
     * @return
     */
    public OutputStream getOutputStream(){
        return out;
    }
    
    



}
