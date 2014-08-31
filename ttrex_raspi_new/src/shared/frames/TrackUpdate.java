package shared.frames;


import java.util.ArrayList;
import java.util.List;

import shared.utilities.*;

public class TrackUpdate extends Frame {

	private static final long serialVersionUID = 1L;
	
	List<TtrexPosition> positionList;
	
	public TrackUpdate() {
		
		this.positionList = new ArrayList<TtrexPosition>();
	
	}
	
	
	
	public void addPosition(double longitude, double latitude){
		
		positionList.add(new TtrexPosition(longitude, latitude));
	}
	
	
	public List<TtrexPosition> getList(){
		
		return positionList;
		
	}



	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
