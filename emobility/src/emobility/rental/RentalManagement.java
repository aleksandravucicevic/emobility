package emobility.rental;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import emobility.utility.*;
import emobility.vehicles.*;

/**
 * This class extends {@link Rental} class and is responsible for managing the financial aspects of rentals, 
 * including calculating costs and generating bills.
 * <p>It also provides utility methods to group rentals by date and time, and handles the battery charging
 * logic between rentals for specific vehicle.</p>
 */
public class RentalManagement extends Rental{
	/** Reader for accessing pricing data from a properties file. */
	private PricingReader pr;
	
	/** Directory path where all the bills will be saved. */
	private String billsDirectoryPath;
	
	/** File object representing the directory where the bills will be saved. */
	private File billsDirectory;

	/** Base price of the rental. */
	private Double basePrice;
	
	/** Factor calculated based on the area in which the vehicle operated during the rental. */
	private Double distanceFactor;
	
	/** Factor calculated based on the number of user's rentals by the time current rental is done. */
	private Double discountFactor;
	
	/** Factor calculated based on the existence of a promotional discount by the time current rental is done. */
	private Double promoFactor;
	
	/** Total price of the rental. */
	private Double totalPrice;
	
	/** The area in which the vehicle operated during the rental. */
	private String area;
	
	/** Unique ID of the bill created for current rental. */
	private Integer billID;
	
	/**
	 * Constructs a {@code RentalManagement} object using data from an existing 
	 * {@code Rental} object and initializes pricing information and billing directory.
	 * @param pricingPropertiesFilePath the file path to the pricing properties file
	 * @param billsDirectoryPath the directory path where the bills will be saved
	 * @param rental the {@code Rental} object containing rental data
	 */
	public RentalManagement(String pricingPropertiesFilePath, String billsDirectoryPath, Rental rental){
		super(rental);
		this.pr = new PricingReader(pricingPropertiesFilePath);
		this.billsDirectoryPath = billsDirectoryPath;
		this.billsDirectory = new File(this.billsDirectoryPath);
		if(!billsDirectory.exists()){
			billsDirectory.mkdirs();
		}
	}
	
	/**
	 * Returns a string representation of the rental's bill with all pricing information.
	 * @return a string representation of the rental's bill
	 */
	@Override
	public String toString(){
		return "Bill:" + billID + "\nfor:" + area + " area \n" + super.toString() + "\n\nCost info:\nbase price:" + String.format(Locale.US, "%.2f", basePrice) + "\ndistance factor:" + String.format(Locale.US, "%.2f", distanceFactor) + "\ndiscount factor:" + String.format(Locale.US, "%.2f", discountFactor) + "\npromo factor:" + String.format(Locale.US, "%.2f", promoFactor) + "\n\nTotal price:" + String.format(Locale.US, "%.2f", totalPrice);
	}
	
	/** Sets the area for the rental. */
	public void setArea(){
		area = isWideArea() ? "wide" : "narrow";
	}
	
	/**
	 * Returns area in which the vehicle operates during the rental.
	 * @return rental area
	 */
	public String getArea(){
		return area;
	}
	
	/**
	 * Returns the unique ID of the bill for this rental.
	 * @return bill's ID
	 */
	public Integer getBillID(){
		return billID;
	}
	
	/**
	 * Determines if the rental takes place in the wide area.
	 * @return {@code true} if the rental is in a wide area, otherwise {@code false}
	 */
	private boolean isWideArea(){
		String startCoordinates[] = startLocation.split(",");
		String goalCoordinates[] = goalLocation.split(",");
		Integer startX = Integer.parseInt(startCoordinates[0]);
		Integer startY = Integer.parseInt(startCoordinates[1]);
		Integer goalX = Integer.parseInt(goalCoordinates[0]);
		Integer goalY = Integer.parseInt(goalCoordinates[1]);
		
		return (startX < 5 || startX > 14 || startY < 5 || startY > 14 || goalX < 5 || goalX > 14 || goalY < 5 || goalY > 14);
	}
	
