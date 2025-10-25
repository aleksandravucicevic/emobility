package emobility.vehicles;

import java.io.Serializable;

/**
 * Represents an electric scooter in the eMobility system.
 * Extends the {@link Vehicle} class to include attributes specific for scooters such as maximum speed.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class ElectricScooter extends Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** The maximum speed the scooter can reach.*/
	private Integer maxSpeed;
	
	/**
	 * Constructs a new {@code ElectricScooter} object with the specified details.
	 * @param ID scooter's unique identifier
	 * @param manufacturer scooter's manufacturer
	 * @param model model of the scooter
	 * @param purchasePrice scooter's purchase price
	 * @param batteryLevel scooter's initial battery level, as a percentage
	 * @param maxSpeed scooter's maximum speed
	 */
	public ElectricScooter(String ID, String manufacturer, String model, Double purchasePrice, Integer batteryLevel, Integer maxSpeed){
		super(ID,manufacturer,model,purchasePrice,batteryLevel);
		this.type = "scooter";
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * Returns a string representation of the scooter, including its details.
	 * @return a string representation of the scooter
	 */
	@Override
	public String toString(){
		return super.toString() + "\n type of vehicle: " + type + ", max speed: " + maxSpeed;
	}
	
	/**
	 * Returns a brief string representation of the scooter, including only basic information.
	 * @return a string representation of the scooter with only basic information
	 */
	@Override
	public String vehicleToString(){
		return super.vehicleToString() + "  maximum speed:  " + maxSpeed + " )";
	}
	
	/**
	 * Gets the type of the vehicle - in this case "scooter".
	 * @return the vehicle's type
	 */
	@Override
	public String getType(){
		return this.type;
	}
	
	/**
	 * Gets the scooter's maximum speed.
	 * @return the maximum speed
	 */
	public Integer getMaxSpeed(){
		return maxSpeed;
	}
}