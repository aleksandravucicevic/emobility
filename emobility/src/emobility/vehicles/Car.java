package emobility.vehicles;

import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Represents a car in the eMobility system.
 * Extends the {@link Vehicle} class to include attributes specific for cars 
 * such as the purchase date, description and the information about the ability to transport more people.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class Car extends Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** Indicates whether the car has more seats available for passengers. */
	private Boolean moreSeats;
	
	/** The date the car was purchased. */
	private Date purchaseDate;
	
	/** A brief description of the car. */
	private String description;

	/**
	 * Constructs a new {@code Car} object with the specified details.
	 * @param ID car's unique identifier
	 * @param manufacturer car's manufacturer
	 * @param model model of the car
	 * @param purchasePrice car's purchase price
	 * @param batteryLevel car's initial battery level, as a percentage
	 * @param moreSeats indicates if the car has more seats
	 * @param purchaseDate car's purchase date
	 * @param description brief description of the car
	 */
	public Car(String ID, String manufacturer, String model, Double purchasePrice, Integer batteryLevel, Boolean moreSeats, Date purchaseDate, String description){
		super(ID,manufacturer,model,purchasePrice,batteryLevel);
		this.type = "car";
		this.moreSeats = moreSeats;
		this.purchaseDate = purchaseDate;
		this.description = description;
	}
	
	/**
	 * Returns a string representation of the car, including its details.
	 * @return a string representation of the car
	 */
	@Override
	public String toString(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
		return super.toString() + "\n type of vehicle: " + type + " - " + description + "\npurchase date:\n " + sdf.format(purchaseDate) + "\nis capable of transporting more passengers:\n " + moreSeats;
	}
	
	/**
	 * Returns a brief string representation of the car, including only basic information.
	 * @return a string representation of the car with only basic information
	 */
	@Override
	public String vehicleToString(){
		return super.vehicleToString() + "  description:  " + description + " )";
 	}
	
	/**
	 * Gets the type of the vehicle - in this case "car".
	 * @return the vehicle's type
	 */
	@Override
	public String getType(){
		return this.type;
	}
	
	/**
	 * Gets the car's purchase date.
	 * @return the purchase date
	 */
	public Date getPurchaseDate(){
		return purchaseDate;
	}
	
	/**
	 * Gets the description of the car.
	 * @return the description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Indicates whether the car is capable of transporting more passengers.
	 * @return "yes" if the car has more seats, otherwise "no"
	 */
	public String getMoreSeats(){
		return moreSeats? "yes" : "no";
	}
}