	/** Calculates the base price of the rental based on the vehicle's type and duration of the rental. */
	private void calculateBasePrice(){
		basePrice = 0.0;
		Double unitPrice = 0.0;
		Vehicle vehicle = VehicleParser.getVehicle(this.ID);
		if(vehicle != null){
			String type = vehicle.getType();
			
			switch(type.toLowerCase()){
				case "car":
					unitPrice = pr.getDoubleProperty("CAR_UNIT_PRICE");
					break;
				
				case "bicycle":
					unitPrice = pr.getDoubleProperty("BIKE_UNIT_PRICE");
					break;
					
				case "scooter":
					unitPrice = pr.getDoubleProperty("SCOOTER_UNIT_PRICE");
					break;
					
				default:
					System.out.println("Invalid vehicle type: " + type);
					return;
			}
			
			basePrice = unitPrice * duration; 
		} else{
			System.out.println("Invalid vehicle ID: " + ID);
			return;
		}
	}
	
	/** Calculates the distance factor based on the rental's area. */
	private void calculateDistanceFactor(){
		distanceFactor = isWideArea() ? pr.getDoubleProperty("DISTANCE_WIDE") : pr.getDoubleProperty("DISTANCE_NARROW");
	}
	
	/** Calculates the discount factor it the rental qualifies for a discount. */
	private void calculateDiscountFactor(){
		discountFactor = 0.0;
		if(discount){
			discountFactor = (pr.getDoubleProperty("DISCOUNT")/100.0);
		}
	}
	
	/** Calculates the promotional factor if the rental qualifies for a promotional discount. */
	private void calculatePromoFactor(){
		promoFactor = 0.0;
		if(promo){
			promoFactor = (pr.getDoubleProperty("DISCOUNT_PROM")/100.0);
		}
	}
	
	/**
	 * Calculates the total price of the rental based on the base price, distance factor, discount factor and promotional factor.
	 * @param basePrice the rental's base price
	 * @param distanceFactor the distance factor
	 * @param discountFactor the discount factor
	 * @param promoFactor the promotional factor
	 */
	private void calculateTotalPrice(Double basePrice, Double distanceFactor, Double discountFactor, Double promoFactor){
		if(fault){
			this.basePrice = 0.0;
			totalPrice = 0.0;
		} else{
			Double primaryPrice = basePrice * distanceFactor;
			totalPrice = primaryPrice - (discountFactor * primaryPrice) - (promoFactor * primaryPrice);
		}
	}
	
