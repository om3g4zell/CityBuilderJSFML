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
	
	/**
	 * Set the id of his workBuilding.
	 * @param workBuildingId : the id of his workBuilding
	 */
	public void setWorkBuildingId(int workBuildingId) {
		this.workBuildingId = id;
	}
	
	/**
	 * Get the id of his workBuilding
	 * @return int : the id of his workBuilding
	 */
	public int getWorkBuildingId() {
		return this.workBuildingId;
	}
	
	
}
