package emobility.vehicles;

import java.io.Serializable;

/**
 * Represents an electric bicycle in the eMobility system.
 * Extends the {@link Vehicle} class to include attributes specific for bicycles 
 * such as autonomy - the distance the bicycle can travel when it's fully charged.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class ElectricBicycle extends Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** The distance the bicycle can travel if being fully charged. */
	private int autonomy; // range per one charge
	
	/**
	 * Constructs a new {@code ElectricBicycle} object with the specified details.
	 * @param ID bicycle's unique identifier
	 * @param manufacturer bicycle's manufacturer
	 * @param model model of the bicycle
	 * @param purchasePrice bicycle's purchase price
	 * @param batteryLevel bicycle's initial battery level, as a percentage
	 * @param autonomy the distance bicycle is able to travel if it's fully charged
	 */
	ElectricBicycle(String ID, String manufacturer, String model, double purchasePrice, int batteryLevel, int autonomy){
		super(ID,manufacturer,model,purchasePrice,batteryLevel);
		this.type = "bicycle";
		this.autonomy = autonomy;
	}
	
	/**
	 * Returns a string representation of the bicycle, including its details.
	 * @return a string representing the bicycle
	 */
	@Override
	public String toString(){
		return super.toString() + "\n type of vehicle : " + type + ", autonomy: " + autonomy;
	}
	
	/**
	 * Returns a brief string representation of the bicycle, including only basic information.
	 * @return a string representation of the bicycle with only basic information
	 */
	@Override
	public String vehicleToString(){
		return super.vehicleToString() + "  autonomy:  " + autonomy + " )";
	}
	
	/**
	 * Gets the type of the vehicle - in this case "bicycle".
	 * @return the vehicle's type
	 */
	@Override
	public String getType(){
		return this.type;
	}
	
	/**
	 * Gets the autonomy of the bicycle.
	 * @return the bicycle's autonomy
	 */
	public Integer getAutonomy(){
		return autonomy;
	}
}