	/** Generates a bill for the rental and saves it to the file in the designated folder. */
	public void generateBill(){
		setArea();
		calculateBasePrice();
		calculateDistanceFactor();
		calculateDiscountFactor();
		calculatePromoFactor();
		
		calculateTotalPrice(basePrice,distanceFactor,discountFactor,promoFactor);
		
		Rental.incrementRentalID();
		billID = rentalID;
		String fileName = billID + "_rentbill.txt";
		
		File billFile = new File(billsDirectory,fileName);
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(billFile))){
			bw.write(toString());
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Groups all rentals by their rental date and time and maps the grouped rentals to that date and time.
	 * @return A map with the rental date and time as the key, and list of {@code Rental} objects grouped by that date and time
	 */
	private static Map<Date,List<Rental>> getRentalsGroupedByTime(){
		List<Rental> rentals = RentalParser.getRentals();
		return rentals.stream().collect(Collectors.groupingBy(Rental::getRentalDateTime));
	}
	
	/**
	 * Groups rentals by their exact date and time using {@getRentalsGroupedByTime()} and sorts them chronologically (by date and time), 
	 * forming lists (based on date of the rental) which consist of lists of {@code Rental} objects that happened at exact same time. 
	 * Maps the formed structure to exact date and time.
	 * @return A map with the rental date and time as the key, and list of lists of {@code Rental} objects is the value
	 */
	private static Map<Date, List<List<Rental>>> getRentalsGroupedByDateTime(){
		Map<Date, List<Rental>> groupedByDateTime = getRentalsGroupedByTime();		
		return groupedByDateTime.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.groupingBy(Map.Entry::getKey,LinkedHashMap::new,Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}
	
	/**
	 * Maps all rentals grouped by their rental time to their rental date (ignoring the time component).
	 * @return A map with the rental date (without time) as the key, and a list of lists of {@code Rental} objects grouped by the exact time of the day they happened
	 */
	public static Map<Date, List<List<Rental>>> getRentalsGroupedByDateAndTime(){
		Map<Date, List<List<Rental>>> originalMap = getRentalsGroupedByDateTime();
		
		Map<Date, List<List<Rental>>> truncatedMap = new LinkedHashMap<>();
		for(Map.Entry<Date, List<List<Rental>>> entry : originalMap.entrySet()){
			Date truncatedKey = FormatDate.truncateTime(entry.getKey());
			if(truncatedMap.containsKey(truncatedKey)){
				truncatedMap.get(truncatedKey).addAll(entry.getValue());
			} else{
				truncatedMap.put(truncatedKey, new ArrayList<>(entry.getValue()));
			}
		}
		
		return truncatedMap;
	}
	
	/**
	 * Retrieves all rentals for a specific vehicle.
	 * @param vehicle the vehicle to retrieve rentals for
	 * @return a list of rentals for the specified vehicle
	 */
	private List<Rental> getAllRentalsForVehicle(Vehicle vehicle){
		List<Rental> allRentals = RentalParser.getRentals();
		List<Rental> rentalsForVehicle = new ArrayList<>();
		
		for(Rental rental : allRentals){
			if(rental.getVehicle().equals(vehicle)){
				rentalsForVehicle.add(rental);
			}
		}
		
		rentalsForVehicle.sort(Comparator.comparing(Rental::getRentalDateTime));
		return rentalsForVehicle;
	}
	
	/**
	 * Finds the next rental date for the specific vehicle.
	 * @param vehicle the vehicle to find the next rental date for
	 * @return the date of the next rental of the specified vehicle, or {@code null} if there are no further rentals
	 */
	private Date findNextRentalDateForVehicle(Vehicle vehicle){
		List<Rental> rentals = getAllRentalsForVehicle(vehicle);
		
		for(Rental rental : rentals){
			if(rental.getRentalDateTime().after(this.rentalDateTime)){
				return rental.getRentalDateTime();
			}
		}
		
		return null;
	}
	
	/**
	 * Charges the vehicle's battery until the next rental, and if there are no further rentals of the vehicle sets the battery to 100%.
	 * @param vehicle the vehicle to charge
	 */
	public void chargeVehicleUntilNextRental(Vehicle vehicle){
		Date nextRentalDate = findNextRentalDateForVehicle(vehicle);
		
		if(nextRentalDate != null){
			Long timeBetweenRentals = nextRentalDate.getTime() - this.getRentalEndTime().getTime();
			Long chargingTimeInMinutes = timeBetweenRentals / (60*1000);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
			System.out.println("BATTERY CHARGING... next rental for the vehicle at " + sdf.format(nextRentalDate));
			
			Integer chargePerMinute = 1;
			Integer additionalCharge = chargingTimeInMinutes.intValue() * chargePerMinute;
			
			Integer newBatteryLevel = Math.min(100, vehicle.getBatteryLevel() + additionalCharge);
			vehicle.setBatteryLevel(newBatteryLevel);
			System.out.println("--> new battery level: " + vehicle.getBatteryLevel() + "\n");
		} else{
			System.out.println("No further rentals for vehicle " + vehicle.getID() + "\n");
			vehicle.setBatteryLevel(100);
		}
		
	}
}