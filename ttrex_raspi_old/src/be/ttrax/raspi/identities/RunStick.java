
package be.ttrax.raspi.identities;

import java.util.ArrayList;
import java.util.List;

import be.ttrax.raspi.utilities.TtrexPosition;

/**
 * This class represents one RunStick in the race
 * 
 * 
 * @author Christopher
 */
public class RunStick {
   
    int numRounds;
	
	private int id; //runner stick id
    
    //keeps all positions from 
    private List<TtrexPosition> positions;
    
    
    private TtrexPosition lastPosition;
    private TtrexPosition currentPosition;
    private TtrexPosition startPosition;
    
    
    /**
     * Constructor for a RunStick
     */
    public RunStick(int id){
  
    	this.positions = new ArrayList<TtrexPosition>();
        this.id = id;
        numRounds = 0;
    }
    
    /** 
     * Constructor to create a startPosition
     * @param id
     * @param startPosition
     */
    public RunStick(int id, TtrexPosition startPosition){
    	
    	this.positions = new ArrayList<TtrexPosition>();
    	this.startPosition = startPosition;
    	this.id = id ;
    	this.numRounds = 0;
    	
    	
    }
    
    
    public int getId(){
    	return id;
    }
    
    
    public void addPosition(TtrexPosition pos){
    	
    	positions.add(lastPosition);
    	lastPosition = currentPosition;
    	currentPosition = pos;
    	
    }
    
    public void incrementRound(){
    	
    	numRounds++;
    	
    }
    
    public TtrexPosition getLastPosition(){
    	
    	return currentPosition;
    }
    
    public TtrexPosition getStartPosition(){
    	
    	
    	return startPosition;
    	
    }
    
    public int getNumRounds(){
    	
    	return numRounds;
    	
    }
    
    public void setStartPosition(TtrexPosition pos){
    	
    	this.startPosition = pos;
    }
    
    
    
    
}
