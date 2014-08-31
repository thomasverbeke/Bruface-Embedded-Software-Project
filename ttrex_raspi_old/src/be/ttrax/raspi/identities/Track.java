package be.ttrax.raspi.identities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.ttrax.raspi.communication.WebCommunicator;
import be.ttrax.raspi.frames.TrackEvent;
import be.ttrax.raspi.handlers.PositionObserver;
import be.ttrax.raspi.utilities.PositionXMLParser;
import be.ttrax.raspi.utilities.TtrexPosition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;



/**
 * A Track maintains all information about one race
 * 
 * It implements the PositionObserver interface, as it is able to handle these events
 * 
 * It is the main class where all the computation is involved
 * 
 * @author Christopher
 */
public class Track implements PositionObserver {
    
	private EntityManager em;
	private PositionXMLParser parser;
    //Contains a list of GPS position points
    //Contains a list of runners
    private List<TtrexPosition> trackDescriptor;
    private Map<Integer,RunStick> runnerStickMap ;
    
    private WebCommunicator web;
    
    
    /**
     * Constructor for the Track
     * 
     * @param WebCommunicator to send the events to 
     */
    public Track(WebCommunicator web){
    	
    	this.web = web;
    	
    	
    	//Get track points from database
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("track.odb");
		em = emf.createEntityManager();
        trackDescriptor = loadTrackDb(1);
        
        System.out.println("Track is loaded from database");

        //Initiate Hashmap
    	parser = new PositionXMLParser();
    	runnerStickMap = parser.getRunnerMap();
    	
    	System.out.println("RunSticks initiated from configure.xml");
    	
        
    }
    
    /**
     * Load a saved track from the track database
     * 
     * @param track_id
     * @return List of positions on the track
     */
    private List<TtrexPosition> loadTrackDb(int track_id){
    	
    	
    	List<TtrexPosition> result_list = new ArrayList<TtrexPosition>();
    	
    	//Receive the data from the database
    	em.getTransaction().begin();
    	//Create the query
    	TypedQuery<PositionWrapper> qry = em.createNamedQuery("PositionWrapper.getTrackCollection",PositionWrapper.class);
		qry.setParameter("track_id", track_id);
		//Get the results
		List<PositionWrapper> results = qry.getResultList();
    	Iterator<PositionWrapper> it = results.iterator();
    	em.getTransaction().commit();
    	
    	//Reconvert it into TtrexPosition objects
    	while(it.hasNext()){
    		result_list.add(it.next().getTtrexPosition());
    	}
    	
    	return result_list;
    	
    }
    
    
    /**
     * Update the track with a new set of position points
     * Once a new track is submitted, all the statistics are reset
     * 
     * @param List<TtrexPosition> trackList
     */
    public void updateTrack(List<TtrexPosition> trackList){
    	
    	trackDescriptor.clear();
    	trackDescriptor.addAll(trackList);
    	
    	System.out.println("New Track received");
    	//Start with updating to the database
    	em.getTransaction().begin();
    	//Delete the previous track 
    	TypedQuery<PositionWrapper> qry = em.createNamedQuery("PositionWrapper.deleteTrack", PositionWrapper.class);
    	qry.setParameter("track_id", 1);
    	qry.executeUpdate();
    	//Update the database with the new track
    	Iterator<TtrexPosition> it1 = trackList.iterator();
    	int i = 0;
    	while(it1.hasNext()){
    		TtrexPosition pos = it1.next();
    		em.persist(new PositionWrapper(1,i,pos.getLong(),pos.getLat()));
    		i++;
    	}
    	em.getTransaction().commit();
    	
    	//The webserver only sends a new track when it was able to update 
    	//the configure.xml file.
    	runnerStickMap.clear();
    	parser = new PositionXMLParser();
    	runnerStickMap = parser.getRunnerMap();
    	
    }
    
    
    /**
     * Method searches for the closest GPS point that is in the list of tracks
     * Also sets percentage in the TrexPosition event
     * 
     * 
     * @param run_pos
     * @return TtrexPosition that 
     */
    private TtrexPosition getClosestTrackPoint(TtrexPosition run_pos, TtrexPosition last_position){
    	
    	//if the tracklist is empty, just return the current position
    	if(trackDescriptor.isEmpty()){
    		return null;
    	}
    	
    	//Take length very long for the beginning
    	double length_min = 99999;
    	TtrexPosition closest_pos = null;
    	Iterator<TtrexPosition> it = trackDescriptor.iterator();
    	
    	//Iterate over all positions to find the closest position on the track
    	while(it.hasNext()){
    		TtrexPosition trackPos = it.next();
    		
    		double length_new = run_pos.getDistance(trackPos);
    		
    		//if length is shorter than previous length, adapt new position
    		//Also the position must be at least equally far down the track
    		if(length_new < length_min){
    			length_min = length_new ;
    			closest_pos = trackPos;
    		}
    	}
    	
    	//Check if it's a valid point
    	int current_index = trackDescriptor.indexOf(closest_pos);
    	int list_size = trackDescriptor.size();
    	
    	closest_pos.setError(length_min);
    	closest_pos.setPercentage((double)current_index/(double)list_size);
    	
    	if(last_position != null){
    		//now check if the run_position is a possible position
    		int last_index = trackDescriptor.indexOf(last_position);
    		
    		
    		int i;
    		boolean in_forward_range = false;
    		final int CONST_RANGE = 15;
    		//Check if current index is in the forwar range
    		for(i = 0 ; i <= CONST_RANGE ; i++){
    			
    			int pos_index = (list_size + last_index + i) % list_size;
    			
    			if(pos_index == current_index){
    				in_forward_range = true;
    			}
    		}
    		
    		//If in_forward_range is true, closest_pos is valid
    		//Otherwise, stay with last position
    		if(in_forward_range){
    			return closest_pos;
    		}else{
    			return last_position;
    		}
   	
    	}else{
    		return closest_pos;
    	}
    	
    	

    	
    	
    	
    }
   
