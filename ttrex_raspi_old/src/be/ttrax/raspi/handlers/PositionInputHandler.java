package be.ttrax.raspi.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.ttrax.raspi.utilities.TtrexPosition;

/**
 * This class handles all the inputs from different NMEA parsers that 
 * provide this class with Position events
 * Different other objects can be attached as a listener.
 * @author Christopher
 *
 */
public class PositionInputHandler  {
	
	private List<PositionObserver> observers;
	
	/**
	 * Constructor for a PositionInputHandler
	 */
	public PositionInputHandler() {


		this.observers = new ArrayList<PositionObserver>();
		
		
	}
	
	
	/**
	 * Add an observer where frames should be sent to 
	 * @param observer
	 */
	public void attachPositionObserver(PositionObserver observer){
		
		observers.add(observer);
	}
	
	/**
	 * Remove an existing observer where frames should be sent to
	 * @param observer
	 */
	public void removePositionObserver(PositionObserver observer){
		
		observers.remove(observer);
	}
	
	/**
	 * This method is called by the different providers of positions
	 * @param position
	 * @param id
	 */
	public void handlePosition(TtrexPosition position, int id){
		
		Iterator<PositionObserver> it = observers.iterator();
		
		while(it.hasNext()){
			
			it.next().HandlePositionEvent(position,id);
			
		}
		
		
	}
	
	
	
	
}
