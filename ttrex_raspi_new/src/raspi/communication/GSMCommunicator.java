package raspi.communication;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class using BlueNMEA app to get nmea data from the gps chip of an android device
 * Mainly used for debugging purposes when no gps data comes in on serial port
 * Could be used in future versions
 * @author Christopher
 *
 */

public class GSMCommunicator {
	
	private Socket client_socket;

	/**
	 * Constructor for a GSM Communicator
	 * 
	 */
	public GSMCommunicator() {
		
		System.out.println("Try to connect with GSM");
		
		
		try{
		client_socket = new Socket();
		
		//Address is hard-coded, connects to one of our cell phones, where BlueNMEA app is running
		client_socket.connect(new InetSocketAddress("192.168.1.91", 4352),4500);
		
		if(client_socket.isConnected()){
			System.out.println("GSM Connection successfully made");

		}else{
			System.out.println("Problem connecting to GSM");
		}
		
		}catch(Exception e){
			System.out.println("Time-out to connect with smartphone");
			client_socket = null;
		}
		
		
		
	}
	
	
	/**
	 * Getter for the inputstream
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException{
		
		
		
		if(client_socket != null){
			System.out.println("Recieving inputstream");
			return client_socket.getInputStream();
		} else{
			return null;
		}
		
	}
	
	

}
