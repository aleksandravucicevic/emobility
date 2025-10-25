package emobility.reporting;

import java.util.Date;
import emobility.vehicles.*;

/**
 * Represents a bill generated during a rental of a vehicle.
 * This class contains information about rented vehicle, occurrence of faults, date, time and area of the rental, 
 * as well as the financial information regarding rentals, such as base price, distance, discount and promotional discount factors and total price.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class Bill{
	/** Bill's unique ID. */
	private Integer billID;
	
	/** The area in which the vehicle operates during rental. */
	private String area;
	
	/** Vehicle's unique ID. */
	private String vehicleID;
	
	/** Rental's date and time. */
	private Date dateTime;
	
	/** Indicates whether there was a fault on the vehicle during rental. */
	private Boolean fault;
	
	/** Base price of the rental. */
	private Double basePrice;
	
	/** Factor calculated based on the area in which the vehicle operates during the rental. */
	private Double distanceFactor;
	
	/** Factor calculated based on the number of user's rentals by the time current rental is done. */
	private Double discountFactor;
	
	/** Factor calculated based on the existence of a promotional discount by the time current rental is done. */
	private Double promoFactor;
	
	/** Total price of the rental. */
	private Double totalPrice;
	
	/** The rented vehicle. */
	private Vehicle vehicle;
	
	/**
	 * Constructs a new {@code Bill} object with specified details.
	 * @param billID bill's unique ID
	 * @param area area in which the rented vehicle operates
	 * @param vehicleID rented vehicle's unique ID
	 * @param dateTime date and time of the rental
	 * @param fault information if the fault occurred or not
	 * @param basePrice base price of the rental
	 * @param distanceFactor distance factor based on rental's area
	 * @param discountFactor discount factor based on user's number of rentals
	 * @param promoFactor promotional discount factor
	 * @param totalPrice total price of the rental
	 */
	public Bill(Integer billID, String area, String vehicleID, Date dateTime, Boolean fault, Double basePrice, Double distanceFactor, Double discountFactor, Double promoFactor, Double totalPrice){
		this.billID = billID;
		this.area = area;
		this.vehicleID = vehicleID;
		this.dateTime = dateTime;
		this.fault = fault;
		this.basePrice = basePrice;
		this.distanceFactor = distanceFactor;
		this.discountFactor = discountFactor;
		this.promoFactor = promoFactor;
		this.totalPrice = totalPrice;
		
		this.vehicle = VehicleParser.getVehicle(this.vehicleID);
	}
	
	/**
	 * Gets rental's total price.
	 * @return rental's total price
	 */
	public Double getTotalPrice(){
		return totalPrice;
	}
	
	/**
	 * Calculates the price based on the duration of the rental.
	 * @return price determined by the rental's duration
	 */
	public Double getDistancePrice(){
		return basePrice * distanceFactor;
	}
	
	/**
	 * Calculates the price based on the area in which the vehicle operates during rental.
	 * @return price determined by the rental's area
	 */
	public Double getDiscountPrice(){
		return getDistancePrice() * discountFactor;
	}
	
	/**
	 * Calculates the promotional discount for the current rental.
	 * @return the promotional discount price
	 */
	public Double getPromoPrice(){
		return getDistancePrice() * promoFactor;
	}
	
	/**
	 * Gets the rental's date and time.
	 * @return rental's date and time
	 */
	public Date getDateTime(){
		return dateTime;
	}
	
	/**
	 * Gets the rented vehicle.
	 * @return rented vehicle
	 */
	public Vehicle getVehicle(){
		return vehicle;
	}
	
	/**
	 * Gets the rented vehcile's purchase price.
	 * @return vehicle's purchase price
	 */
	public Double getVehiclePurchasePrice(){
		return vehicle.getPurchasePrice();
	}
	
	/**
	 * Gets the rented vehicle's type.
	 * @return vehicle's type
	 */
	public String getVehicleType(){
		return vehicle.getType();
	}
	
	/**
	 * Gets bill's unique ID.
	 * @return bill's ID
	 */
	public Integer getBillID(){
		return billID;
	}
	
	/**
	 * Gets the area in which the vehicle operates during rental.
	 * @return rental's area
	 */
	public String getArea(){
		return area;
	}
	
	/**
	 * Gets the information whether the fault happened during rental.
	 * @return {@code true} if the fault happened, otherwise {@code false}
	 */
	public Boolean isFault(){
		return fault;
	}
}
