package emobility.rental;

/**
 * Represents a user.
 * This class contains information about the user's ID document, such as ID for local users, or passport for foreign users, 
 * driver's license number for users who rent cars, and rent counter which represents the number of rentals for each user.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class User{
	/** User's rent counter. */
	private Integer rentCounter = 0;
	
	/** User's ID document - typically ID or passport. */
	private String idDocument;
	
	/** User's driver's license numbers - needed only for car rentals. */
	private String driverLicenseNumber;
	
	/**
	 * Constructs a new User with specified ID document and driver's license number.
	 * @param idDocument user's ID document
	 * @param driverLicenseNumber user's driver's license number
	 */
	public User(String idDocument, String driverLicenseNumber){
		this.idDocument = idDocument;
		this.driverLicenseNumber = driverLicenseNumber;
	}
	
	/**
	 * Constructs a new User with specified ID document and sets driver's license number to "Unknown".
	 * @param idDocument user's ID document
	 */
	public User(String idDocument){
		this.idDocument = idDocument;
		this.driverLicenseNumber = "Unknown";
	}
	
	/**
	 * Returns a string representation of user, including useful information about user.
	 * @return string representation of user
	 */
	@Override
	public String toString(){
		return idDocument + "," + driverLicenseNumber;
	}
	
	/**
	 * Returns user's rent counter.
	 * @return rent counter
	 */
	public Integer getRentCounter(){
		return rentCounter;
	}
	
	/** Increments user's rent counter. */
	public void incrementRentCounter(){
		rentCounter++;
	}
	
	/** Resets user's rent counter by setting its value to zero. */
	public void resetRentCounter(){
		rentCounter = 0;
	}
	
	/**
	 * Returns user's ID document.
	 * @return ID document
	 */
	public String getIdDocument(){
		return idDocument;
	}
	
	/**
	 * Returns user's driver's license number
	 * @return driver's license number
	 */
	public String getDriverLicenseNumber(){
		return driverLicenseNumber;
	}
}