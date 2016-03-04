package world;

public class Citizen {
	
	protected static int actualID  = 0;
	
	protected int id;
	protected int houseId = -1;
	protected int workBuildingId = -1;
	
	public Citizen(int houseId) {
		this.houseId = houseId;
		this.id = Citizen.actualID ++;
	}
	
	public void setWorkBuildingId(int workBuildingId) {
		this.workBuildingId = id;
	}
	
	
}
