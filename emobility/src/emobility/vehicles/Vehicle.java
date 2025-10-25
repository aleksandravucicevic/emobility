package emobility.vehicles;

import java.util.*;
import java.io.Serializable;

/**
 * Represents a generic vehicle in the eMobility system.
 * This class is intended to be extended by specific types of vehicles.
 * <p>It implements {@link Serializable} interface for object serialization and 
 * {@link Comparable} interface for sorting vehicles by their IDs.</p>
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */

public abstract class Vehicle implements Serializable, Comparable<Vehicle>{
	private static final long serialVersionUID = 1L;
	/** Vehicle's unique identifier. */
	protected String ID;
	
	/** Vehicle's manufacturer. */
	protected String manufacturer;
	
	/** Model of the vehicle. */
	protected String model;
	
	/** Vehicle's purchase price. */
	protected Double purchasePrice;
	
	/** Vehicle's current battery level, represented as a percentage. */
	protected Integer batteryLevel;
	
	/** List of faults that have occurred during rentals of this vehicle. */
	protected List<Fault> faults;
	
	/** Type of vehicle (e.g. car, bicycle, scooter). */
	protected String type;
	
	/**
	 * Constructs a new {@code Vehicle} object with specified details.
	 * @param ID vehicle's unique identifier
	 * @param manufacturer vehicle's manufacturer
	 * @param model model of the vehicle
	 * @param purchasePrice vehicle's purchase price
	 * @param batteryLevel vehicle's initial battery level, as a percentage
	 */
	public Vehicle(String ID, String manufacturer, String model, Double purchasePrice, Integer batteryLevel){
		this.ID = ID;
		this.manufacturer = manufacturer;
		this.model = model;
		this.purchasePrice = purchasePrice;
		this.batteryLevel = batteryLevel;
		faults = new ArrayList<>();
		type = "vehicle";
	}

	/**
	 * Returns a string representation of the vehicle, including its details and list of faults.
	 * @return a string representation of the vehicle
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
	    sb.append("List of all faults that happened during rentals:\n");
	    
	    if(faults != null && !faults.isEmpty()){
	        for(Fault fault : faults){
	            sb.append(fault.toString()).append("\n");
	        }
	    } else{
	        sb.append("No faults");
	    }
		
		return "Vehicle " + ID + " by " + manufacturer + ", model: " + model + "\npurchase price:\n " + purchasePrice + "\ncurrent battery level:\n " + batteryLevel + "(%)\n" + sb.toString();
	}
	
	/**
	 * Returns a brief string representation of the vehicle including only basic information.
	 * @return a string representation of the vehicle with only basic information
	 */
	public String vehicleToString(){
		return "Vehicle  " + ID + "  ( manufacturer:  " + manufacturer + " ,  model: " + model;
	}
	
	/**
	 * Compares this vehicle to another one by their IDs.
	 * @param vehicle the vehicle to compare to
	 * @return a negative, zero or a positive integer as this vehicle's ID is less than, equal to, or grater than the specified vehicle's ID
	 */
	@Override
    public int compareTo(Vehicle vehicle){
        return this.ID.compareTo(vehicle.getID());
    }

	/**
	 * Sets the battery level of the vehicle
	 * @param batteryLevel the new battery level, as a percentage
	 */
	public void setBatteryLevel(Integer batteryLevel){
		this.batteryLevel = batteryLevel;
	}
	
	/**
	 * Gets the current battery level of the vehicle.
	 * @return the battery level as a percentage
	 */
	public Integer getBatteryLevel(){
		return batteryLevel;
	}
	
	/**
	 * Gets the vehicle's unique identifier.
	 * @return the vehicle's ID
	 */
	public String getID(){
		return ID;
	}
	
	/**
	 * Gets the vehicle's purchase price.
	 * @return the purchase price
	 */
	public Double getPurchasePrice(){
		return purchasePrice;
	}
	
	/**
	 * Gets the vehicle's model name.
	 * @return the model name
	 */
	public String getModel(){
		return model;
	}
	
	/**
	 * Gets the vehicle's manufacturer.
	 * @return the manufacturer
	 */
	public String getManufacturer(){
		return manufacturer;
	}
	
	/**
	 * Gets the list of faults that occurred during rentals of this vehicle.
	 * @return a list of {@link Fault} objects representing faults during rentals
	 */
	public List<Fault> getFaults(){
		return faults;
	}
	
	/**
	 * Registers a fault that occurred on the vehicle at the specified date and time.
	 * Description of the fault is generated randomly.
	 * @param dateTime the date and time when the fault occurred
	 * @return the registered {@link Fault} object
	 */
	public Fault registerFault(Date dateTime){
		String description = Fault.getRandomFaultDescription();
		Fault fault = new Fault(description, dateTime);
		this.faults.add(fault);
		return fault;
	}
	
	/**
	 * Gets the type of the vehicle.
	 * @return the vehicle's type
	 */
	public String getType(){
		return type;
	}
}