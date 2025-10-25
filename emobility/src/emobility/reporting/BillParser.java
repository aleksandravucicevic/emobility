package emobility.reporting;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import emobility.utility.FormatDate;

/**
 * Utility class for parsing data from text files which represent bills generated during rentals.
 * This class is responsible for reading financial and other useful information from bills into {@link Bill} objects, 
 * adding them to collections, and providing access to the parsed data.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class BillParser{
	/** A list of all parsed bills. */
	private static List<Bill> bills = new ArrayList<>();
	
	/** A set of processed bill IDs to avoid duplicates. */
	private static Set<Integer> processedBills = new HashSet<>();
	
	/** A map of bills grouped by the date they were issued. */
	private static Map<Date, List<Bill>> billsByDate = new TreeMap<>();
	
	/**
	 * Gets the list of all parsed bills.
	 * @return a list of {@code Bill} objects
	 */
	public static List<Bill> getBills(){
		return bills;
	}
	
	/**
	 * Gets a map of bills grouped by the date they were issued.
	 * @return a map with date as the key and {@code Bill} object as value
	 */
	public static Map<Date, List<Bill>> getBillsByDate(){
		return billsByDate;
	}
	
	/**
	 * Parses all bill files in the specified directory and stores the {@code Bill} objects in a list. 
	 * Only files with a ".txt" extension are considered.
	 * @param directoryPath the path of the directory containing the bill files
	 */
	public static void parseBills(String directoryPath){
		File directory = new File(directoryPath);
			if(!directory.exists() || !directory.isDirectory()){
				System.out.println("Invalid directory path: " + directoryPath);
				return;
			}
			
		for(File file : directory.listFiles()){
			if(file.isFile() && file.getName().endsWith(".txt")){
				Bill bill = parseSingleBill(file.getPath());
				if(bill != null){
					bills.add(bill);
				}
			}
		}
	}
	
	/**
	 * Parses a single bill from a file.
	 * @param filePath the path of the file containing the bill information
	 * @return the parsed {@code Bill} object, or {@code null} if the bill couldn't be processed
	 */
	public static Bill parseSingleBill(String filePath){
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Integer billID = 0;
		String area = "";
		String vehicleID = "";
		Date dateTime = null;
		Boolean fault = false;
		
		Double basePrice = 0.0;
		Double distancePrice = 0.0;
		Double discountPrice = 0.0;
		Double promoPrice = 0.0;
		Double totalPrice = 0.0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String line;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.isEmpty())
					continue;
				
				String data[] = line.split(":",2);
				if(data.length < 2)
					continue;
				
				String key = data[0].trim();
				String value = data[1].trim();
				
				try{
					switch(key.toLowerCase()){
						case "bill":
							billID = Integer.parseInt(value);
							break;
							
						case "for":
							String areaInfo[] = value.split(" ");
							if(areaInfo.length == 2){
								area = areaInfo[0];
							} else{
								System.out.println("Area info is incomplete.");
								area = "";
							}
							break;
							
						case "rental of the vehicle":
							vehicleID = value;
							break;
							
						case "date and time":
							dateTime = sdf.parse(value);
							break;
							
						case "fault":
							if(value != null){
								fault = "yes".equalsIgnoreCase(value);
							} else{
								System.out.println("Fault value is null.");
								fault = false;
							}
							
							break;
							
						case "base price":
							basePrice = Double.parseDouble(value);
							break;
							
						case "distance factor":
							distancePrice = Double.parseDouble(value);
							break;
							
						case "discount factor":
							discountPrice = Double.parseDouble(value);
							break;
							
						case "promo factor":
							promoPrice = Double.parseDouble(value);
							break;
							
						case "total price":
							totalPrice = Double.parseDouble(value);
							break;
							
						default:
							break;
					}
					
				} catch(NumberFormatException e){
					System.out.println("Error parsing number for key: " + key + " with value: " + value);
					e.printStackTrace();
					return null;
				} catch(ParseException e){
					System.out.println("Error parsing date for key: " + key + " with value: " + value);
					e.printStackTrace();
					return null;
				}
			}
		} catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		if(billID == null || vehicleID.isEmpty() || dateTime == null || area.isEmpty()){
			System.out.println("Missing important data.");
			return null;
		}
		
		Bill bill = new Bill(billID,area,vehicleID,dateTime,fault,basePrice,distancePrice,discountPrice,promoPrice,totalPrice);
		
		if(processedBills.contains(billID)){
	        System.out.println("Bill already processed: " + billID);
	        return null;
	    }
		
		processedBills.add(billID);
		
		Date mapKey = FormatDate.truncateTime(dateTime);
		billsByDate.putIfAbsent(mapKey, new ArrayList<>());
		billsByDate.get(mapKey).add(bill);
		
		return bill;
	}
}
