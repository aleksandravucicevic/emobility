package emobility.rental;

import java.util.*;
import java.text.SimpleDateFormat;
import emobility.vehicles.*;

/**
 * Represents a rental of the vehicle.
 * This class contains information about the vehicle, its user, date, time and duration of the rental, 
 * as well as the start and goal location, possible problems with the vehicle, promotional discounts and 
 * possible additional discounts for the user.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class Rental{
	/** Rental's unique ID. */
	protected static Integer rentalID = 0;
	
	/** A map that stores all users by their ID documents. */
	protected static Map<String, User> users = new HashMap<>();
	
	/** A map that stores all rentals of a user sorted by date and time of the rental. */
	protected static Map<User, TreeMap<Date, List<Rental>>> rentalsByUser = new HashMap<>();
	
	/** Date and time of the rental. */
	protected Date rentalDateTime;
	
	/** User that rented the vehicle. */
	protected User user;
	
	/** Vehicle's ID. */
	protected String ID;
	
	/** Rental's start location. */
	protected String startLocation;
	
	/** Rental's goal location. */
	protected String goalLocation;
	
	/** Rental's duration in seconds. */
	protected Long duration;
	
	/** Indicates whether a fault occurred during the rental. */
	protected Boolean fault;
	
	/** Registered {@link Fault} on the rental. */
	protected Fault registeredFault;
	
	/** Indicates whether there is a promotional discount on the rental. */
	protected Boolean promo;
	
	 /** Indicates whether the user gets additional discount on the rental. */
	protected Boolean discount;
	
	/** A map that saves all the faults registered on a vehicle. */
	private static Map<Vehicle, List<Fault>> vehicleFaultsMap = new TreeMap<>();
	
	
	/**
	 * Constructs a new Rental with specified details.
	 * @param rentalDateTime date and time of the rental
	 * @param user user that rents the vehicle
	 * @param ID ID of the vehicle being rented
	 * @param startLocation rental's start location
	 * @param goalLocation rental's goal location
	 * @param fault information if the fault occurred or not
	 * @param duration duration of the rental
	 * @param promo information if there was a promotional discount
	 */
	public Rental(Date rentalDateTime, User user, String ID, String startLocation, String goalLocation, Boolean fault, Long duration, Boolean promo){
		this.rentalDateTime = rentalDateTime;
		this.user = getOrCreateUser(user.getIdDocument(),user.getDriverLicenseNumber());
		this.ID = ID;
		this.startLocation = startLocation;
		this.goalLocation = goalLocation;
		this.fault = fault;
		this.duration = duration;
		this.promo = promo;
		this.discount = false;
		
		if(fault){
			Vehicle vehicle = getVehicle();
			Fault registeredFault = vehicle.registerFault(rentalDateTime);
			this.registeredFault = registeredFault;
			updateVehicleFaultsMap(vehicle, registeredFault);
		}
		
		addToUserRentalMap();
	}
	
	/**
	 * Copy constructor for Rental class.
	 * @param rental {@link Rental} object being copied
	 */
	public Rental(Rental rental){
		this.rentalDateTime = rental.rentalDateTime;
		this.user = rental.user;
		this.ID = rental.ID;
		this.startLocation = rental.startLocation;
		this.goalLocation = rental.goalLocation;
		this.fault = rental.fault;
		this.duration = rental.duration;
		this.promo = rental.promo;
		this.discount = rental.discount;
		this.registeredFault = getVehiclesFault(getVehicle(),rentalDateTime);
	}
	
	/**
	 * Returns a string representation of the rental, including its details and user information
	 * @return a string representation of the rental
	 */
	@Override
	public String toString(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return "Rental of the vehicle:" + ID + "\ndate and time:" + dateFormat.format(rentalDateTime) + "\nfrom:" + startLocation + "\nto:" + goalLocation + "\nduration:" + duration
				+ "\nfault:" + (fault? "yes" : "no") + "\npromo:" + (promo? "yes" : "no") + "\nUser info:" + user.toString();
	}
	
	/**
	 * Returns existing user based on the ID document, or creates a new one if the user doesn't exist
	 * @param userDocumentID user's ID document (such as ID or passport)
	 * @param driverLicenseNumber user's driver's license number
	 * @return {@link User} object that corresponds to the specified ID document
	 */
	private static User getOrCreateUser(String userDocumentID, String driverLicenseNumber){
		User user = users.get(userDocumentID);
		if(user == null){
			user = new User(userDocumentID,driverLicenseNumber);
		}
		users.put(userDocumentID,user);
		return user;
	}
	
	/** Adds rental to the map that contains all user's rentals sorted by date and time of the rental. */
	private void addToUserRentalMap(){
		TreeMap<Date, List<Rental>> userRentals = rentalsByUser.getOrDefault(user, new TreeMap<>());
		List<Rental> rentalsAtSameTime = userRentals.getOrDefault(rentalDateTime, new ArrayList<>());
		rentalsAtSameTime.add(this);
		userRentals.put(rentalDateTime, rentalsAtSameTime);
		rentalsByUser.put(user, userRentals);
	}
	
	/** Processes rentals by adding additional discount for user's every 10th rental. */
	public static void processRentals(){
		for(Map.Entry<User, TreeMap<Date, List<Rental>>> entry : rentalsByUser.entrySet()){
			User user = entry.getKey();
			TreeMap<Date, List<Rental>> rentals = entry.getValue();
			
			user.resetRentCounter();
			
			for(Map.Entry<Date, List<Rental>> rentalEntry : rentals.entrySet()){
				List<Rental> rentalsAtSameTime = rentalEntry.getValue();
				for(Rental rental : rentalsAtSameTime){
					user.incrementRentCounter();
					
					if(user.getRentCounter() % 10 == 0){
						rental.discount = true;
					}
				}
			}
		}
	}
	
	/**
	 * Returns fault that occurred on the vehicle at the specified time
	 * @param vehicle vehicle whose faults are being checked
	 * @param rentalDateTime date and time of the potential fault
	 * @return {@link Fault} object if the fault exists, otherwise null
	 */
	private static Fault getVehiclesFault(Vehicle vehicle, Date rentalDateTime){
		List<Fault> faultsOnOneVehicle = vehicleFaultsMap.get(vehicle);
		if(faultsOnOneVehicle != null){
			for(Fault fault : faultsOnOneVehicle){
				if(!fault.getDateTime().before(rentalDateTime) && !fault.getDateTime().after(rentalDateTime)){
					return fault;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Updates map of vehicle's faults by adding a new fault and keeps the map sorted by date and time.
	 * @param vehicle vehicle on which the fault occurred
	 * @param fault fault that occurred
	 */
	private static void updateVehicleFaultsMap(Vehicle vehicle, Fault fault){
		vehicleFaultsMap.putIfAbsent(vehicle, new ArrayList<>());
		List<Fault> faults = vehicleFaultsMap.get(vehicle);
	    faults.add(fault);

	    faults.sort(Comparator.comparing(Fault::getDateTime));
	}
	
	/**
	 * Returns rental's date and time.
	 * @return precise date and time
	 */
	public Date getRentalDateTime(){
		return rentalDateTime;
	}
	
	/**
	 * Returns vehicle's ID.
	 * @return vehicle's ID
	 */
	public String getID(){
		return ID;
	}
	
	/**
	 * Returns user that rented the vehicle.
	 * @return {@link User} object
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * Returns start location of the rental
	 * @return start location
	 */
	public String getStartLocation(){
		return startLocation;
	}
	
	/**
	 * Returns goal location of the rental
	 * @return goal location
	 */
	public String getGoalLocation(){
		return goalLocation;
	}
	
	/**
	 * Returns the information whether the fault happened.
	 * @return {@code true} if the fault occurred, otherwise {@code false}
	 */
	public Boolean isFault(){
		return fault;
	}
	
	/**
	 * Returns the information whether there was promotional discount during the rental.
	 * @return {@code true} if there was promotional discount, otherwise {@code false}
	 */
	public Boolean isPromo(){
		return promo;
	}
	
	/**
	 * Returns duration of the rental in seconds.
	 * @return duration of the rental
	 */
	public Long getDuration(){
		return duration;
	}
	
	/**
	 * Returns rental's end time.
	 * @return precise end date and time of the rental
	 */
	public Date getRentalEndTime(){
		return new Date(rentalDateTime.getTime() + duration * 1000);
	}
	
	/**
	 * Returns the rented vehicle.
	 * @return {@code Vehicle} object
	 */
	public Vehicle getVehicle(){
		return VehicleParser.getVehicle(ID);
	}
	
	/**
	 * Returns rental's unique ID.
	 * @return rental's ID
	 */
	public static Integer getRentalID(){
		return rentalID;
	}
	
	/** Increments the rental's unique ID. */
	public static void incrementRentalID(){
		rentalID++;
	}
	
	/**
	 * Returns map that contains vehicles and their faults
	 * @return map with vehicles and their faults
	 */
	public static Map<Vehicle, List<Fault>> getVehicleFaultsMap(){
		return vehicleFaultsMap;
	}
	
	/**
	 * Returns fault registered during rental.
	 * @return {@code Fault} object if the fault happened during rental, otherwise null
	 */
	public Fault getRegisteredFault(){
		return registeredFault;
	}
}