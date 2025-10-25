package emobility.vehicles;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Utility class for parsing vehicle data from a CSV file and managing the list of vehicles.
 * <p>This class is responsible for reading {@link Vehicle} information from a file, sorting it in collections, 
 * and providing access to the parsed data.
 * It supports parsing and storing different types of vehicles, including {@code Car}, {@code ElectricBicycle}, and {@code ElectricScooter}.</p>
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class VehicleParser{
	/** A map that stores all vehicles by their IDs. */
	private static Map<String,Vehicle> vehicles = new HashMap<>();
	
	/** A list that contains all valid vehicle IDs. */
	private static List<String> validIDs = new ArrayList<>();
	
	/** A list that contains all parsed cars. */
	private static List<Car> cars = new ArrayList<>();
	
	/** A list that contains all parsed bicycles. */
	private static List<ElectricBicycle> bicycles = new ArrayList<>();
	
	/** A list that contains all parsed scooters. */
	private static List<ElectricScooter> scooters = new ArrayList<>();
	
	/**
	 * Returns a map of all vehicles parsed from the CSV file.
	 * The map is keyed by the vehicles' IDs.
	 * @return a map of all vehicles
	 */
	public static Map<String,Vehicle> getAllVehicles(){
		return vehicles;
	}
	
	/**
	 * Returns a specific vehicle by its ID.
	 * @param ID vehicle's ID
	 * @return the vehicle with the specified ID, or null if no such vehicle exists
	 */
	public static Vehicle getVehicle(String ID){
		return vehicles.get(ID);
	}
	
	/**
	 * Returns a list of all valid vehicle IDs.
	 * @return a list of valid vehicle IDs
	 */
	public static List<String> getValidIDs(){
		return validIDs;
	}
	
	/**
	 * Returns a list of parsed cars.
	 * @return a list of {@link Car} objects
	 */
	public static List<Car> getCars(){
		return cars;
	}
	
	/**
	 * Returns a list of parsed bicycles.
	 * @return a list of {@link ElectricBicycle} objects
	 */
	public static List<ElectricBicycle> getBicycles(){
		return bicycles;
	}
	
	/**
	 * Returns a list of parsed scooters.
	 * @return a list of {@link ElectricScooter} objects
	 */
	public static List<ElectricScooter> getScooters(){
		return scooters;
	}
	
	/**
	 * Parses vehicles data from a CSV file located at the specified file path.
	 * The method reads the file, processes each line to create corresponding {@link Vehicle} objects based on their type, 
	 * and stores these objects in the appropriate collections.
	 * @param filePath the path to the CSV file containing vehicle data
	 */
	public static void parseVehicleCSV(String filePath){
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String line;
			br.readLine(); // skipping header of the file
			
			while((line = br.readLine()) != null){
				if(line.isEmpty())
					continue;
				
				String data[] = line.split(",");
				
				if(data.length == 9){
					String ID = data[0];
					if(ID.isEmpty()){
						System.out.println("Vehicle ID is empty.");
						continue;
					}
					if(!validIDs.contains(ID)){
						String manufacturer = data[1];
						String model = data[2];
						if(manufacturer.isEmpty() || model.isEmpty()){
							System.out.println("Vehicle information missing.");
							continue;
						}
						
						try{
							Double purchasePrice = Double.parseDouble(data[4]);
							String type = data[8];
							
							switch(type.toLowerCase()){
								case "automobil": 
									try{
										Date purchaseDate = sdf.parse(data[3]);

										String description = data[7];
										Car car = new Car(ID,manufacturer,model,purchasePrice,100,true,purchaseDate,description);
										vehicles.put(ID, car);
										cars.add(car);
									} catch(ParseException e){
										System.out.println("Invalid date format for purchase date: " + data[3]);
										e.printStackTrace();
										continue;
									}
									
									break;

								case "bicikl":
									try{
										Integer autonomy = Integer.parseInt(data[5]);
										ElectricBicycle bicycle = new ElectricBicycle(ID,manufacturer,model,purchasePrice,100,autonomy);
										vehicles.put(ID, bicycle);
										bicycles.add(bicycle);
									} catch(NumberFormatException e){
										System.out.println("Invalid number format for bicycle's autonomy: " + data[5]);
										e.printStackTrace();
										continue;
									}
									
									break;
								
								case "trotinet":
									try{
										Integer maxSpeed = Integer.parseInt(data[6]);
										ElectricScooter scooter = new ElectricScooter(ID,manufacturer,model,purchasePrice,100,maxSpeed);
										vehicles.put(ID, scooter);
										scooters.add(scooter);
									} catch(NumberFormatException e){
										System.out.println("Invalid number format for scooter's max speed: " + data[6]);
										e.printStackTrace();
										continue;
									}
									
									break;
								
								default:
									System.out.println("Unexpected type of vehicle: " + type);
									continue;
							}
							
							validIDs.add(ID);
							
						} catch(NumberFormatException e){
							System.out.println("Invalid number format for purchase price: " + data[4]);
							e.printStackTrace();
							continue;
						}
					} else{
						System.out.println("Duplicate vehicle ID: " + ID);
						continue;
					}
				} else{
					System.out.println("Data not formatted correctly: " + line);
					continue;
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}