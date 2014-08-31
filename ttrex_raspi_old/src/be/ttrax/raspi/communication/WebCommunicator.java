package be.ttrax.raspi.communication;


import java.io.*;
import java.net.*;
import be.ttrax.raspi.handlers.FrameHandler;



/**
 * This class serves the communication with the private Web server
 * That webserver is then responsible for further broadcasting of the data.
 * 
 * Currently this implementation only supports only 1 webserver to be connected.
 * 
 * TODO: Implement for more concurrent connections
 * 
 * @author Christopher
 */

public class WebCommunicator implements Runnable {
    
    private ServerSocket srvrSckt;
    private Socket clientSckt;
    //Streams for a client socket
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    //Indicates wether or not there is a connection going on
    private boolean isConnected;
    private boolean closeServer;
    //Thread where the connection runs in
    private Thread thisThread;
	
	private FrameHandler frameHandle;
    
	/**
	 * Constructor for the webcommunicator
	 * 
	 * @param handler
	 * @throws Exception
	 */
    public WebCommunicator(FrameHandler handler) throws Exception{
    
    	//Connect handler
    	frameHandle = handler;
    	
    	//Initiate the web server
    	isConnected = false;
    	closeServer = false;
    	//Startup server
    	srvrSckt = new ServerSocket(5999);
		
    	//Start in a new thread
    	thisThread = new Thread(this);
    	thisThread.start();
    	
    }
    
    /**
     * Frame sends a frame to the webserver if connected
     * Frames are defined in the shared library
     * 
     * @param fr
     * @throws IOException
     */
    public void sendFrame(Object obj){
    	
    	if(isConnected){
    	
    	try {
			System.out.println("Sending frame to webserver from Webcommunicator");
			output.writeObject(obj);
			//Directly flush the object to make sure it is sent to the webserver
			output.flush();

		} catch (IOException e) {
			// TODO Write error handling because now frame is discarded
			System.out.println("Error in sending frame to webserver");
		}
    	}
    	
    }
    
    /**
     * Receive a frame from the webserver,
     * The frame is passed on to the frameHandler
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void receiveFrame() throws ClassNotFoundException, IOException{
    	
    	//Read in a arbitry object
		Object obj;
		obj = input.readObject();
		
    	//Send object to handler for parsing purposes
    	frameHandle.parseFrame(obj);
    	
    }
    	
    public void close(){
    	
    	closeServer = true;
    	
    }
	
    public void run() {
    	
    	//Once started 
    	while(!closeServer){
    		
    		try {
    			isConnected = false;
    			clientSckt = srvrSckt.accept();

    			System.out.println("Webserver connected");
    			output = new ObjectOutputStream(clientSckt.getOutputStream());
				input = new ObjectInputStream(clientSckt.getInputStream());
				
				
				System.out.println("Streams attached");
				isConnected = true;
				
				while(true){
					
				
					try{
						
					receiveFrame();
					
					
					}catch(EOFException e){
						//EOF Exception means that webserver has closed connection
						isConnected = false;
						System.out.println("Webserver has disconnected");
						break;
					}catch(Exception e){
						isConnected = false;
						clientSckt.close();
						System.out.println("Something weird happened!");
						e.printStackTrace();
						break;
					}
				
				}
    		
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
    		
			}
    		
    		
    		
    	}
    	
    	//When run arrives here, it should be closed
    	try{
    		clientSckt.close();
    		srvrSckt.close();
    	}catch(Exception e){
    		System.out.println("Error in closing down sockets");
    		//TODO: Error handling
    	}
		
	}


    

}