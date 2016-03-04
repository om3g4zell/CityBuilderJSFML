package world;

public class Citizen {
	protected static int lastID = 0;
	
	protected int id;
	protected int houseId = -1; // House.
	protected int workBuildingId = -1; // Work ?
	protected int smallFurnitureBuildingId = -1; // Grocery store.
	
	public Citizen(int houseId) {
		this.houseId = houseId;
		this.id = Citizen.lastID++;
	}
	
	/**
	 * Sets the id of the work building.
	 * @param workBuildingId : the id of the workBuilding
	 */
	public void setWorkBuildingId(int workBuildingId) {
		this.workBuildingId = id;
	}
	
	/**
	 * Returns the id of the work building.
	 * @return the id of the workBuilding
	 */
	public int getWorkBuildingId() {
		return this.workBuildingId;
	}
	
	/**
	 * Sets the id of the small furniture building.
	 * @param smallFoodBuildingId : the id of the small furniture building
	 */
	public void setSmallFurnitureBuildingId(int smallFurnitureBuildingId) {
		this.smallFurnitureBuildingId = smallFurnitureBuildingId;
	}
	
	/**
	 * Returns the id of the small furniture building.
	 * @return the small furniture building id
	 */
	public int getSmallFurnitureBuildingId() {
		return this.smallFurnitureBuildingId;
	}
}