    /**
     * Method checks if the runner has passed the starting point for a certain runstick
     * And returns true if a new round has begun
     * 
     */
    private boolean checkNewRound(TtrexPosition closestTrackPoint, RunStick stick){
    	
    	final int MARGIN = 3; //the number of positions 
    	
    	boolean b_lastpos = false;
    	boolean b_nextpos = false;
    	
    	int list_size = trackDescriptor.size();
    	
    	int index_startpos = trackDescriptor.indexOf(stick.getStartPosition());
    	int index_lastpos = trackDescriptor.indexOf(stick.getLastPosition());
    	int index_currentpos = trackDescriptor.indexOf(closestTrackPoint);
    	
    	//check if last round was around the starting point
    	for(int i = 0; i <= MARGIN; i++){
    		
    		if(index_lastpos == ((list_size + index_startpos - i -1)%list_size) ){
    			
    			b_lastpos = true;
    		}
    		
    		if(index_currentpos == ((list_size + index_startpos + i)%list_size)){
    			
    			b_nextpos = true;
    		}
    		
    	}
    	
    	
    	return (b_lastpos && b_nextpos);	
    	
    }
   
    /**
     * Handle an position event that is thrown by the PositionInputHandler
     */
    public void HandlePositionEvent(TtrexPosition pos, int runnerId) {
    	//Define a RunStick
    	RunStick stick;
    	//If the RunStick is already in the arraylist, just load that stick
    	//Otherwise create a new stick
    	if(runnerStickMap.containsKey(runnerId)){
    		
    		stick = runnerStickMap.get(runnerId);
    		
    		//Get the closest track point on the track
    		TtrexPosition pos_track = getClosestTrackPoint(pos, stick.getLastPosition());
    		
    		if(pos_track == null){
    			//return from method, means no points are in track
    			System.out.println("No track defined :'(");
    			return;
    		}
    		//Add the position to the runstick
    		if(checkNewRound(pos_track, stick)){
    			stick.incrementRound();
    		}
    	
    		stick.addPosition(pos_track);
    	
    		//Print debug information
    		System.out.println("R: "+ runnerId + " E:" + pos_track.getError() + " P:"+ pos_track.getPercentage() + " LAT:" +pos_track.getLat() + " LONG:" + pos_track.getLong() + "SPEED:"+ pos_track.getSpeed() + "Rounds:" + stick.getNumRounds());

    		//Send an event to the webserver
    		TrackEvent event = new TrackEvent(runnerId,pos_track.getPercentage(),pos.getLong(),pos.getLat(),pos.getSpeed());
    		event.setRounds(stick.getNumRounds());
    		web.sendFrame(event);
    		System.out.println("Frame send from track class");
    	}
    
    }
    
    
    
}
