package emobility.rental;

import java.util.*;
import java.io.*;
import java.text.*;
import emobility.vehicles.*;

/**
 * Utility class for parsing rental data from a CSV file and managing the list of rentals.
 * This class is responsible for reading rental information from a file, adding it to collection, 
 * and providing access to the parsed data.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class RentalParser{
	/**
	 * A set of unique rental keys that are a combination of vehicle's ID and the precise time the rental happened, 
	 * which should always be unique as the same vehicle can only be rented once at a given time.
	 */
	private static Set<String> rentalCheck = new HashSet<>();

	/** A list that contains all parsed rentals. */
	private static List<Rental> rentals = new ArrayList<>();
	
	/**
	 * Returns a list of all parsed rentals.
	 * @return a list of {@link Rental} objects
	 */
	public static List<Rental> getRentals(){
		return rentals;
	}
	
	/**
	 * Parses rentals data from a CSV file located at the specified file path.
	 * The method reads the file, processes each line to create {@link Rental} objects, 
	 * and stores these objects in the appropriate list.
	 * @param filePath the path to the CSV file containing rental data
	 */
	public static void parseRentalCSV(String filePath){
		List<String> validIDs = new ArrayList<>();
		validIDs = VehicleParser.getValidIDs();
		System.out.println("valid IDs: " + validIDs);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String line;
			br.readLine(); // skipping header of the file
			while((line = br.readLine()) != null){
				if(line.isEmpty())
					continue;
				
				String data[] = line.split(",");
				
				if(data.length == 10){
					String ID = data[2];
					if(!ID.isEmpty() && validIDs.contains(ID)){
						try{
							Date dateTime = sdf.parse(data[0]);
							
							String rentalKey = ID + "-" + dateTime.getTime();
							
							if(!rentalCheck.contains(rentalKey)){
								String userID = data[1];
								User user = new User(userID);
								if(userID.isEmpty()){
									System.out.println("User info missing.");
									continue;
								}
								
								try{
									String startXString = data[3].replace("\"", "");
									Integer startX = Integer.parseInt(startXString);
									
									String startYString = data[4].replace("\"", "");
									Integer startY = Integer.parseInt(startYString);
									
									String startLocation = startXString + "," + startYString;
									
									String goalXString = data[5].replace("\"", "");
									Integer goalX = Integer.parseInt(goalXString);
									
									String goalYString = data[6].replace("\"", "");
									Integer goalY = Integer.parseInt(goalYString);
									
									String goalLocation = goalXString + "," + goalYString;
									
									if(startX >= 0 && startX <= 19 && startY >= 0 && startY <= 19 && goalX >= 0 && goalX <= 19 && goalY >= 0 && goalY <= 19){
										Long duration = Long.parseLong(data[7]);
										Boolean fault = "da".equalsIgnoreCase(data[8]);
										Boolean promo = "da".equalsIgnoreCase(data[9]);
											
										rentals.add(new Rental(dateTime,user,ID,startLocation,goalLocation,fault,duration,promo));
										rentalCheck.add(rentalKey);
									} else{
										System.out.println("Coordinates out of bounds: (" + startX + "," + startY + ") -> (" + goalX + "," + goalY + ")");
										continue;
									}
								} catch(NumberFormatException e){
									System.out.println("Invalid number format in coordinates or duration: " + Arrays.toString(data));
									e.printStackTrace();
									continue;
								}
							} else{
								System.out.println("Vehicle " + ID + " already rented out at " + sdf.format(dateTime));
								continue;
							}
						} catch(ParseException e){
							System.out.println("Invalid date format for rental date: " + data[0]);
							e.printStackTrace();
							continue;
						}
					} else{
						System.out.println("Invalid vehicle's ID: " + ID + ".");
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