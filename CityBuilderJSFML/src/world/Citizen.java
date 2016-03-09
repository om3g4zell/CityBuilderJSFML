package world;

public class Citizen {
	protected static int lastID = 0;
	
	protected int id;
	protected int houseId = -1; // House.
	protected int workBuildingId = -1; // Work ?
	protected int smallFurnitureBuildingId = -1; // Grocery store.
	protected int bigFurnitureBuildingId = -1; // Mall.
	protected int hobbiesBuildingId = -1; // Hobbies
	protected int sportBuildingId = -1; // Stadium
	protected int restaurantBuildingId = -1; // Restaurant
	protected int pubId = -1; // Pub
	

	public Citizen(int houseId) {
		this.houseId = houseId;
		this.id = Citizen.lastID++;
	}
	
	public int getBigFurnitureBuildingId() {
		return bigFurnitureBuildingId;
	}

	public void setBigFurnitureBuildingId(int bigFurnitureBuildingId) {
		this.bigFurnitureBuildingId = bigFurnitureBuildingId;
	}

	public int getHobbiesBuildingId() {
		return hobbiesBuildingId;
	}

	public void setHobbiesBuildingId(int hobbiesBuildingId) {
		this.hobbiesBuildingId = hobbiesBuildingId;
	}

	public int getSportBuildingId() {
		return sportBuildingId;
	}

	public void setSportBuildingId(int sportBuildingId) {
		this.sportBuildingId = sportBuildingId;
	}

	public int getRestaurantBuildingId() {
		return restaurantBuildingId;
	}

	public void setRestaurantBuildingId(int restaurantBuildingId) {
		this.restaurantBuildingId = restaurantBuildingId;
	}

	public int getPubId() {
		return pubId;
	}

	public void setPubId(int pubId) {
		this.pubId = pubId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	
	/**
	 * Returns the id of the house.
	 * @return the id of the work building
	 */
	public int getHouseId() {
		return this.houseId;
	}
	
	/**
	 * Sets the id of the work building.
	 * @param workBuildingId : the id of the workb building
	 */
	public void setWorkBuildingId(int workBuildingId) {
		this.workBuildingId = id;
	}
	
	/**
	 * Returns the id of the work building.
	 * @return the id of the work building
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
