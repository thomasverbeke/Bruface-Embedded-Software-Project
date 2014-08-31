package be.ttrax.raspi.handlers;

import be.ttrax.raspi.utilities.TtrexPosition;

/**
 * Interface for a PositionObserver
 * Must be able to handle the Position Events
 * @author Christopher
 *
 */
public interface PositionObserver {

	
	public void HandlePositionEvent(TtrexPosition pos, int runnerId);
	

}
