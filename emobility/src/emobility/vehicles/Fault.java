package emobility.vehicles;

import java.util.*;
import java.io.Serializable;

/**
 * Represents a fault that can occur in a vehicle during its operation.
 * This class stores information about the fault's description and 
 * the date and time of the rental in which the fault occurred.
 * <p>Implements {@link Serializable} interface for object serialization.</p>
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class Fault implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** A description of the fault. */
	private String description;
	
	/** The date and time of the rental in which the fault occurred. */
	private Date dateTime;
	
	/**
	 * Enum representing fault descriptions that could possibly occur in a vehicle.
	 */
	public enum FaultDescription{
		/** Represents engine issue in the vehicle. */
        ENGINE_FAILURE,
        /** Represents battery issue in the vehicle. */
        BATTERY_ISSUE,
        /** Represents brake failure in the vehicle. */
        BRAKE_FAILURE,
        /** Represents electrical fault in the vehicle. */
        ELECTRICAL_FAULT,
        /** Represents software glitch in the vehicle. */
        SOFTWARE_GLITCH,
        /** Represents tire puncture in the vehicle. */
        TIRE_PUNCTURE
    }
	
	/**
	 * Constructs a new {@code Fault} object with the specified description and occurrence time.
	 * @param description the fault's description
	 * @param dateTime the date and time of the rental in which the fault occurred
	 */
	public Fault(String description, Date dateTime){
		this.description = description;
		this.dateTime = dateTime;
	}
	
	/**
	 * Generates a random fault description from the available {@link FaultDescription} enum values.
	 * @return a randomly selected fault description as a string
	 */
	public static String getRandomFaultDescription(){
		FaultDescription descriptions[] = FaultDescription.values();
		Integer randomIndex = new Random().nextInt(descriptions.length);
		return descriptions[randomIndex].name().replace("_"," ").toLowerCase();
	}
	
	/**
	 * Gets the description of the fault.
	 * @return the fault's description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Gets the date and time of the rental in which the fault occurred.
	 * @return the precise date and time
	 */
	public Date getDateTime(){
		return dateTime;
	}
	
	/**
	 * Returns a string representation of the fault, including its details.
	 * @return a string representation of the fault
	 */
	@Override
	public String toString(){
		return description + "," + dateTime;
	}
}