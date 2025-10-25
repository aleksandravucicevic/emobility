package emobility.reporting;

import java.util.List;
import java.io.*;
import emobility.vehicles.*;

/**
 * Performs loss analysis for vehicles, finding the vehicle with the greatest loss 
 * for each type of the vehicle ({@code Car}, {@code ElectricBicycle}, {@code ElectricScooter}). 
 * This class also handles serialization and deserialization of the loss data.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class LossAnalysis implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** A list of all bills. */
	private static transient List<Bill> allBills = BillParser.getBills();
	
	/** The car with the greatest loss. */
	private Car carWithGreatestLoss;
	
	/** The bicycle with the greatest loss. */
	private ElectricBicycle bicycleWithGreatestLoss;
	
	/** The scooter with the greatest loss. */
	private ElectricScooter scooterWithGreatestLoss;
	
	/** The greatest loss for cars. */
	private Double maxCarLoss;
	
	/** The greatest loss for bicycles. */
	private Double maxBicycleLoss;
	
	/** The greatest loss for scooters. */
	private Double maxScooterLoss;
	
	/** Path to the directory where loss analysis files are stored. */
	private String lossAnalysisDirectoryPath;
	/** Directory for storing loss analysis files. */
	private File lossAnalysisDirectory;
	/** Filename for storing the information about the car and its loss. */
	private String carLossFileName;
	/** Filename for storing the information about the bicycle and its loss. */
	private String bicycleLossFileName;
	/** Filename for storing the information about the scooter and its loss. */
	private String scooterLossFileName;
	
	/**
	 * Constructs a {@code LossAnalysis} object and performs loss calculations for each vehicle type.
	 * @param lossAnalysisDirectoryPath path to the directory where the loss data files will be stored
	 */
	public LossAnalysis(String lossAnalysisDirectoryPath){
		maxCarLoss = 0.0;
		maxBicycleLoss = 0.0;
		maxScooterLoss = 0.0;
		
		carWithGreatestLoss = null;
		bicycleWithGreatestLoss = null;
		scooterWithGreatestLoss = null;
		
		if(lossAnalysisDirectoryPath != null && !lossAnalysisDirectoryPath.isEmpty()){
			this.lossAnalysisDirectoryPath = lossAnalysisDirectoryPath;
			this.lossAnalysisDirectory = new File(this.lossAnalysisDirectoryPath);
			if(!lossAnalysisDirectory.exists()){
				boolean created = lossAnalysisDirectory.mkdirs();
				if(!created){
					throw new RuntimeException("Failed to create directory: " + lossAnalysisDirectoryPath);
				}
			}
			
			this.carLossFileName = "car_with_highest_loss.ser";
			this.bicycleLossFileName ="bicycle_with_highest_loss.ser";
			this.scooterLossFileName = "scooter_with_highest_loss.ser";
			
			calculateLossesForEachVehicleType();
			
		} else{
			System.out.println("Invalid directory path");
		}
	}
	
	/**
	 * Constructs a {@code LossAnalysis} object without performing calculations, for deserialization purposes.
	 * @param lossAnalysisDirectoryPath path to the directory where loss data files are stored
	 * @param dummy dummy parameter to differentiate this constructor from the one that performs the calculations.
	 */
	public LossAnalysis(String lossAnalysisDirectoryPath, Boolean dummy){
		if(lossAnalysisDirectoryPath != null && !lossAnalysisDirectoryPath.isEmpty()){
			this.lossAnalysisDirectoryPath = lossAnalysisDirectoryPath;
			this.lossAnalysisDirectory = new File(this.lossAnalysisDirectoryPath);
			if(lossAnalysisDirectory.exists()){
				this.carLossFileName = "car_with_highest_loss.ser";
				this.bicycleLossFileName ="bicycle_with_highest_loss.ser";
				this.scooterLossFileName = "scooter_with_highest_loss.ser";
				
				maxCarLoss = 0.0;
				maxBicycleLoss = 0.0;
				maxScooterLoss = 0.0;
				
				carWithGreatestLoss = null;
				bicycleWithGreatestLoss = null;
				scooterWithGreatestLoss = null;
			} else{
				System.out.println("Directory does not exist.");
				return;
			}
		} else{
			System.out.println("Invalid directory path.");
			return;
		}
	}
	
	/**
	 * Gets the car with the greatest loss.
	 * @return {@link Car} with the greatest loss
	 */
	public Car getCarWithGreatestLoss(){
		return carWithGreatestLoss;
	}
	
	/**
	 * Gets the bicycle with the greatest loss.
	 * @return {@link ElectricBicycle} with the greatest loss
	 */
	public ElectricBicycle getBicycleWithGreatestLoss(){
		return bicycleWithGreatestLoss;
	}
	
	/**
	 * Gets the scooter with the greatest loss.
	 * @return {@link ElectricScooter} with the greatest loss
	 */
	public ElectricScooter getScooterWithGreatestLoss(){
		return scooterWithGreatestLoss;
	}
	
	/**
	 * Gets the greatest loss for cars.
	 * @return greatest loss for cars
	 */
	public Double getMaxCarLoss(){
		return maxCarLoss;
	}
	
	/**
	 * Gets the greatest loss for bicycles.
	 * @return greatest loss for bicycles
	 */
	public Double getMaxBicycleLoss(){
		return maxBicycleLoss;
	}
	
	/**
	 * Gets the greatest loss for scooters.
	 * @return greatest loss for scooters
	 */
	public Double getMaxScooterLoss(){
		return maxScooterLoss;
	}
	
	/** Calculates the losses for each vehicle type based on the bills data. */
	public void calculateLossesForEachVehicleType(){
		Double carLoss = 0.0;
		Double bicycleLoss = 0.0;
		Double scooterLoss = 0.0;
		Double repairCoefficient = 0.0;
		Double purchasePrice = 0.0;
		
		for(Bill bill : allBills){
			if(bill.isFault()){
				String vehicleType = bill.getVehicleType();
				switch(vehicleType.toLowerCase()){
					case "car":
						repairCoefficient = ReportGenerator.getCarRepairCoefficient();
						purchasePrice = bill.getVehiclePurchasePrice();
						carLoss = repairCoefficient * purchasePrice;
						
						if(carLoss > maxCarLoss){
							maxCarLoss = carLoss;
							carWithGreatestLoss = (Car) bill.getVehicle();
						}
						break;
						
					case "bicycle":
						repairCoefficient = ReportGenerator.getBicycleRepairCoefficient();
						purchasePrice = bill.getVehiclePurchasePrice();
						bicycleLoss = repairCoefficient * purchasePrice;
						
						if(bicycleLoss > maxBicycleLoss){
							maxBicycleLoss = bicycleLoss;
							bicycleWithGreatestLoss = (ElectricBicycle) bill.getVehicle();
						}
						break;
					case "scooter":
						repairCoefficient = ReportGenerator.getScooterRepairCoefficient();
						purchasePrice = bill.getVehiclePurchasePrice();
						scooterLoss = repairCoefficient * purchasePrice;
						
						if(scooterLoss > maxScooterLoss){
							maxScooterLoss = scooterLoss;
							scooterWithGreatestLoss = (ElectricScooter) bill.getVehicle();
						}
						break;
					default:
						System.out.println("Invalid vehicle type: " + vehicleType);
						continue;
				}
			}
		}
	}
	
	/**
	 * Serializes the vehicle loss data to a file.
	 * @param vehicle vehicle with the greatest loss
	 * @param loss vehicle's loss
	 * @param filePath file path where the data should be stored
	 */
	public void serializeVehicleLossToFile(Vehicle vehicle, Double loss, String filePath){
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
			oos.writeObject(vehicle);
			oos.writeObject(loss);
			System.out.println("Vehicle and its loss successfully serilaized to " + filePath);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/** Serializes the loss analysis report to files. */
	public void serializeReport(){
		serializeVehicleLossToFile(carWithGreatestLoss,maxCarLoss,lossAnalysisDirectoryPath + carLossFileName);
		serializeVehicleLossToFile(bicycleWithGreatestLoss,maxBicycleLoss,lossAnalysisDirectoryPath + bicycleLossFileName);
		serializeVehicleLossToFile(scooterWithGreatestLoss,maxScooterLoss,lossAnalysisDirectoryPath + scooterLossFileName);
	}
	
	/**
	 * Deserializes the vehicle loss data from a file.
	 * @param filePath file path from where the data is read
	 * @return a {@link Pair} object containing the vehicle and the associated loss value
	 */
	public Pair<Vehicle, Double> deserializeVehicleLossFromFile(String filePath){
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
	        Vehicle vehicle = (Vehicle) ois.readObject();
	        Double loss = (Double) ois.readObject();
	        return new Pair<>(vehicle,loss);
	    } catch (IOException | ClassNotFoundException e){
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * Deserializes the entire loss analysis report from files.
	 * @param directoryPath directory path where the report files are stored
	 * @return a {@code LossAnalysis} object populated with the deserilaized data.
	 */
	public LossAnalysis deserializeReport(String directoryPath){
		LossAnalysis deserializedLossAnalysis = new LossAnalysis(directoryPath,true);
		
		Pair<Vehicle, Double> carLossData = deserializeVehicleLossFromFile(lossAnalysisDirectoryPath + carLossFileName);
		if(carLossData == null || carLossData.getKey() == null){
			System.out.println("Error deserializing car loss data: " + carLossFileName + " - No available data.");
		} else if(carLossData.getKey().getType().equalsIgnoreCase("car")){
			deserializedLossAnalysis.carWithGreatestLoss = (Car) carLossData.getKey();
			deserializedLossAnalysis.maxCarLoss = carLossData.getValue();
		}
		
		Pair<Vehicle, Double> bicycleLossData = deserializeVehicleLossFromFile(lossAnalysisDirectoryPath + bicycleLossFileName);
		if(bicycleLossData == null || bicycleLossData.getKey() == null){
			System.out.println("Error deserializing bicycle loss data: " + bicycleLossFileName + " - No available data.");
		} else if(bicycleLossData.getKey().getType().equalsIgnoreCase("bicycle")){
			deserializedLossAnalysis.bicycleWithGreatestLoss = (ElectricBicycle) bicycleLossData.getKey();
			deserializedLossAnalysis.maxBicycleLoss = bicycleLossData.getValue();
		}
		
		Pair<Vehicle, Double> scooterLossData = deserializeVehicleLossFromFile(lossAnalysisDirectoryPath + scooterLossFileName);
		if(scooterLossData == null || scooterLossData.getKey() == null){
			System.out.println("Error deserializing scooter loss data: " + scooterLossFileName + " - No available data.");
		} else if(scooterLossData.getKey().getType().equalsIgnoreCase("scooter")){
			deserializedLossAnalysis.scooterWithGreatestLoss = (ElectricScooter) scooterLossData.getKey();
			deserializedLossAnalysis.maxScooterLoss = scooterLossData.getValue();
		}
		
		return deserializedLossAnalysis;
	}
}
